package dev.httpmarco.polocloud.node.terminal.setup.impl;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.terminal.setup.SelectQuestion;
import dev.httpmarco.polocloud.node.terminal.setup.common.AbstractQuestion;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;

@Accessors(fluent = true)
@Getter
public class SelectQuestionImpl extends AbstractQuestion implements SelectQuestion {

    private final String[] options;

    public SelectQuestionImpl(String question, String... options) {
        super(question);
        this.options = options;
    }

    @Override
    public void display() {
        Node.instance().terminal().hideCursor();
        Arrays.stream(options).forEach(s -> print(" &8- [ ] &7" + s));
    }
}
