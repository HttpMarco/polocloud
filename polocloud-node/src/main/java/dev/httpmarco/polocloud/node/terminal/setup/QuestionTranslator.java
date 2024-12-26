package dev.httpmarco.polocloud.node.terminal.setup;

public interface QuestionTranslator<T> {

    T translate(String input);

}