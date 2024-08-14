package dev.httpmarco.polocloud.node.terminal.setup;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.util.Pair;
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

    public void displayQuestion(String remark) {
        var terminal = Node.instance().terminal();
        terminal.updatePrompt("&8» &7");
        terminal.clear();

        var question = question();
        terminal.printLine("&b" + name + " &8- &7Question &8(&7" + (index + 1) + "&8/&7" + questions.size() + "&8) &7" + question.question());

        if (!question.possibleAnswers().apply(answers).isEmpty()) {
            terminal.printLine("&ePossible answers: " + String.join(", ", question.possibleAnswers().apply(answers)));
        }

        if(answers.containsKey(question.answerKey())) {
            terminal.printLine("&7The previous response was&8: &f" + answers.get(question.answerKey()));
        }

        if (remark != null) {
            terminal.printLine(remark);
        }

        // we write an empty placeholder
        terminal.printLine(" ");
        terminal.printLine(" ");
        terminal.printLine("Enter &8'&7exit&8' &7for leave the setup or enter &8'&7back&8' &7for see the previous question&8!");
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
            displayQuestion("&cThe given answer is not correct.");
            return;
        }

        answers.put(question().answerKey(), answer);

        index++;
        if (index >= questions.size()) {
            exit();
            return;
        }
        this.displayQuestion();
    }

    public void previousQuestion() {
        if(index == 0) {
            displayQuestion();
            return;
        }
        index--;
        displayQuestion();
    }

    public void exit() {
        Node.instance().terminal().clear();
        //todo add a result builder
        Node.instance().terminal().setup(null);
    }

}