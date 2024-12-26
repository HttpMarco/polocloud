package dev.httpmarco.polocloud.node.terminal.setup;

import dev.httpmarco.polocloud.node.terminal.setup.questions.SelectedQuestion;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class SetupQuestionBuilder<T> {

    private final String question;

    @Contract(" -> new")
    public <S> @NotNull SelectedQuestion<T, S> ofSelection() {
        return new SelectedQuestion<>(this, question);
    }
}