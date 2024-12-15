package dev.httpmarco.polocloud.node.terminal.setup;

import dev.httpmarco.polocloud.node.terminal.setup.impl.select.SelectedQuestion;

import java.util.List;

public interface SelectQuestion extends Question{

    List<SelectedQuestion> options();

}
