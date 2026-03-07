package codes.viremox.salute.pipeline;

import codes.viremox.salute.util.Fingerprints;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class TileSplitter {

    public static final int FACE_SIZE = 8;
    public static final int SKIN_SIZE = 64;
    public static final int FACE_X = 8;
    public static final int FACE_Y = 8;

    private TileSplitter() {}

    public static SplitResult split(BufferedImage source) {
        if (source == null) throw new IllegalArgumentException("Source image must not be null");

        int cols = ceilDiv(source.getWidth(), FACE_SIZE);
        int rows = ceilDiv(source.getHeight(), FACE_SIZE);

        List<Tile> tiles = new ArrayList<>(cols * rows);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                BufferedImage skin = buildSkinForTile(source, c, r);
                String fp = Fingerprints.ofImage(skin);
                tiles.add(new Tile(c, r, skin, fp));
            }
        }

        return new SplitResult(List.copyOf(tiles), cols, rows);
    }

    private static BufferedImage buildSkinForTile(BufferedImage source, int col, int row) {
        int srcX = col * FACE_SIZE;
        int srcY = row * FACE_SIZE;
        int srcW = Math.min(FACE_SIZE, source.getWidth() - srcX);
        int srcH = Math.min(FACE_SIZE, source.getHeight() - srcY);

        BufferedImage skin = new BufferedImage(SKIN_SIZE, SKIN_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = skin.createGraphics();
        try {
            g.setComposite(AlphaComposite.Src);
            g.drawImage(source,
                    FACE_X, FACE_Y, FACE_X + srcW, FACE_Y + srcH,
                    srcX, srcY, srcX + srcW, srcY + srcH,
                    null);
        } finally {
            g.dispose();
        }
        return skin;
    }

    private static int ceilDiv(int numerator, int denominator) {
        return (numerator + denominator - 1) / denominator;
    }
}
