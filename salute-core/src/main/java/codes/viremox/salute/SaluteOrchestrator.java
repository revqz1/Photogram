package codes.viremox.salute;

import codes.viremox.salute.config.ImageEntry;
import codes.viremox.salute.config.MotdEntry;
import codes.viremox.salute.display.MotdRenderer;
import codes.viremox.salute.hook.MiniPlaceholderHook;
import codes.viremox.salute.pipeline.HeadComponentFactory;
import codes.viremox.salute.pipeline.ManagedImage;
import codes.viremox.salute.pipeline.PipelineState;
import codes.viremox.salute.pipeline.SkinUploader;
import codes.viremox.salute.pipeline.SplitResult;
import codes.viremox.salute.pipeline.TileSplitter;
import codes.viremox.salute.storage.ConfigProvider;
import codes.viremox.salute.storage.TextureStore;
import codes.viremox.salute.util.Assets;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.kyori.adventure.text.Component;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SaluteOrchestrator {

    private final Path dataDirectory;
    private final Logger logger;
    private final ConfigProvider configProvider;
    private final SkinUploader skinUploader;
    private final TextureStore textureStore;

    private final Map<String, ManagedImage> registry = new ConcurrentHashMap<>();
    private volatile MotdRenderer motdRenderer;
    private MiniPlaceholderHook miniPlaceholderHook;

    @Inject
    public SaluteOrchestrator(
            @Named("dataDirectory") Path dataDirectory,
            @Named("salute") Logger logger,
            ConfigProvider configProvider,
            SkinUploader skinUploader,
            TextureStore textureStore
    ) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
        this.configProvider = configProvider;
        this.skinUploader = skinUploader;
        this.textureStore = textureStore;
    }

    public void start() {
        Assets.extractDefaults(dataDirectory);
        configProvider.reload();
        processAllImages();
        miniPlaceholderHook = MiniPlaceholderHook.attemptRegistration(this::images, logger);
    }

    public void reload() {
        cancelAllInFlight();
        configProvider.reload();
        processAllImages();
    }

    public void shutdown() {
        if (miniPlaceholderHook != null) miniPlaceholderHook.unregister();
        cancelAllInFlight();
        textureStore.persist();
    }

    private void processAllImages() {
        registry.clear();

        skinUploader.configure(configProvider.settings().api().mineSkinKey());
        motdRenderer = MotdRenderer.create(() -> registry);

        Path imageRoot = dataDirectory.resolve("images");

        for (var entry : configProvider.settings().images().entrySet()) {
            String imageId = entry.getKey();
            ImageEntry cfg = entry.getValue();

            try {
                BufferedImage source = cfg.readFromDisk(imageRoot);
                SplitResult split = TileSplitter.split(source);

                ManagedImage managed = new ManagedImage(imageId, split, cfg.shadow());
                managed.forceState(PipelineState.SPLITTING);
                registry.put(imageId, managed);

                skinUploader.uploadAll(managed).thenRun(() -> {
                    if (managed.isCancelled()) return;

                    if (!managed.transition(PipelineState.UPLOADING, PipelineState.ASSEMBLING)) return;

                    Component grid = HeadComponentFactory.buildGrid(managed);
                    managed.renderedComponent(grid);
                    managed.forceState(PipelineState.READY);

                    textureStore.persistAsync();
                });
            } catch (Exception e) {
                logger.log(Level.WARNING, "Could not process image '" + imageId + "'", e);
            }
        }
    }

    private void cancelAllInFlight() {
        registry.values().forEach(ManagedImage::cancel);
    }

    public Map<String, ManagedImage> images() {
        return Collections.unmodifiableMap(registry);
    }

    public Optional<Component> buildMotd() {
        if (motdRenderer == null) return Optional.empty();

        MotdEntry motd = configProvider.settings().pickRandomMotd();
        if (motd == null) return Optional.empty();

        boolean imagesReady = isImageReadyForMotd(motd);
        Component rendered = motdRenderer.render(motd, imagesReady);

        if (rendered.equals(Component.empty())) return Optional.empty();
        return Optional.of(rendered);
    }

    private boolean isImageReadyForMotd(MotdEntry motd) {
        if (motd.referencesImage()) {
            ManagedImage ref = registry.get(motd.imageRef());
            return ref != null && ref.isDisplayable();
        }
        return registry.values().stream().anyMatch(ManagedImage::isDisplayable);
    }

}
