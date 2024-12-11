package dev.httpmarco.polocloud.node.terminal.setup.common;

import dev.httpmarco.polocloud.node.terminal.setup.Setup;
import dev.httpmarco.polocloud.node.terminal.setup.SetupStep;

import java.util.Set;

public abstract class AbstractSetup<T> implements Setup {

    public abstract void bindQuestion();

    @Override
    public SetupStep current() {
        return null;
    }

    @Override
    public void next() {

    }

    @Override
    public void previous() {

    }

    public T run() {
        return null;
    }
}
