package dev.httpmarco.polocloud.node.commands.specific;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.players.ClusterPlayer;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.CommandArgument;
import dev.httpmarco.polocloud.node.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PlayerArgument extends CommandArgument<ClusterPlayer> {

    public PlayerArgument(String key) {
        super(key);
    }

    @Override
    public List<String> defaultArgs(CommandContext context) {
        var list = new ArrayList<String>();
        Node.instance().playerProvider().players().forEach(clusterPlayer -> {
            list.add(clusterPlayer.name());
            list.add(clusterPlayer.uniqueId().toString());
        });
        return list;
    }

    @Override
    public boolean predication(@NotNull String rawInput) {
        try {
            return CloudAPI.instance().playerProvider().online(UUID.fromString(rawInput));
        } catch (IllegalArgumentException exception) {
            return CloudAPI.instance().playerProvider().online(rawInput);
        }
    }

    @Override
    public ClusterPlayer buildResult(String input) {
        try {
            return CloudAPI.instance().playerProvider().find(UUID.fromString(input));
        } catch (IllegalArgumentException exception) {
            return CloudAPI.instance().playerProvider().find(input);
        }
    }
}
