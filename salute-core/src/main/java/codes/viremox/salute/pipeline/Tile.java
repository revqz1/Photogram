package codes.viremox.salute.pipeline;

import java.awt.image.BufferedImage;

public record Tile(int column, int row, BufferedImage pixels, String fingerprint) {
}
