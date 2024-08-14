package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PlayerCommand extends Command {

    public PlayerCommand() {
        super("players", "Manage and get information about every player", "player");

        syntax(commandContext -> {
            var playerProvider = Node.instance().playerProvider();
            log.info("Following &b{} &7players are online&8:", playerProvider.players().size());
            playerProvider.players().forEach(player -> log.info("&8- &f{}&8: (&7{}&8)", player.name(), player.details()));
        }, CommandArgumentType.Keyword("list"));

        var playerArgument = CommandArgumentType.Player("player");

        syntax(commandContext -> {
            var player = commandContext.arg(playerArgument);
            log.info("Username&8: &b{}", player.name());
            log.info("UniqueId&8: &b{}", player.uniqueId());
            log.info("Current proxy&8: &b{}", player.currentProxyName());
            log.info("Current server&8: &b{}", player.currentServerName());
        }, playerArgument, CommandArgumentType.Keyword("info"));
    }
}
