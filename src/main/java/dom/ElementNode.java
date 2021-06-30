package dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class ElementNode extends Node {


    public ElementNode() {
        attributes = new LinkedHashMap<>();
    }

    public ElementNode(String tagName) {
        this();
        this.tagName = tagName;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public void addAttr(String key, String value) {
        attributes.put(key, value);
    }


    public ArrayList<Attribute> getSortedAttrArray() {
        Collections.sort(attributeArray);
        return attributeArray;
    }
}
