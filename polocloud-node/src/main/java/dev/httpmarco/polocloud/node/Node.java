package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.node.terminal.Terminal;
import dev.httpmarco.polocloud.node.terminal.impl.JLineTerminalImpl;
import dev.httpmarco.polocloud.node.i18n.I18n;
import dev.httpmarco.polocloud.node.i18n.impl.I18nPoloCloudNode;
import dev.httpmarco.polocloud.node.terminal.setup.impl.StartSetup;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Accessors(fluent = true)
public final class Node {

    @Getter
    private static Node instance;
    @Getter
    private static final I18n translation = new I18nPoloCloudNode();

    private final Terminal terminal;

    public Node() {
        instance = this;

        this.terminal = new JLineTerminalImpl();

        log.info(translation().get("node.starting"));

        //todo
        //this is only temporary for testing
        new StartSetup().run();
    }
}
