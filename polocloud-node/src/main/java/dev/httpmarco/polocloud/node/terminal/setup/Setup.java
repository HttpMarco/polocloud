package dev.httpmarco.polocloud.node.terminal.setup;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.utils.Pair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class Setup implements Named {

    private int index = 0;
    private final String name;
    private final List<SetupQuestion> questions = new ArrayList<>();
    private final Map<String, String> answers = new HashMap<>();

    public void question(String answerKey, String question, Function<Map<String, String>, List<String>> possibleAnswers, Function<Pair<String, Map<String, String>>, Boolean> prediction) {
        this.questions.add(new SetupQuestion(answerKey, question, possibleAnswers, prediction));
    }

    public void question(String answerKey, String question, Function<Pair<String, Map<String, String>>, Boolean> prediction) {
        this.questions.add(new SetupQuestion(answerKey, question, stringStringMap -> List.of(), prediction));
    }

    public abstract void complete(Map<String, String> context);

    public void displayQuestion(String remark) {
        var terminal = Node.instance().terminal();

        terminal.updatePrompt("&8» &7");
        terminal.clear();

        var question = question();
        terminal.printLine(Node.translation().get("setup.header.text", name, (index + 1), questions.size(), question.question()));

        if (!question.possibleAnswers().apply(answers).isEmpty()) {
            terminal.printLine(Node.translation().get("setup.answers.text") + String.join("&8, &f", question.possibleAnswers().apply(answers)));
        }

        if (answers.containsKey(question.answerKey())) {
            terminal.printLine(Node.translation().get("setup.answer.before", answers.get(question.answerKey())));
        }

        if (remark != null) {
            terminal.printLine(remark);
        }

        // we write an empty placeholder
        terminal.printLine(" ");
        terminal.printLine(Node.translation().get("setup.exit.text"));
    }

    public void displayQuestion() {
        this.displayQuestion(null);
    }

    public void run() {
        Node.instance().terminal().setup(this);

        displayQuestion();
    }

    public List<String> possibleAnswers() {
        return this.question().possibleAnswers().apply(answers);
    }

    public SetupQuestion question() {
        return questions().get(index);
    }

    public void answer(String answer) {

        if (question().predicate() != null && !question().predicate().apply(new Pair<>(answer, answers))) {
            displayQuestion(Node.translation().get("setup.answer.wrong"));
            return;
        }

        answers.put(question().answerKey(), answer);

        index++;

        if (index >= questions.size()) {
            exit(true);
            return;
        }
        this.displayQuestion();
    }

    public void previousQuestion() {
        if (index == 0) {
            displayQuestion();
            return;
        }
        index--;
        displayQuestion();
    }

    public void exit(boolean completed) {
        Node.instance().terminal().setup(null);
        Node.instance().terminal().clear();

        if (completed) {
            complete(this.answers);
        }
    }
}