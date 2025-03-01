package dev.httpmarco.polocloud.suite.terminal;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.commands.CommandContext;
import dev.httpmarco.polocloud.suite.commands.CommandSyntax;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.Arrays;
import java.util.List;

public class PolocloudTerminalCompleter implements Completer {

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        var commandService = PolocloudSuite.instance().commandService();

        if (parsedLine.wordIndex() == 0) {

            /*
            if (PolocloudSuite.instance().terminal().hasSetup()) {

                for (var input : PolocloudSuite.instance().terminal().setup().possibleAnswers()) {
                    list.add(new Candidate(input));
                }

                list.add(new Candidate("exit"));

                if (PolocloudSuite.instance().terminal().setup().index() > 0) {
                    list.add(new Candidate("back"));
                }
                return;
            }


            if (PolocloudSuite.instance().screenProvider().isUsed()) {
                list.add(new Candidate("exit"));
                return;
            }

             */

            // we only display the command names -> not aliases

            for (var command : commandService.commands()) {
                // if one command start with the given command first word
                if (command.name().startsWith(parsedLine.word())) {
                    list.add(new Candidate(command.name()));
                }

                Arrays.stream(command.aliases())
                        .filter(it -> !parsedLine.word().isEmpty() && it.startsWith(parsedLine.word()))
                        .forEach(alias -> list.add(new Candidate(alias)));
            }
            return;
        }

        /*
        if (Node.instance().terminal().hasSetup()) {
            return;
        }

         */

        var commandName = parsedLine.words().get(0);
        for (var command : commandService.commandsByName(commandName)) {

            for (CommandSyntax commandSyntax : command.commandSyntaxes()) {

                if (isMatchingSyntax(parsedLine, commandSyntax)) {
                    addSuggestions(parsedLine, commandSyntax, list);
                }
            }
        }
    }

    private boolean isMatchingSyntax(ParsedLine parsedLine, CommandSyntax commandSyntax) {
        var argumentIndex = parsedLine.wordIndex() - 1;
        if (argumentIndex >= commandSyntax.arguments().length) {
            return false;
        }

        for (int i = 0; i < argumentIndex; i++) {
            var expectedArgument = commandSyntax.arguments()[i];
            var enteredArgument = parsedLine.words().get(i + 1).replace("<", "").replace(">", "");

            if ((!expectedArgument.key().equals(enteredArgument) && !expectedArgument.predication(enteredArgument))) {
                return false;
            }
        }
        return true;
    }

    private void addSuggestions(ParsedLine parsedLine, CommandSyntax commandSyntax, List<Candidate> list) {
        var argumentIndex = parsedLine.wordIndex() - 1;
        if (argumentIndex >= commandSyntax.arguments().length) {
            return;
        }

        var argument = commandSyntax.arguments()[argumentIndex];
        var context = new CommandContext();

        for (int i = 0; i < argumentIndex; i++) {
            // read all previous temp parameters
            var input = parsedLine.words().get(i + 1).replace("<", "").replace(">", "");
            var tempArgument = commandSyntax.arguments()[i];
            if (input.equalsIgnoreCase(tempArgument.key())) {
                continue;
            }
            context.append(commandSyntax.arguments()[i], commandSyntax.arguments()[i].buildResult(input));
        }

        if (argument.defaultArgs(context).isEmpty()) {
            String candidateValue = "<" + argument.key() + ">";
            if (list.stream().noneMatch(candidate -> candidate.value().equals(candidateValue))) {
                list.add(new Candidate(candidateValue));
            }
        } else {
            argument.defaultArgs(context).stream()
                    .filter(defaultArg -> list.stream().noneMatch(candidate -> candidate.value().equals(defaultArg)))
                    .forEach(defaultArg -> list.add(new Candidate(defaultArg)));
        }
    }
}