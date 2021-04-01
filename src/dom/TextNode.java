package dom;

import java.util.ArrayList;
import java.util.HashMap;

public class TextNode extends Node {
    String text;

    public TextNode(){
    }

    public TextNode(String text){
        this.text = text;
    }

    @Override
    public void buildString(StringBuffer buffer, int level) {
        buffer.delete(buffer.length() - 1, buffer.length());
        buffer.append(text);
    }

    @Override
    public ArrayList<Attribute> getSortedAttrArray() {
        return null;
    }
}
