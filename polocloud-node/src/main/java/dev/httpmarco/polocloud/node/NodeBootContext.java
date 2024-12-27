package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.inject.Injector;

public final class NodeBootContext {

    public static void main(String[] args) {
        var injector = Injector.newInjector();

        injector.instance(Node.class);
    }
}
