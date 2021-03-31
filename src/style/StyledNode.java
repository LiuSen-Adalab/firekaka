package style;

import css.Stylesheet;
import dom.Attribute;
import dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class StyledNode {
    HashMap<String, String> declarations;
    ArrayList<StyledNode> children;
    
    String name;
    Stylesheet stylesheet;


    public StyledNode(Node domNode, Stylesheet stylesheet) {
        declarations = new LinkedHashMap<>();
        children = new ArrayList<>();
        this.stylesheet = stylesheet;
        this.name = domNode.getTagName();
        addStylesToStyleNode(domNode);
        buildTree(domNode);
    }

    private void buildTree(Node domNode) {
        for (Node child : domNode.getChildren()) {
            children.add(new StyledNode(child, stylesheet));
        }
    }

    private void addStylesToStyleNode(Node domNode) {
        ArrayList<Attribute> attrs = domNode.getSortedAttrArray();
        if (attrs != null) {
            for (Attribute attr : attrs) {
                HashMap<String, String> allDeclarationsOfAttr = stylesheet.getDeclarations(attr.getSelectorName());
                if (allDeclarationsOfAttr != null) {
                    declarations.putAll(allDeclarationsOfAttr);
                }
            }
        }
    }

    public String declarationsToString() {
        StringBuffer buffer = new StringBuffer();
        declarations.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String property, String value) {
                buffer.append(property).append("=");
                buffer.append("\"").append(value).append("\" ");
            }
        });
        if (buffer.length() > 0) {
            buffer.delete(buffer.length() - 1, buffer.length());
        }

        return buffer.toString();
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buildString(buffer, 0);

        return buffer.toString();
    }


    private void buildString(StringBuffer buffer, int level) {
        appendBlank(buffer, level);
        if (name == null) {
            buffer.append("<text></text>\n");
            return;
        } else {
            buffer.append("<").append(name);
            buffer.append(" ").append(declarationsToString()).append(">\n");
        }
        for (StyledNode child : children) {
            child.buildString(buffer, level + 1);
        }

        appendBlank(buffer, level);
        buffer.append("</").append(name).append(">\n");
    }

    private void appendBlank(StringBuffer buffer, int level) {
        for (int i = 0; i < level; i++) {
            buffer.append("  ");
        }
    }

}

