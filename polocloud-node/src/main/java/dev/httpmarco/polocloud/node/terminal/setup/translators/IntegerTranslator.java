package dev.httpmarco.polocloud.node.terminal.setup.translators;

import dev.httpmarco.polocloud.node.terminal.setup.QuestionTranslator;

public final class IntegerTranslator implements QuestionTranslator<Integer> {

    @Override
    public Integer translate(String input) {
        return Integer.parseInt(input);
    }
}
