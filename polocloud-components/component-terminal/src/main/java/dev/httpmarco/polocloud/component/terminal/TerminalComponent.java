package dev.httpmarco.polocloud.component.terminal;

import dev.httpmarco.polocloud.suite.components.Component;

@Component.Info(name = "terminal", version = "1.0.0")
public class TerminalComponent extends Component {

    @Override
    public void start() {
        System.out.println("Starting terminal component");
    }

    @Override
    public void stop() {
        System.out.println("Stopping terminal component");
    }
}
