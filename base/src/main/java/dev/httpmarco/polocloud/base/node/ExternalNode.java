package dev.httpmarco.polocloud.base.node;

import java.util.UUID;

public final class ExternalNode implements Node {

    @Override
    public UUID id() {
        return null;
    }

    @Override
    public String name() {
        return "";
    }
}
