package codes.revqz.photogram.platform;

import net.kyori.adventure.text.Component;

import java.util.Optional;

public interface ServerPlatform {

    // used when protocol version is unknown (e.g. legacy ping)
    Optional<Component> motd();

    // used by ping listeners that can supply the client's protocol version
    Optional<Component> motd(int clientProtocol);
}
