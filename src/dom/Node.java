package dom;

import css.Selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public abstract class Node {
    ArrayList<Node> children = new ArrayList<>();
    String tagName;
    HashMap<String, String> attributes;
    ArrayList<Attribute> attributeArray;


    public ArrayList<Node> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buildString(buffer, 0);

        return buffer.toString();
    }

    public void buildString(StringBuffer buffer, int level) {
        writeTagHead(buffer, level);  // write tag head
        writeChildren(buffer, level); // write children recursively
        writeTagTail(buffer, level);  // write tag tail
    }

    private void writeTagTail(StringBuffer buffer, int level) {
        if (isElementNode(buffer)) {
            appendBlank(buffer, level);
        }
        buffer.append("</").append(tagName).append(">\n");
    }

    private boolean isElementNode(StringBuffer buffer){
        return buffer.lastIndexOf("\n") == buffer.length() - 1;
    }

    private void writeChildren(StringBuffer buffer, int level) {
        for (Node child : children) {
            child.buildString(buffer, level + 1);
        }
    }

    private void writeTagHead(StringBuffer buffer, int level) {
        appendBlank(buffer, level);
        buffer.append("<").append(this.tagName);
        appendAttr(buffer);
        buffer.append(">\n");
    }

    private void appendAttr(StringBuffer buffer) {
        attributes.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                buffer.append(" ").append(String.format("%s=\"%s\"", s, s2));
            }
        });
    }

    private void appendBlank(StringBuffer buffer, int level) {
        for (int i = 0; i < level; i++) {
            buffer.append("  ");
        }
    }

    public abstract ArrayList<Attribute> getSortedAttrArray();

    public String getTagName() {
        return tagName;
    }
}
