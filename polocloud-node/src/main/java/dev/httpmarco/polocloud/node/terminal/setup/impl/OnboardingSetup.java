package dev.httpmarco.polocloud.node.terminal.setup.impl;

import dev.httpmarco.polocloud.node.NodeConfig;
import dev.httpmarco.polocloud.node.terminal.setup.Setup;
import dev.httpmarco.polocloud.node.terminal.setup.translators.IntegerTranslator;

public class OnboardingSetup extends Setup<NodeConfig> {

    public OnboardingSetup() {
            question("Enter the amount of the node").ofSelection()
                    .defineType(new IntegerTranslator())
                    .items(1, 2, 3)
                    .ifAnswer(2, it -> false);



    }

    @Override
    public NodeConfig newInstance() {
        return new NodeConfig();
    }
}
