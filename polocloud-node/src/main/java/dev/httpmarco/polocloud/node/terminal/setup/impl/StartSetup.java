package dev.httpmarco.polocloud.node.terminal.setup.impl;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.terminal.setup.Setup;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;

@Log4j2
public final class StartSetup extends Setup {

    private static final List<String> YES_NO_OPTIONS = List.of(Node.translation().get("setup.option.yes"), Node.translation().get("setup.option.no"));

    public StartSetup() {
        super("Start-Setup");

        question("question-auto-update",
                Node.translation().get("setup.question.auto-update"),
                stringStringMap -> YES_NO_OPTIONS,
                pair -> YES_NO_OPTIONS.contains(pair.first().toLowerCase()));
    }

    @Override
    public void complete(Map<String, String> context) {
        log.info("Auto-Update: {}", context.get("question-auto-update"));
    }
}
