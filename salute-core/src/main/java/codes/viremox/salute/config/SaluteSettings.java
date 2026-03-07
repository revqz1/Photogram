package codes.viremox.salute.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@ConfigSerializable
public class SaluteSettings {

    @Setting("motds")
    private Map<String, MotdEntry> motds = new LinkedHashMap<>();

    @Setting("images")
    private Map<String, ImageEntry> images = new LinkedHashMap<>();

    @Setting("settings")
    private ApiSettings api = new ApiSettings();

    public Map<String, MotdEntry> motds() {
        return motds;
    }

    public Map<String, ImageEntry> images() {
        return images;
    }

    public ApiSettings api() {
        return api;
    }

    public MotdEntry pickRandomMotd() {
        if (motds.isEmpty()) return null;
        List<MotdEntry> pool = List.copyOf(motds.values());
        return pool.get(ThreadLocalRandom.current().nextInt(pool.size()));
    }
}
