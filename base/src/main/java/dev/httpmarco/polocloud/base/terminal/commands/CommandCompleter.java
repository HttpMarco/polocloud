package dev.httpmarco.polocloud.base.terminal.commands;

import dev.httpmarco.polocloud.base.CloudBase;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

public final class CommandCompleter implements Completer {

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {

        var context = line.line().split(" ", -1);

        for (var command : CloudBase.instance().terminal().commandService().commands()) {

            if(context.length == 1) {
                var main = context[0];

                for (var method : command.getClass().getDeclaredMethods()) {
                    if (!method.isAnnotationPresent(Command.class)) {
                        continue;
                    }

                    var commandData = method.getDeclaredAnnotation(Command.class);

                    if(commandData.command().startsWith(main)) {
                        candidates.add(new Candidate(commandData.command()));
                    }

                    for (var alias : commandData.aliases()) {
                        if(alias.startsWith(main)) {
                            candidates.add(new Candidate(alias));
                        }
                    }
                }
            }
        }
    }
}
