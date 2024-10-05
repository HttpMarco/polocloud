package dev.httpmarco.polocloud.node.commands.specific;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.CommandArgument;
import dev.httpmarco.polocloud.node.commands.CommandContext;
import dev.httpmarco.polocloud.node.templates.Template;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

public final class TemplateArgument extends CommandArgument<Template> {

    public TemplateArgument(String key) {
        super(key);
    }

    @Override
    public @NotNull @Unmodifiable List<String> defaultArgs(CommandContext context) {
        var templates = new ArrayList<>(Node.instance().templatesProvider().templates().stream().map(Template::templateId).toList());
        templates.add("<template>");
        return templates;
    }

    @Contract("_ -> new")
    @Override
    public Template buildResult(String input) {
        return Node.instance().templatesProvider().templates().stream().filter(it -> it.templateId().equals(input)).findFirst().orElse(new Template(input));
    }
}
