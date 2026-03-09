package codes.revqz.photogram.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@ConfigSerializable
public class MotdEntry {

    @Setting("description")
    private List<String> lines = null;

    @Setting("fallback_description")
    private List<String> fallbackLines = List.of();

    // shown to clients below min-protocol-version
    @Setting("version_fallback_description")
    private List<String> versionFallbackLines = List.of();

    @Setting("image")
    private String imageRef = null;

    public List<String> lines() {
        return lines;
    }

    public List<String> fallbackLines() {
        return fallbackLines;
    }

    public List<String> versionFallbackLines() {
        return versionFallbackLines;
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

    public boolean hasVersionFallback() {
        return versionFallbackLines != null && !versionFallbackLines.isEmpty();
    }

    public boolean referencesImage() {
        return imageRef != null && !imageRef.isBlank();
    }
}
