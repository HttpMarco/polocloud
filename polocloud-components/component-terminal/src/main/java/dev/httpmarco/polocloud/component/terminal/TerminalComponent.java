package dev.httpmarco.polocloud.component.terminal;

import dev.httpmarco.polocloud.component.terminal.command.CommandService;
import dev.httpmarco.polocloud.suite.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component.Info(name = "terminal", version = "1.0.0")
public class TerminalComponent extends Component {
    private static final Logger log = LoggerFactory.getLogger(TerminalComponent.class);

    // todo list component
    // 1. Reading Thread
    // 2. Add prompt (prefix) at reading thread
    // 3. old command system like minestom
    // 4. Add auto completer
    // 5. clean shutdown

    private static TerminalComponent instance;

    private CommandService commandService;
    private PolocloudTerminal terminal;

    @Override
    public void start() {
        System.out.println("Starting terminal component");

        this.commandService = new CommandService();
        (terminal = new PolocloudTerminalImpl()).start();

        instance = this;
    }

    @Override
    public void stop() {
        System.out.println("Stopping terminal component");

        try {
            this.terminal.close();
        } catch (Exception ex) {
            log.error("Failed to close terminal", ex);
        }
    }

    public static TerminalComponent instance() {
        return instance;
    }

    public CommandService commandService() {
        return commandService;
    }

}