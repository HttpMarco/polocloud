package dev.httpmarco.polocloud.base.node;

import java.util.UUID;

public final class LocalNode implements Node {
    @Override
    public UUID id() {
        return null;
    }

    @Override
    public String name() {
        return "";
    }
}
