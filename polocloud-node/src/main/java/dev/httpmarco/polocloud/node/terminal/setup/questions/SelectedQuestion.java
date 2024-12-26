package dev.httpmarco.polocloud.node.terminal.setup.questions;

import dev.httpmarco.polocloud.node.terminal.setup.QuestionTranslator;
import dev.httpmarco.polocloud.node.terminal.setup.SetupQuestion;
import dev.httpmarco.polocloud.node.terminal.setup.SetupQuestionBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public final class SelectedQuestion<S, I> extends SetupQuestion<S> {

    private @Nullable QuestionTranslator<I> type;
    private @Nullable I[] items;

    public SelectedQuestion(SetupQuestionBuilder<S> setupQuestionBuilder, String question) {
        super(question, setupQuestionBuilder);
    }

    public <T extends QuestionTranslator<I>> SelectedQuestion<S, I> defineType(T type) {
        this.type = type;
        return this;
    }

    public SelectedQuestion<S, I> items(I... items) {
        this.items = items;
        return this;
    }

    public SelectedQuestion<S, I> ifAnswer(S output, Function<Void, Boolean> condition) {
        return this;
    }

    @Override
    public void display() {

    }
}
