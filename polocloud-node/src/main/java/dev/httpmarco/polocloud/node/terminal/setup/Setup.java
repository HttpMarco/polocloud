package dev.httpmarco.polocloud.node.terminal.setup;

public interface Setup {

    SetupStep current();

    void next();

    void previous();

}
