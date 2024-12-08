package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.node.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.node.dependencies.impl.DependencyProviderImpl;
import dev.httpmarco.polocloud.node.terminal.Terminal;
import dev.httpmarco.polocloud.node.terminal.impl.JLineTerminalImpl;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class Node {

    @Getter
    private static Node instance;

    private final DependencyProvider dependencyProvider;
    private final Terminal terminal;

    public Node() {
        instance = this;

        this.dependencyProvider = new DependencyProviderImpl();

        this.terminal = new JLineTerminalImpl();
    }
}
