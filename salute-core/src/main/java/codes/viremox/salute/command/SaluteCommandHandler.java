package codes.viremox.salute.command;

import codes.viremox.salute.SaluteOrchestrator;
import codes.viremox.salute.pipeline.ManagedImage;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

@Singleton
public final class SaluteCommandHandler {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final String TAG = "<gold>[<yellow>Salute<gold>]</gold> ";
    private static final String VERSION = loadVersion();

    private final SaluteOrchestrator orchestrator;

    @Inject
    public SaluteCommandHandler(SaluteOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    public void reload(Audience sender) {
        orchestrator.reload();
        sender.sendMessage(MM.deserialize(TAG + "<green>Reloaded."));
    }

    public void view(Audience sender, String name) {
        ManagedImage img = orchestrator.images().get(name);
        if (img == null) {
            sender.sendMessage(MM.deserialize(TAG + "<red>Unknown image: <white>" + name));
            return;
        }

        sender.sendMessage(MM.deserialize(
                TAG + "<yellow>" + img.id()
                + " <gray>| <white>" + img.state().name()
                + " <gray>(" + img.completedUploads() + "/" + img.totalTiles() + ")"
        ));

        Component rendered = img.renderedComponent();
        if (rendered != null) {
            sender.sendMessage(rendered);
        }
    }

    public void info(Audience sender) {
        sender.sendMessage(MM.deserialize(
                "<gray>Running <gold><click:open_url:'https://github.com/Viremox/Salute'><hover:show_text:'<gray>Open on GitHub'>Salute</hover></click></gold> "
                + "<yellow>v" + VERSION + " <gray>made by "
                + "<gold><click:open_url:'https://viremox.codes'><hover:show_text:'<gray>Visit website'>Viremox</hover></click></gold>"
        ));
    }

    public Set<String> imageNames() {
        return orchestrator.images().keySet();
    }

    private static String loadVersion() {
        try (InputStream in = SaluteCommandHandler.class.getClassLoader().getResourceAsStream("salute.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                return props.getProperty("version", "unknown");
            }
        } catch (IOException ignored) {}
        return "unknown";
    }
}
