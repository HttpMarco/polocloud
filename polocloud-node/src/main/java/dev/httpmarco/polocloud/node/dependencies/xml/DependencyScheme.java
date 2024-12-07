package dev.httpmarco.polocloud.node.dependencies.xml;

import dev.httpmarco.polocloud.node.dependencies.common.AbstractDependency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class DependencyScheme extends AbstractDependency {

    private final String scope;
    private final boolean optional;

    public DependencyScheme(Node node) {
        if(!(node instanceof Element element)) {
            throw new IllegalArgumentException("Dependency node is not an element!");
        }

        this.groupId( textElement(element, "groupId"));
        this.artifactId(textElement(element, "artifactId"));
        this.version(textElement(element, "version"));

        this.scope = textElement(element, "scope");
        this.optional = Boolean.parseBoolean(textElement(element, "optional"));
    }

    private @Nullable String textElement(@NotNull Element element, String tagName) {
        var nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }
}