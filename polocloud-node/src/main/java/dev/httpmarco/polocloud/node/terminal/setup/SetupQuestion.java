package dev.httpmarco.polocloud.node.terminal.setup;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class SetupQuestion<T> implements Question {

    private final String question;
    private final SetupQuestionBuilder<T> setupBuilder;

    public SetupQuestionBuilder<T> and() {
        return this.setupBuilder;
    }
}
