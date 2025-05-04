package dev.httpmarco.polocloud.suite.terminal.setup;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.utils.Pair;
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
public abstract class Setup {

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

    public void question(String answerKey, String question) {
        this.questions.add(new SetupQuestion(answerKey, question, stringStringMap -> List.of(), stringMapPair -> true));
    }

    public abstract void complete(Map<String, String> context);

    public void displayQuestion(String remark) {
        var translation = PolocloudSuite.instance().translation();
        var terminal = PolocloudSuite.instance().terminal();
        terminal.prompt("&8» &7");
        terminal.clear();

        var question = question();
        terminal.print(translation.get("suite.terminal.setup.question", name, index + 1, questions.size(), question.question()));

        if (!question.possibleAnswers().apply(answers).isEmpty()) {
            terminal.print(translation.get("suite.terminal.setup.possibleAnswers", String.join("&8, &f", question.possibleAnswers().apply(answers))));
        }

        if (answers.containsKey(question.answerKey())) {
            terminal.print(translation.get("suite.terminal.setup.previousResponse", answers.get(question.answerKey())));
        }

        if (remark != null) {
            terminal.print(remark);
        }

        // we write an empty placeholder
        terminal.print(" ");
        terminal.print(translation.get("suite.terminal.setup.navigation"));
    }

    public void displayQuestion() {
        this.displayQuestion(null);
    }

    public void run() {

        if(this.questions.isEmpty()) {
            this.exit(true);
            return;
        }

        PolocloudSuite.instance().terminal().changeSetup(this);

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
            displayQuestion(PolocloudSuite.instance().translation().get("suite.terminal.setup.answer.invalid"));
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
        var terminal = PolocloudSuite.instance().terminal();
        terminal.revertSetup();
        terminal.clear();

        if (completed) {
            complete(this.answers);
        }
    }
}