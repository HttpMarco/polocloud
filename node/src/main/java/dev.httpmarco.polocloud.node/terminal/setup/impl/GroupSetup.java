package dev.httpmarco.polocloud.node.terminal.setup.impl;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.platforms.Platform;
import dev.httpmarco.polocloud.node.platforms.PlatformVersion;
import dev.httpmarco.polocloud.node.terminal.setup.Setup;
import java.util.Arrays;

public final class GroupSetup extends Setup {

    public GroupSetup() {
        super("Group-Setup");

        question("name", "What is the name of the group&8?", s -> true);

        question("platform", "Which platform do you like to use&8?",
                it -> Arrays.stream(Node.instance().platformService().platforms()).map(Platform::platform).toList(),
                rawInput -> Node.instance().platformService().exists(rawInput.first()));

        question("platform", "Select a version&8?",
                it -> Node.instance().platformService().platform(it.get("platform"))
                        .versions()
                        .stream().map(PlatformVersion::version).toList(),
                context -> Node.instance().platformService().platform(context.second().get("platform"))
                        .versions()
                        .stream()
                        .anyMatch(it -> it.version().equals(context.first())));
    }
}
