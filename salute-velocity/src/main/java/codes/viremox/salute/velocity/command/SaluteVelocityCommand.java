package codes.viremox.salute.velocity.command;

import codes.viremox.salute.command.SaluteCommandHandler;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;

@CommandAlias("salute")
public class SaluteVelocityCommand extends BaseCommand {

    private final SaluteCommandHandler handler;

    @Inject
    public SaluteVelocityCommand(SaluteCommandHandler handler) {
        this.handler = handler;
    }

    @Default
    public void onDefault(CommandSource sender) {
        handler.info(sender);
    }

    @Subcommand("reload")
    @CommandPermission("salute.admin")
    public void onReload(CommandSource sender) {
        handler.reload(sender);
    }

    @Subcommand("view")
    @CommandPermission("salute.admin")
    @CommandCompletion("@images")
    public void onView(CommandSource sender, String name) {
        handler.view(sender, name);
    }
}
