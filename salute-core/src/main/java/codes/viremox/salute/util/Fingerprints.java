package codes.viremox.salute.util;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class Fingerprints {

    private static final MessageDigest SHA_256;

    static {
        try {
            SHA_256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Fingerprints() {}

    public static String ofImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] rgb = image.getRGB(0, 0, width, height, null, 0, width);

        ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * (2 + rgb.length));
        buf.putInt(width);
        buf.putInt(height);
        for (int pixel : rgb) {
            buf.putInt(pixel);
        }

        byte[] digest;
        synchronized (SHA_256) {
            SHA_256.reset();
            digest = SHA_256.digest(buf.array());
        }
        return HexFormat.of().formatHex(digest);
    }
}
