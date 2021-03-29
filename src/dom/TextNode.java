package dom;

import java.util.ArrayList;

public class TextNode extends Node {
    String text;
    public TextNode() {
        children = new ArrayList<>();
    }

    public TextNode(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
