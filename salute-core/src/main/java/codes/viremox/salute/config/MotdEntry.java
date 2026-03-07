package codes.viremox.salute.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@ConfigSerializable
public class MotdEntry {

    @Setting("description")
    private List<String> lines = null;

    @Setting("fallback_description")
    private List<String> fallbackLines = List.of();

    @Setting("image")
    private String imageRef = null;

    public List<String> lines() {
        return lines;
    }

    public List<String> fallbackLines() {
        return fallbackLines;
    }

    public String imageRef() {
        return imageRef;
    }

    public boolean hasExplicitDescription() {
        return lines != null && !lines.isEmpty();
    }

    public boolean hasFallback() {
        return fallbackLines != null && !fallbackLines.isEmpty();
    }

    public boolean referencesImage() {
        return imageRef != null && !imageRef.isBlank();
    }
}
