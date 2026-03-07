package codes.viremox.salute.paper.guice;

import codes.viremox.salute.paper.SalutePaperPlugin;
import codes.viremox.salute.platform.ServerPlatform;
import com.google.inject.AbstractModule;
import org.bukkit.plugin.java.JavaPlugin;

public class SalutePaperModule extends AbstractModule {

    private final SalutePaperPlugin plugin;

    public SalutePaperModule(SalutePaperPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
        bind(SalutePaperPlugin.class).toInstance(plugin);
        bind(ServerPlatform.class).toInstance(plugin);
    }
}
