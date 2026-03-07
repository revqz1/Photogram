package codes.viremox.salute.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TextureStore {

    private static final String STORE_FILE = "texture-cache.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<HashMap<String, String>>() {}.getType();

    private final Path storePath;
    private final Logger logger;
    private final ConcurrentHashMap<String, String> entries = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock fileLock = new ReentrantReadWriteLock();

    @Inject
    public TextureStore(@Named("dataDirectory") Path dataDirectory, @Named("salute") Logger logger) {
        this.storePath = dataDirectory.resolve(STORE_FILE);
        this.logger = logger;
        load();
    }

    public String lookup(String fingerprint) {
        return entries.get(fingerprint);
    }

    public void store(String fingerprint, String textureValue) {
        entries.put(fingerprint, textureValue);
    }

    public void load() {
        if (!Files.exists(storePath)) return;

        fileLock.writeLock().lock();
        try (Reader reader = Files.newBufferedReader(storePath, StandardCharsets.UTF_8)) {
            Map<String, String> loaded = GSON.fromJson(reader, MAP_TYPE);
            if (loaded != null) {
                entries.putAll(loaded);
                logger.info("Loaded " + entries.size() + " cached textures from " + STORE_FILE);
            }
        } catch (IOException | com.google.gson.JsonSyntaxException e) {
            logger.log(Level.WARNING, "Could not read texture cache; starting fresh", e);
        } finally {
            fileLock.writeLock().unlock();
        }
    }

    public void persist() {
        fileLock.writeLock().lock();
        try {
            Files.createDirectories(storePath.getParent());
            try (Writer writer = Files.newBufferedWriter(storePath, StandardCharsets.UTF_8)) {
                GSON.toJson(new HashMap<>(entries), MAP_TYPE, writer);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to persist texture cache", e);
        } finally {
            fileLock.writeLock().unlock();
        }
    }

    public void persistAsync() {
        Thread.ofVirtual().name("salute-cache-persist").start(this::persist);
    }
}
