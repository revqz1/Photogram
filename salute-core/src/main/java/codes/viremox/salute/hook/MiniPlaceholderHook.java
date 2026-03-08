package codes.viremox.salute.hook;

import codes.viremox.salute.pipeline.ManagedImage;
import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;

import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

public final class MiniPlaceholderHook {

    private static final String REQUIRED_CLASS = "io.github.miniplaceholders.api.MiniPlaceholders";

    private final Expansion expansion;

    private MiniPlaceholderHook(Expansion expansion) {
        this.expansion = expansion;
    }

    public static MiniPlaceholderHook attemptRegistration(
            Supplier<Map<String, ManagedImage>> imageSource,
            Logger logger
    ) {
        try {
            Class.forName(REQUIRED_CLASS);
        } catch (ClassNotFoundException ignored) {
            return null;
        }

        Expansion expansion = Expansion.builder("salute")
                .globalPlaceholder("image", (queue, ctx) -> {
                    String name = queue.popOr("<salute_image> requires an image name").value();
                    ManagedImage image = imageSource.get().get(name);
                    if (image == null || !image.isDisplayable()) {
                        return Tag.selfClosingInserting(Component.empty());
                    }
                    return Tag.selfClosingInserting(image.renderedComponent());
                })
                .globalPlaceholder("ready", (queue, ctx) -> {
                    String name = queue.popOr("<salute_ready> requires an image name").value();
                    ManagedImage image = imageSource.get().get(name);
                    boolean displayable = image != null && image.isDisplayable();
                    return Tag.selfClosingInserting(Component.text(String.valueOf(displayable)));
                })
                .globalPlaceholder("state", (queue, ctx) -> {
                    String name = queue.popOr("<salute_state> requires an image name").value();
                    ManagedImage image = imageSource.get().get(name);
                    String label = image != null
                            ? image.state().name().toLowerCase()
                            : "unknown";
                    return Tag.selfClosingInserting(Component.text(label));
                })
                .build();

        expansion.register();
        logger.info("MiniPlaceholders expansion registered.");
        return new MiniPlaceholderHook(expansion);
    }

    public void unregister() {
        if (expansion.registered()) {
            expansion.unregister();
        }
    }
}
