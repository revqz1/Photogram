package codes.revqz.photogram.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class ApiSettings {

    @Setting("mineskin-key")
    private String mineSkinKey = "";

    // minimum protocol version for the image motd (default: 771 = 1.21.9)
    @Setting("min-protocol-version")
    private int minProtocolVersion = 771;

    public String mineSkinKey() {
        return mineSkinKey;
    }

    public int minProtocolVersion() {
        return minProtocolVersion;
    }

}
