package dev.httpmarco.polocloud.component.terminal;

import dev.httpmarco.polocloud.component.api.Component;
import dev.httpmarco.polocloud.component.terminal.command.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component.Info(name = "terminal", version = "1.0.0")
public class TerminalComponent  extends Component {
    private static final Logger log = LoggerFactory.getLogger(TerminalComponent.class);

    // todo list component
    // 4. Add auto completer

    private static TerminalComponent instance;

    private CommandService commandService;
    private PolocloudTerminal terminal;

    @Override
    public void start() {
        this.commandService = new CommandService();
        (terminal = new PolocloudTerminalImpl()).start();

        instance = this;
    }

    @Override
    public void stop() {
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