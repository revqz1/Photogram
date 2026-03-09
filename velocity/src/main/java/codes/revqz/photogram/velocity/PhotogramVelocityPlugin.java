package codes.revqz.photogram.velocity;

import codes.revqz.photogram.PhotogramOrchestrator;
import codes.revqz.photogram.command.PhotogramCommandHandler;
import codes.revqz.photogram.guice.PhotogramCoreModule;
import codes.revqz.photogram.platform.ServerPlatform;
import codes.revqz.photogram.velocity.command.PhotogramVelocityCommand;
import codes.revqz.photogram.velocity.guice.PhotogramVelocityModule;
import codes.revqz.photogram.velocity.listener.ProxyPingListener;
import co.aikar.commands.VelocityCommandManager;
import org.bstats.velocity.Metrics;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

@Plugin(id = "photogram")
public class PhotogramVelocityPlugin implements ServerPlatform {

    private final ProxyServer proxy;
    private final Path dataDir;
    private final Metrics.Factory metricsFactory;
    private PhotogramOrchestrator orchestrator;
    private VelocityCommandManager commandManager;

    @Inject
    public PhotogramVelocityPlugin(ProxyServer proxy, @DataDirectory Path dataDir, Metrics.Factory metricsFactory) {
        this.proxy = proxy;
        this.dataDir = dataDir;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        metricsFactory.make(this, 29943);

        Logger logger = Logger.getLogger("Photogram");

        final Injector injector = Guice.createInjector(
                new PhotogramCoreModule(dataDir, logger),
                new PhotogramVelocityModule(this, proxy));

        orchestrator = injector.getInstance(PhotogramOrchestrator.class);
        orchestrator.start();

        proxy.getEventManager().register(this, injector.getInstance(ProxyPingListener.class));

        commandManager = new VelocityCommandManager(proxy, this);
        PhotogramCommandHandler handler = injector.getInstance(PhotogramCommandHandler.class);
        commandManager.getCommandCompletions().registerAsyncCompletion(
                "images", ctx -> handler.imageNames());
        commandManager.registerCommand(injector.getInstance(PhotogramVelocityCommand.class));
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        if (orchestrator != null)
            orchestrator.shutdown();
        if (commandManager != null)
            commandManager.unregisterCommands();
    }

    @Override
    public Optional<Component> motd() {
        if (orchestrator == null)
            return Optional.empty();
        return orchestrator.buildMotd();
    }

    @Override
    public Optional<Component> motd(int clientProtocol) {
        if (orchestrator == null)
            return Optional.empty();
        return orchestrator.buildMotd(clientProtocol);
    }
}
