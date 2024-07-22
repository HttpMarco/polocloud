/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.base.terminal.commands;

import dev.httpmarco.polocloud.base.Node;
import lombok.SneakyThrows;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.Arrays;
import java.util.List;

public final class CommandCompleter implements Completer {

    @Override
    @SneakyThrows
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        var context = line.line().split(" ", -1);

        if (context.length == 0) {
            return;
        }

        var main = context[0];

        for (var command : Node.instance().terminal().commandService().commands()) {

            var data = command.getClass().getDeclaredAnnotation(Command.class);

            if (context.length == 1) {
                if (data.command().startsWith(main)) {
                    candidates.add(new Candidate(data.command()));
                }
            } else if (main.equalsIgnoreCase(data.command()) || Arrays.stream(data.aliases()).anyMatch(it -> it.equalsIgnoreCase(main))) {
                //sub commands
                var subIndex = line.wordIndex();
                var subCommand = Arrays.copyOfRange(context, 1, context.length);

                outerLoop:
                for (var completer : command.getClass().getDeclaredMethods()) {

                    if (!completer.isAnnotationPresent(SubCommandCompleter.class)) {
                        continue;
                    }

                    var subCompleter = completer.getDeclaredAnnotation(SubCommandCompleter.class);

                    if (subCompleter.completionPattern().length < subIndex) {
                        continue;
                    }

                    for (int i = 1; i < subCommand.length; i++) {
                        if (!subCommand[i-1].equals(subCompleter.completionPattern()[i-1]) && !(subCompleter.completionPattern()[i-1].startsWith("<") && (subCompleter.completionPattern()[i-1].endsWith(">")))) {
                            continue outerLoop;
                        }
                    }

                    if (subCompleter.completionPattern()[subIndex - 1].startsWith("<") && (subCompleter.completionPattern()[subIndex - 1].endsWith(">"))) {
                        completer.invoke(command, subIndex, candidates);
                    } else {
                        candidates.add(new Candidate(subCompleter.completionPattern()[subIndex - 1]));
                    }
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
