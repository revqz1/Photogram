package codes.viremox.salute.platform;

import net.kyori.adventure.text.Component;

import java.util.Optional;

public interface ServerPlatform {

    Optional<Component> motd();
}
