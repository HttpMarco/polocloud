package dev.httpmarco.polocloud.base.terminal.commands;

import dev.httpmarco.polocloud.base.CloudBase;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public final class CommandCompleter implements Completer {

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {

        var context = line.line().split(" ", -1);

        if (context.length == 0) {
            return;
        }

        var main = context[0];

        for (var command : CloudBase.instance().terminal().commandService().commands()) {

            var data = command.getClass().getDeclaredAnnotation(Command.class);

            if (context.length == 1) {
                if (data.command().startsWith(main)) {
                    candidates.add(new Candidate(data.command()));
                }
            } else if (main.equalsIgnoreCase(data.command()) || Arrays.stream(data.aliases()).anyMatch(it -> it.equalsIgnoreCase(main))) {
                //sub commands
                var subIndex = line.wordIndex();
                var subCommand = Arrays.copyOfRange(context, 1, context.length);

                for (var completer : command.getClass().getDeclaredMethods()) {

                    if (!completer.isAnnotationPresent(SubCommandCompleter.class)) {
                        continue;
                    }

                    var subCompleter = completer.getDeclaredAnnotation(SubCommandCompleter.class);




                }

                for (var completer : command.getClass().getDeclaredMethods()) {
                    if (!completer.isAnnotationPresent(SubCommand.class)) {
                        continue;
                    }

                    var subData = completer.getDeclaredAnnotation(SubCommand.class);
                    if (Arrays.stream(subData.args()).anyMatch(s -> s.startsWith("<") && s.endsWith(">"))) {
                        continue;
                    }

                    if (subData.args().length < subIndex) {
                        continue;
                    }

                    var possibleSubCommand = subData.args()[subIndex - 1];
                    candidates.add(new Candidate(possibleSubCommand));
                    break;
                }
            }
        }
    }
}
