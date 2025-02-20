package dev.httpmarco.polocloud.component.terminal;

import dev.httpmarco.polocloud.suite.components.Component;

@Component.Info(name = "terminal", version = "1.0.0")
public class TerminalComponent extends Component {

    // todo list component
    // 1. Reading Thread
    // 2. Add prompt (prefix) at reading thread
    // 3. old command system like minestom
    // 4. Add auto completer
    // 5. clean shutdown


    @Override
    public void start() {
        System.out.println("Starting terminal component");
    }

    @Override
    public void stop() {
        System.out.println("Stopping terminal component");
    }
}
