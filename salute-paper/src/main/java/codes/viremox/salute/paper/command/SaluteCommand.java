package codes.viremox.salute.paper.command;

import codes.viremox.salute.command.SaluteCommandHandler;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;

@CommandAlias("salute")
public class SaluteCommand extends BaseCommand {

    private final SaluteCommandHandler handler;

    @Inject
    public SaluteCommand(SaluteCommandHandler handler) {
        this.handler = handler;
    }

    @Default
    public void onDefault(CommandSender sender) {
        handler.info(sender);
    }

    @Subcommand("reload")
    @CommandPermission("salute.admin")
    public void onReload(CommandSender sender) {
        handler.reload(sender);
    }

    @Subcommand("view")
    @CommandPermission("salute.admin")
    @CommandCompletion("@images")
    public void onView(CommandSender sender, String name) {
        handler.view(sender, name);
    }
}
