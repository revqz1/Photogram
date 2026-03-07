package codes.viremox.salute.pipeline;

public enum PipelineState {
    IDLE,
    SPLITTING,
    UPLOADING,
    ASSEMBLING,
    READY,
    FAILED,
    CANCELLED
}
