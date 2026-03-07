package codes.viremox.salute.config;

import net.kyori.adventure.text.format.ShadowColor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ConfigSerializable
public class ImageEntry {

    @Setting("path")
    private String filePath = "";

    @Setting("shadow_color")
    private ShadowColor shadow = null;

    public String filePath() {
        return filePath;
    }

    public ShadowColor shadow() {
        return shadow;
    }

    public BufferedImage readFromDisk(Path imageRoot) throws IOException {
        Path resolved = imageRoot.resolve(filePath);
        if (!Files.isRegularFile(resolved)) {
            throw new IOException("Image file does not exist: " + resolved);
        }
        BufferedImage img = ImageIO.read(resolved.toFile());
        if (img == null) {
            throw new IOException("Unrecognized image format: " + resolved);
        }
        return img;
    }
}
