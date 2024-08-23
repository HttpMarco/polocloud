package dev.httpmarco.polocloud.node.terminal.setup.impl;

import dev.httpmarco.polocloud.api.packet.resources.group.GroupCreatePacket;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.groups.ClusterGroupFallbackImpl;
import dev.httpmarco.polocloud.node.groups.ClusterGroupImpl;
import dev.httpmarco.polocloud.node.platforms.Platform;
import dev.httpmarco.polocloud.node.platforms.PlatformVersion;
import dev.httpmarco.polocloud.node.terminal.setup.Setup;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Slf4j
public final class GroupSetup extends Setup {

    public GroupSetup() {
        super("Group-Setup");

        // todo custom error messages

        question("name", "What is the name of the group&8?", s -> !Node.instance().groupProvider().exists(s.first()));

        question("platform", "Which platform do you like to use&8?",
                it -> Arrays.stream(Node.instance().platformService().platforms()).map(Platform::platform).toList(),
                rawInput -> Node.instance().platformService().exists(rawInput.first()));

        question("version", "Select a version&8?",
                it -> Node.instance().platformService().platform(it.get("platform"))
                        .versions()
                        .stream().map(PlatformVersion::version).toList(),
                context -> {
                    var platform = Node.instance().platformService().platform(context.second().get("platform"));
                    var proof = platform.versions().stream().anyMatch(it -> it.version().equals(context.first()));

                    // add the fallback question
                    if (proof && platform.type() == PlatformType.SERVER) {
                        question("fallback", "Is the given group a fallback group?", it -> List.of("false", "true"), it -> it.first().equalsIgnoreCase("true") || it.first().equalsIgnoreCase("false"));
                    }
                    return proof;
                });

        question("minMemory", "Select the value of the minimal memory of one service (mb)", it -> List.of("512", "1024", "256", "2048"), it -> isNumber(it.first()));
        question("maxMemory", "Select the value of the maximum memory of one service (mb)", it -> {
            var minMemory = Integer.parseInt(it.get("minMemory"));
            var results = new ArrayList<String>();
            if (minMemory <= 512) {
                results.add("512");
            }
            if (minMemory <= 1024) {
                results.add("1024");
            }
            if (minMemory <= 256) {
                results.add("256");
            }
            if (minMemory <= 2048) {
                results.add("2048");
            }
            return results;
        }, it -> isNumber(it.first()) && Integer.parseInt(it.second().get("minMemory")) <= Integer.parseInt(it.first()));

        question("staticService", "Is the service a static Service? (Static services are not be reset on a restart)", it -> List.of("true", "false"), it -> it.first().equalsIgnoreCase("true") || it.first().equalsIgnoreCase("false"));
        question("minOnlineServices", "How many service should be minimal online?", it -> List.of(), it -> isNumber(it.first()));
        question("maxOnlineServices", "How many service should be maximal online?", it -> List.of(), it -> isNumber(it.first()));
    }

    public boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void complete(@NotNull Map<String, String> context) {
        var name = context.get("name");
        var platform = Node.instance().platformService().platform(context.get("platform"));
        var version = platform.versions().stream().filter(it -> it.version().equalsIgnoreCase(context.get("version"))).findFirst().orElseThrow();
        var minMemory = Integer.parseInt(context.get("minMemory"));
        var maxMemory = Integer.parseInt(context.get("maxMemory"));
        var staticService = Boolean.parseBoolean(context.get("staticService"));
        var minOnlineServices = Integer.parseInt(context.get("minOnlineServices"));
        var maxOnlineServices = Integer.parseInt(context.get("maxOnlineServices"));
        var fallbackGroup = context.containsKey("fallback") && (Boolean.parseBoolean(context.get("fallback")));

        Node.instance().clusterProvider().broadcastAll(new GroupCreatePacket(name,
                new String[]{"every", platform.type().defaultTemplateSpace(), name},
                new String[]{Node.instance().clusterProvider().localNode().data().name()},
                new PlatformGroupDisplay(platform.platform(), version.version(), platform.type()),
                minMemory,
                maxMemory,
                staticService,
                minOnlineServices,
                maxOnlineServices,
                fallbackGroup));

        log.info("You successfully created the group &8'&f{}&8'", name);
    }
}
