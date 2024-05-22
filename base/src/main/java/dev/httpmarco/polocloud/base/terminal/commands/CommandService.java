package dev.httpmarco.polocloud.base.terminal.commands;

import dev.httpmarco.polocloud.base.groups.GroupCommand;
import dev.httpmarco.polocloud.base.node.NodeCommand;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CommandService {

    private final List<Object> commands = new ArrayList<>();

    public CommandService() {
        this.registerCommand(new ShutdownCommand());
        this.registerCommand(new NodeCommand());
        this.registerCommand(new GroupCommand());
    }

    private void registerCommand(Object command) {
        this.commands.add(command);
    }

    @SneakyThrows
    public void call(String[] args) {
        var main = args[0];
        for (var command : commands) {

            Command mainCommand = null;

            for (var method : command.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Command.class)) {
                    continue;
                }
                mainCommand = method.getDeclaredAnnotation(Command.class);
                break;
            }

            if (mainCommand == null) {
                continue;
            }

            if (!(main.equalsIgnoreCase(mainCommand.command()) || Arrays.stream(mainCommand.aliases()).anyMatch(it -> it.equalsIgnoreCase(main)))) {
                continue;
            }

            for (var method : command.getClass().getDeclaredMethods()) {
                if (args.length == 1 && method.isAnnotationPresent(Command.class)) {
                    method.invoke(command);
                }
                if (!method.isAnnotationPresent(SubCommand.class)) {
                    continue;
                }

                var commandData = method.getDeclaredAnnotation(SubCommand.class);

                if ((commandData.args().length + 1) == args.length) {
                    if (Arrays.equals(Arrays.copyOfRange(args, 1, args.length), commandData.args())) {
                        method.invoke(command);
                    }
                }
            }
        }
    }
}
