package codes.viremox.salute.paper.listener;

import codes.viremox.salute.platform.ServerPlatform;
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
        platform.motd().ifPresent(event::motd);
    }
}
