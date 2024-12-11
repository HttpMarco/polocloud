package dev.httpmarco.polocloud.node.terminal.setup.impl;

import dev.httpmarco.polocloud.node.terminal.setup.SelectQuestion;
import dev.httpmarco.polocloud.node.terminal.setup.common.AbstractQuestion;

public class SelectQuestionImpl extends AbstractQuestion implements SelectQuestion {

    public SelectQuestionImpl(String question) {
        super(question);
    }

    @Override
    public String[] options() {
        return new String[0];
    }
}
