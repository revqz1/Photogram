package codes.viremox.salute.util;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public final class Retry {

    private Retry() {}

    public static <T> CompletableFuture<T> withBackoff(
            Supplier<CompletableFuture<T>> action,
            int maxAttempts,
            Duration initialDelay
    ) {
        return attempt(action, 1, maxAttempts, initialDelay.toMillis());
    }

    private static <T> CompletableFuture<T> attempt(
            Supplier<CompletableFuture<T>> action,
            int current,
            int max,
            long delayMs
    ) {
        return action.get().exceptionallyCompose(failure -> {
            if (current >= max) {
                return CompletableFuture.failedFuture(failure);
            }
            CompletableFuture<T> delayed = new CompletableFuture<>();
            CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS)
                    .execute(() -> attempt(action, current + 1, max, delayMs * 2)
                            .whenComplete((result, ex) -> {
                                if (ex != null) delayed.completeExceptionally(ex);
                                else delayed.complete(result);
                            }));
            return delayed;
        });
    }
}
