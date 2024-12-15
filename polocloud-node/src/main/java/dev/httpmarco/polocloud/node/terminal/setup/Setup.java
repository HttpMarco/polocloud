package dev.httpmarco.polocloud.node.terminal.setup;

import dev.httpmarco.polocloud.api.Closeable;

public interface Setup extends Closeable {

    SetupStep current();

    void next();

    void previous();

    void start();

}
