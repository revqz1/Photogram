package codes.viremox.salute.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public final class Assets {

    private static final String[] DEFAULT_FILES = {"config.yml"};

    private Assets() {}

    public static void extractDefaults(Path dataFolder) {
        try {
            Files.createDirectories(dataFolder);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create data folder: " + dataFolder, e);
        }

        ClassLoader loader = Assets.class.getClassLoader();
        for (String fileName : DEFAULT_FILES) {
            Path target = dataFolder.resolve(fileName);
            if (Files.exists(target)) continue;

            try (InputStream in = loader.getResourceAsStream("defaults/" + fileName)) {
                if (in == null) continue;
                Files.createDirectories(target.getParent());
                Files.copy(in, target);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to extract default asset: " + fileName, e);
            }
        }
    }
}
