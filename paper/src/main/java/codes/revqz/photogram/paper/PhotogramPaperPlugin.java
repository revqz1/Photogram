package codes.revqz.photogram.paper;

import codes.revqz.photogram.PhotogramOrchestrator;
import org.bstats.bukkit.Metrics;
import codes.revqz.photogram.command.PhotogramCommandHandler;
import codes.revqz.photogram.guice.PhotogramCoreModule;
import codes.revqz.photogram.paper.command.PhotogramCommand;
import codes.revqz.photogram.paper.guice.PhotogramPaperModule;
import codes.revqz.photogram.paper.listener.ServerPingListener;
import codes.revqz.photogram.platform.ServerPlatform;
import co.aikar.commands.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class PhotogramPaperPlugin extends JavaPlugin implements ServerPlatform {

    private Injector injector;
    private PhotogramOrchestrator orchestrator;
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        new Metrics(this, 29942);

        injector = Guice.createInjector(
                new PhotogramCoreModule(getDataFolder().toPath(), getLogger()),
                new PhotogramPaperModule(this));

        orchestrator = injector.getInstance(PhotogramOrchestrator.class);
        orchestrator.start();

        getServer().getPluginManager().registerEvents(
                injector.getInstance(ServerPingListener.class), this);
        setupCommands();
    }

    @Override
    public void onDisable() {
        if (orchestrator != null)
            orchestrator.shutdown();
        if (commandManager != null)
            commandManager.unregisterCommands();
    }

    private void setupCommands() {
        PhotogramCommandHandler handler = injector.getInstance(PhotogramCommandHandler.class);
        commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerAsyncCompletion(
                "images", ctx -> handler.imageNames());
        commandManager.registerCommand(injector.getInstance(PhotogramCommand.class));
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
