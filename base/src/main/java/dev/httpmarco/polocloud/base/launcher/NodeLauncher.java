package dev.httpmarco.polocloud.base.launcher;

import dev.httpmarco.polocloud.base.CloudBase;

public final class NodeLauncher {

    public static void main(String[] args) {
        System.setProperty("startup", String.valueOf(System.currentTimeMillis()));

        new CloudBase();
    }

}
