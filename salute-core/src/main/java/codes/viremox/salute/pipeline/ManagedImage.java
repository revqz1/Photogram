package codes.viremox.salute.pipeline;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.ShadowColor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ManagedImage {

    private final String id;
    private final SplitResult splitResult;
    private final ShadowColor shadow;

    private final AtomicReference<PipelineState> state = new AtomicReference<>(PipelineState.IDLE);
    private final AtomicInteger uploadsFinished = new AtomicInteger(0);
    private final Map<String, String> resolvedTextures = new ConcurrentHashMap<>();
    private volatile Component renderedComponent;

    public ManagedImage(String id, SplitResult splitResult, ShadowColor shadow) {
        this.id = id;
        this.splitResult = splitResult;
        this.shadow = shadow;
    }

    public String id() {
        return id;
    }

    public SplitResult splitResult() {
        return splitResult;
    }

    public ShadowColor shadow() {
        return shadow;
    }

    public PipelineState state() {
        return state.get();
    }

    public boolean transition(PipelineState expected, PipelineState next) {
        return state.compareAndSet(expected, next);
    }

    public void forceState(PipelineState next) {
        state.set(next);
    }

    public void recordTexture(String fingerprint, String textureValue) {
        resolvedTextures.put(fingerprint, textureValue);
        uploadsFinished.incrementAndGet();
    }

    public String textureFor(String fingerprint) {
        return resolvedTextures.get(fingerprint);
    }

    public int completedUploads() {
        return uploadsFinished.get();
    }

    public int totalTiles() {
        return splitResult.tileCount();
    }

    public boolean allUploaded() {
        return uploadsFinished.get() >= splitResult.tileCount();
    }

    public void renderedComponent(Component component) {
        this.renderedComponent = component;
    }

    public Component renderedComponent() {
        return renderedComponent;
    }

    public boolean isDisplayable() {
        return state.get() == PipelineState.READY && renderedComponent != null;
    }

    public void cancel() {
        state.set(PipelineState.CANCELLED);
    }

    public boolean isCancelled() {
        return state.get() == PipelineState.CANCELLED;
    }
}
