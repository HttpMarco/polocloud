package dev.httpmarco.polocloud.node.terminal.setup.impl;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.terminal.setup.SelectQuestion;
import dev.httpmarco.polocloud.node.terminal.setup.common.AbstractQuestion;
import dev.httpmarco.polocloud.node.terminal.setup.common.AbstractSetup;
import dev.httpmarco.polocloud.node.terminal.setup.impl.select.SelectedQuestion;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Accessors(fluent = true)
@Getter
public final class SelectQuestionImpl extends AbstractQuestion implements SelectQuestion {

    private final AbstractSetup bindedSetup;

    private int selectedQuestion = 0;
    private final List<SelectedQuestion> options;

    public SelectQuestionImpl(AbstractSetup setup, String question, String... options) {
        super(question);
        this.bindedSetup = setup;
        this.options = Arrays.stream(options).map(SelectedQuestion::new).toList();
    }

    @Override
    public void display() {
        Node.instance().terminal().hideCursor();

        for (int i = 0; i < options.size(); i++) {
            var question = options.get(i);

            if(i == selectedQuestion) {
                print("   &8[&b✘&8] &b" + question.option());
                continue;
            }
            print("   &8[ ] &7" + question.option());
        }
    }

    public void nextSelectedOption() {
        if(selectedQuestion == options.size() - 1){
            this.selectedQuestion = 0;
        } else {
            this.selectedQuestion++;
        }
        this.bindedSetup.redisplay();
    }

    public void previousSelectedOption() {
        if(selectedQuestion == 0){
            this.selectedQuestion = options.size() - 1;
        } else {
            this.selectedQuestion--;
        }
        this.bindedSetup.redisplay();
    }
}
