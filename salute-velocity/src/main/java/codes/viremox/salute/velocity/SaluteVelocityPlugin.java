package codes.viremox.salute.velocity;

import codes.viremox.salute.SaluteOrchestrator;
import codes.viremox.salute.command.SaluteCommandHandler;
import codes.viremox.salute.guice.SaluteCoreModule;
import codes.viremox.salute.platform.ServerPlatform;
import codes.viremox.salute.velocity.command.SaluteVelocityCommand;
import codes.viremox.salute.velocity.guice.SaluteVelocityModule;
import codes.viremox.salute.velocity.listener.ProxyPingListener;
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

@Plugin(id = "salute")
public class SaluteVelocityPlugin implements ServerPlatform {

    private final ProxyServer proxy;
    private final Path dataDir;
    private final Metrics.Factory metricsFactory;
    private SaluteOrchestrator orchestrator;
    private VelocityCommandManager commandManager;

    @Inject
    public SaluteVelocityPlugin(ProxyServer proxy, @DataDirectory Path dataDir, Metrics.Factory metricsFactory) {
        this.proxy = proxy;
        this.dataDir = dataDir;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        metricsFactory.make(this, 29943);

        Logger logger = Logger.getLogger("Salute");

        final Injector injector = Guice.createInjector(
                new SaluteCoreModule(dataDir, logger),
                new SaluteVelocityModule(this, proxy)
        );

        orchestrator = injector.getInstance(SaluteOrchestrator.class);
        orchestrator.start();

        proxy.getEventManager().register(this, injector.getInstance(ProxyPingListener.class));

        commandManager = new VelocityCommandManager(proxy, this);
        SaluteCommandHandler handler = injector.getInstance(SaluteCommandHandler.class);
        commandManager.getCommandCompletions().registerAsyncCompletion(
                "images", ctx -> handler.imageNames());
        commandManager.registerCommand(injector.getInstance(SaluteVelocityCommand.class));
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        if (orchestrator != null) orchestrator.shutdown();
        if (commandManager != null) commandManager.unregisterCommands();
    }

    @Override
    public Optional<Component> motd() {
        if (orchestrator == null) return Optional.empty();
        return orchestrator.buildMotd();
    }
}
