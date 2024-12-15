package dev.httpmarco.polocloud.node.terminal.setup.impl.defaults;

import dev.httpmarco.polocloud.node.terminal.setup.common.AbstractSetup;
import dev.httpmarco.polocloud.node.terminal.setup.impl.SelectQuestionImpl;

public final class StartSetup extends AbstractSetup {

    @Override
    public void bindQuestion() {
        add(new SelectQuestionImpl("Do you want to start the setup?", "Yes", "No"));

        this.start();
    }
}
