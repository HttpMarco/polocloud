package dev.httpmarco.polocloud.node.terminal.setup;

public abstract class Setup<T> {

    public SetupQuestionBuilder<T> question(String question) {
        return new SetupQuestionBuilder<>(question);
    }

    public abstract T newInstance();

}