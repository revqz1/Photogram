package codes.viremox.salute.display;

import codes.viremox.salute.config.MotdEntry;
import codes.viremox.salute.pipeline.ManagedImage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Map;
import java.util.function.Supplier;

public final class MotdRenderer {

    private final MiniMessage miniMessage;

    private MotdRenderer(MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }

    public static MotdRenderer create(Supplier<Map<String, ManagedImage>> imageSource) {
        TagResolver imageResolver = TagResolver.resolver("image", (queue, ctx) -> {
            String name = queue.popOr("<image> requires a name argument").value();
            Map<String, ManagedImage> images = imageSource.get();
            ManagedImage img = images.get(name);
            if (img == null || !img.isDisplayable()) {
                return Tag.selfClosingInserting(Component.empty());
            }
            return Tag.selfClosingInserting(img.renderedComponent());
        });

        MiniMessage mm = MiniMessage.builder()
                .tags(TagResolver.resolver(TagResolver.standard(), imageResolver))
                .build();

        return new MotdRenderer(mm);
    }

    public Component render(MotdEntry motd, boolean imagesReady) {
        if (motd == null) return Component.empty();

        if (motd.hasExplicitDescription()) {
            return miniMessage.deserialize(String.join("\n", motd.lines()));
        }

        if (motd.referencesImage()) {
            if (imagesReady) {
                return miniMessage.deserialize("<image:" + motd.imageRef() + ">");
            }
            if (motd.hasFallback()) {
                return miniMessage.deserialize(String.join("\n", motd.fallbackLines()));
            }
            return Component.empty();
        }

        if (motd.hasFallback()) {
            return miniMessage.deserialize(String.join("\n", motd.fallbackLines()));
        }

        return Component.empty();
    }

}
