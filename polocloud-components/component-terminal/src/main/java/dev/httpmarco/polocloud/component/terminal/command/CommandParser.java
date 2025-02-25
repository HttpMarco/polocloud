package dev.httpmarco.polocloud.component.terminal.command;

import dev.httpmarco.polocloud.component.terminal.command.arguments.KeywordArgument;
import dev.httpmarco.polocloud.component.terminal.command.arguments.StringArrayArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandParser {

    private static final Logger log = LoggerFactory.getLogger(CommandParser.class);

    public static void serializer(CommandService commandService, String name, String[] args) {
        // all command with same start name
        List<Command> commands = commandService.commandsByName(name);

        if (executeCommand(commands, args)) {
            return;
        }

        // we must calculate the usage, because no command was found
        for (var command : commands) {
            if (command.defaultExecution() != null) {
                command.defaultExecution().execute(new CommandContext());
            } else {
                for (var syntaxCommand : command.commandSyntaxes()) {
                    log.info("{} {}", command.name(), syntaxCommand.usage());
                }
            }
        }
    }

    private static boolean executeCommand(List<Command> commands, String[] args) {
        for (var command : commands) {
            if ((!command.hasSyntaxCommands()) || args.length == 0) {
                return false;
            }

            for (var syntaxCommand : command.commandSyntaxes()) {
                if (args.length != syntaxCommand.arguments().length && Arrays.stream(syntaxCommand.arguments()).noneMatch(it -> it instanceof StringArrayArgument)) {
                    continue;
                }

                var commandContext = new CommandContext();

                var provedSyntax = true;
                Optional<String> provedSyntaxWarning = Optional.empty();

                for (int i = 0; i < syntaxCommand.arguments().length; i++) {
                    var argument = syntaxCommand.arguments()[i];

                    if (i >= args.length) {
                        provedSyntax = false;
                        break;
                    }

                    var rawInput = args[i];

                    if (argument instanceof StringArrayArgument arrayArgument) {
                        commandContext.append(arrayArgument, argument.buildResult(String.join(" ", Arrays.copyOfRange(args, i, args.length))));
                        break;
                    } else if (argument instanceof KeywordArgument keywordArgument) {
                        if (!keywordArgument.key().equalsIgnoreCase(rawInput)) {
                            provedSyntax = false;
                            break;
                        }
                    } else if (!argument.predication(rawInput)) {
                        provedSyntaxWarning = Optional.of(argument.wrongReason());
                        continue;
                    }

                    commandContext.append(argument, argument.buildResult(rawInput));
                }

                if (!provedSyntax) {
                    continue;
                }

                if (provedSyntaxWarning.isPresent()) {
                    log.warn(provedSyntaxWarning.get());
                    return true;
                }
                syntaxCommand.execution().execute(commandContext);
                return true;
            }
        }
        return false;
    }

}
