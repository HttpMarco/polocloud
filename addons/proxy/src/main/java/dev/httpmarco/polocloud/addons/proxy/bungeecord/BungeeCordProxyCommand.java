package dev.httpmarco.polocloud.addons.proxy.bungeecord;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.Contract;

public final class BungeeCordProxyCommand extends Command {

    public BungeeCordProxyCommand() {
        super("proxy");
    }

    @Contract(pure = true)
    @Override
    public void execute(CommandSender sender, String[] args) {


        if(args[0].equalsIgnoreCase("maintenance")) {

            return;
        }
    }
}
