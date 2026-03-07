package codes.viremox.salute.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class ApiSettings {

    @Setting("mineskin-key")
    private String mineSkinKey = "";

    public String mineSkinKey() {
        return mineSkinKey;
    }

}
