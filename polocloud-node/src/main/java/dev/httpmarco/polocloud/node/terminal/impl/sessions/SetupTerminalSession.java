package dev.httpmarco.polocloud.node.terminal.impl.sessions;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.terminal.NodeTerminalSession;
import dev.httpmarco.polocloud.node.terminal.setup.Question;
import dev.httpmarco.polocloud.node.terminal.setup.SelectQuestion;
import dev.httpmarco.polocloud.node.terminal.setup.Setup;
import dev.httpmarco.polocloud.node.terminal.setup.impl.SelectQuestionImpl;
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
        var question = setup.current().question();

        if(question instanceof SelectQuestionImpl selectQuestion) {

            if(result.equals("select-up")) {
                selectQuestion.nextSelectedOption();
                return;
            }

            if(result.equals("select-down")) {
                selectQuestion.previousSelectedOption();
            }

            if(result.equals("selection")) {
                setup.next();
            }
            return;
        }
    }

    @Override
    public void prepare(Terminal terminal) {
        this.bindings.bind(new Reference("select-up"), KeyMap.key(terminal, InfoCmp.Capability.key_down));
        this.bindings.bind(new Reference("select-down"), KeyMap.key(terminal, InfoCmp.Capability.key_up));
        this.bindings.bind(new Reference("selection"), KeyMap.key(terminal, InfoCmp.Capability.carriage_return));
    }
}
