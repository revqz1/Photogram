package codes.revqz.photogram.velocity.listener;

import codes.revqz.photogram.platform.ServerPlatform;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;

public class ProxyPingListener {

    private final ServerPlatform platform;

    @Inject
    public ProxyPingListener(ServerPlatform platform) {
        this.platform = platform;
    }

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        // pass client protocol version so old clients get a plain-text fallback
        int protocol = event.getConnection().getProtocolVersion().getProtocol();
        platform.motd(protocol).ifPresent(motd -> {
            ServerPing updated = event.getPing().asBuilder()
                    .description(motd)
                    .build();
            event.setPing(updated);
        });
    }
}
