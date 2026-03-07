package codes.viremox.salute.pipeline;

import codes.viremox.salute.storage.TextureStore;
import codes.viremox.salute.util.Retry;
import codes.viremox.salute.util.TextureCodec;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.mineskin.JsoupRequestHandler;
import org.mineskin.MineSkinClient;
import org.mineskin.request.GenerateRequest;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SkinUploader {

    private static final int RETRY_LIMIT = 3;
    private static final Duration RETRY_BASE_DELAY = Duration.ofSeconds(2);

    private final TextureStore textureStore;
    private final Logger logger;
    private volatile MineSkinClient client;

    @Inject
    public SkinUploader(TextureStore textureStore, @Named("salute") Logger logger) {
        this.textureStore = textureStore;
        this.logger = logger;
    }

    public void configure(String apiKey) {
        var builder = MineSkinClient.builder()
                .userAgent("salute")
                .requestHandler(JsoupRequestHandler::new);
        if (apiKey != null && !apiKey.isBlank()) {
            builder.apiKey(apiKey);
        }
        this.client = builder.build();
    }

    public CompletableFuture<Void> uploadAll(ManagedImage image) {
        if (!image.transition(PipelineState.IDLE, PipelineState.UPLOADING)
                && !image.transition(PipelineState.SPLITTING, PipelineState.UPLOADING)) {
            return CompletableFuture.failedFuture(
                    new IllegalStateException("Image '" + image.id() + "' is not in a valid state for uploading"));
        }

        CompletableFuture<?>[] jobs = image.splitResult().tiles().stream()
                .map(tile -> processOneTile(image, tile))
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(jobs)
                .whenComplete((ignored, error) -> {
                    if (image.isCancelled()) return;
                    if (error != null) {
                        image.forceState(PipelineState.FAILED);
                        logger.log(Level.WARNING, "Upload pipeline failed for '" + image.id() + "'", error);
                    }
                });
    }

    private CompletableFuture<Void> processOneTile(ManagedImage image, Tile tile) {
        String cached = textureStore.lookup(tile.fingerprint());
        if (cached != null) {
            image.recordTexture(tile.fingerprint(), cached);
            return CompletableFuture.completedFuture(null);
        }

        if (client == null) {
            return CompletableFuture.failedFuture(
                    new IllegalStateException("MineSkin client has not been configured"));
        }

        return Retry.withBackoff(() -> submitToMineSkin(tile), RETRY_LIMIT, RETRY_BASE_DELAY)
                .thenAccept(textureValue -> {
                    if (image.isCancelled()) return;
                    String compacted = TextureCodec.compact(textureValue);
                    image.recordTexture(tile.fingerprint(), compacted);
                    textureStore.store(tile.fingerprint(), compacted);
                });
    }

    private CompletableFuture<String> submitToMineSkin(Tile tile) {
        GenerateRequest request = GenerateRequest.upload(tile.pixels());
        return client.queue().submit(request)
                .thenCompose(queued -> client.queue().waitForCompletion(queued.getJob()))
                .thenCompose(jobRef -> jobRef.getOrLoadSkin(client))
                .thenApply(skin -> skin.texture().data().value());
    }
}
