package dev.httpmarco.polocloud.suite.platforms.setup;

import dev.httpmarco.polocloud.suite.terminal.setup.Setup;

import java.util.Map;

public class PlatformSetup extends Setup {

    public PlatformSetup() {
        super("custom-platform");

        question("name", "What is the name of the platform?");
    }

    @Override
    public void complete(Map<String, String> context) {

    }
}
