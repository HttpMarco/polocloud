package dev.httpmarco.polocloud.node.terminal.impl.sessions;

import dev.httpmarco.polocloud.node.terminal.NodeTerminalSession;
import org.jetbrains.annotations.NotNull;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.Reference;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

public final class SetupTerminalSession implements NodeTerminalSession<Binding> {

    private final KeyMap<Binding> bindings = new KeyMap<>();

    @Override
    public Binding waitFor(@NotNull LineReaderImpl lineReader) {
        var result = lineReader.readBinding(bindings);

        return result;
    }

    @Override
    public void handleInput(@NotNull Binding result) {
        System.out.println(result.getClass().getName());
    }

    @Override
    public void prepare(Terminal terminal) {
        this.bindings.bind(new Reference("select-up"), KeyMap.key(terminal, InfoCmp.Capability.key_down));
        this.bindings.bind(new Reference("select-down"), KeyMap.key(terminal, InfoCmp.Capability.key_up));

        // todo
       // this.bindings.bind(new Reference("confirm-selection"), KeyMap.key(terminal, InfoCmp.Capability.keypad_xmit));
    }
}
