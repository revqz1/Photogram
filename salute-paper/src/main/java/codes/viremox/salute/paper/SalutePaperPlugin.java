package codes.viremox.salute.paper;

import codes.viremox.salute.SaluteOrchestrator;
import org.bstats.bukkit.Metrics;
import codes.viremox.salute.command.SaluteCommandHandler;
import codes.viremox.salute.guice.SaluteCoreModule;
import codes.viremox.salute.paper.command.SaluteCommand;
import codes.viremox.salute.paper.guice.SalutePaperModule;
import codes.viremox.salute.paper.listener.ServerPingListener;
import codes.viremox.salute.platform.ServerPlatform;
import co.aikar.commands.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class SalutePaperPlugin extends JavaPlugin implements ServerPlatform {

    private Injector injector;
    private SaluteOrchestrator orchestrator;
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        new Metrics(this, 29942);

        injector = Guice.createInjector(
                new SaluteCoreModule(getDataFolder().toPath(), getLogger()),
                new SalutePaperModule(this)
        );

        orchestrator = injector.getInstance(SaluteOrchestrator.class);
        orchestrator.start();

        getServer().getPluginManager().registerEvents(
                injector.getInstance(ServerPingListener.class), this);
        setupCommands();
    }

    @Override
    public void onDisable() {
        if (orchestrator != null) orchestrator.shutdown();
        if (commandManager != null) commandManager.unregisterCommands();
    }

    private void setupCommands() {
        SaluteCommandHandler handler = injector.getInstance(SaluteCommandHandler.class);
        commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerAsyncCompletion(
                "images", ctx -> handler.imageNames());
        commandManager.registerCommand(injector.getInstance(SaluteCommand.class));
    }

    @Override
    public Optional<Component> motd() {
        if (orchestrator == null) return Optional.empty();
        return orchestrator.buildMotd();
    }
}
