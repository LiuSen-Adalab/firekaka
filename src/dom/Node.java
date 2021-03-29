package dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

public abstract class Node {
    ArrayList<Node> children = new ArrayList<>();
    String tagName;
    HashMap<String, String> attrMap;

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buildString(buffer, 0);

        return buffer.toString();
    }

    private void buildString(StringBuffer buffer, int level) {
        if (this.tagName == null) {
            buffer.delete(buffer.length() - 1, buffer.length());
            buffer.append(((TextNode) this).text);
            return;
        }
        addBlank(buffer, level);
        buffer.append("<").append(this.tagName);
        addAttr(buffer);
        buffer.append(">\n");

        for (Node child : children) {
            child.buildString(buffer, level + 1);
        }

        if (buffer.lastIndexOf("\n") == buffer.length() - 1) {
            addBlank(buffer, level);
        }
        buffer.append("</").append(tagName).append(">\n");
    }

    private void addAttr(StringBuffer buffer) {
        this.attrMap.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                buffer.append(" ").append(String.format("%s=\"%s\"", s, s2));
            }
        });
    }


    private void addBlank(StringBuffer buffer, int level) {
        for (int i = 0; i < level; i++) {
            buffer.append("  ");
        }
    }

}
