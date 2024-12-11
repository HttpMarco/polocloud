package dev.httpmarco.polocloud.node.terminal.impl.sessions;

import dev.httpmarco.polocloud.node.terminal.NodeTerminalSession;
import dev.httpmarco.polocloud.node.terminal.setup.SelectQuestion;
import dev.httpmarco.polocloud.node.terminal.setup.Setup;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.Reference;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

@RequiredArgsConstructor
public final class SetupTerminalSession implements NodeTerminalSession<String> {

    private final KeyMap<Binding> bindings = new KeyMap<>();
    private final Setup setup;

    @Override
    public String waitFor(@NotNull LineReaderImpl lineReader) {
        if(setup.current().question() instanceof SelectQuestion) {
            return ((Reference) lineReader.readBinding(bindings)).name();
        } else {
            return lineReader.readLine().trim();
        }
    }

    @Override
    public void handleInput(@NotNull String result) {
        System.out.println("Handling input: " + result);
    }

    @Override
    public void prepare(Terminal terminal) {
        this.bindings.bind(new Reference("select-up"), KeyMap.key(terminal, InfoCmp.Capability.key_down));
        this.bindings.bind(new Reference("select-down"), KeyMap.key(terminal, InfoCmp.Capability.key_up));

        // todo
       // this.bindings.bind(new Reference("confirm-selection"), KeyMap.key(terminal, InfoCmp.Capability.keypad_xmit));
    }
}
