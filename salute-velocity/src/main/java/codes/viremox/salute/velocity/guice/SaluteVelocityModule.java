package codes.viremox.salute.velocity.guice;

import codes.viremox.salute.platform.ServerPlatform;
import codes.viremox.salute.velocity.SaluteVelocityPlugin;
import com.google.inject.AbstractModule;
import com.velocitypowered.api.proxy.ProxyServer;

public class SaluteVelocityModule extends AbstractModule {

    private final SaluteVelocityPlugin plugin;
    private final ProxyServer proxy;

    public SaluteVelocityModule(SaluteVelocityPlugin plugin, ProxyServer proxy) {
        this.plugin = plugin;
        this.proxy = proxy;
    }

    @Override
    protected void configure() {
        bind(SaluteVelocityPlugin.class).toInstance(plugin);
        bind(ServerPlatform.class).toInstance(plugin);
        bind(ProxyServer.class).toInstance(proxy);
    }
}
