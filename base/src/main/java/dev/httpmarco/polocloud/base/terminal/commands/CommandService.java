package dev.httpmarco.polocloud.base.terminal.commands;

import dev.httpmarco.polocloud.base.terminal.commands.impl.ShutdownCommand;
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
    }

    private void registerCommand(Object command) {
        this.commands.add(command);
    }

    @SneakyThrows
    public void call(String[] args) {
        var main = args[0];
        for (var command : commands) {
            for (var method : command.getClass().getDeclaredMethods()) {

                if (!method.isAnnotationPresent(Command.class)) {
                    return;
                }

                var commandData = method.getDeclaredAnnotation(Command.class);

                if (main.equalsIgnoreCase(commandData.command()) || Arrays.stream(commandData.aliases()).anyMatch(it -> it.equalsIgnoreCase(main))) {
                    method.invoke(command);
                    continue;
                }
            }
        }
    }
}
