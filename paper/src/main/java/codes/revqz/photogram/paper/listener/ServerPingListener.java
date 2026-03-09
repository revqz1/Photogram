package codes.revqz.photogram.paper.listener;

import codes.revqz.photogram.platform.ServerPlatform;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServerPingListener implements Listener {

    private final ServerPlatform platform;

    @Inject
    public ServerPingListener(ServerPlatform platform) {
        this.platform = platform;
    }

    @EventHandler
    public void onPing(PaperServerListPingEvent event) {
        // pass client protocol version so old clients get a plain-text fallback
        int protocol = event.getProtocolVersion();
        platform.motd(protocol).ifPresent(event::motd);
    }
}
