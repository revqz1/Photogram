package codes.viremox.salute.pipeline;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;

public final class HeadComponentFactory {

    private HeadComponentFactory() {}

    public static Component buildGrid(ManagedImage image) {
        SplitResult split = image.splitResult();
        ShadowColor shadow = image.shadow();

        Component grid = Component.empty();

        for (int row = 0; row < split.rows(); row++) {
            if (row > 0) {
                grid = grid.append(Component.newline());
            }
            for (int col = 0; col < split.columns(); col++) {
                Tile tile = split.tileAt(col, row);
                if (tile == null) continue;

                String texture = image.textureFor(tile.fingerprint());
                if (texture == null) continue;

                Component head = createHead(texture, shadow);
                grid = grid.append(head);
            }
        }

        return grid;
    }

    private static Component createHead(String textureValue, ShadowColor shadow) {
        PlayerHeadObjectContents contents = ObjectContents.playerHead()
                .profileProperty(PlayerHeadObjectContents.property("textures", textureValue))
                .build();

        Component head = Component.object(contents);

        if (shadow != null) {
            return head.style(Style.style().shadowColor(shadow).build());
        }
        return head.color(NamedTextColor.WHITE);
    }
}
