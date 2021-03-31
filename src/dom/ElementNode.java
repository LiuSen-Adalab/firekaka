package dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class ElementNode extends Node {


    public ElementNode() {
        attributes = new LinkedHashMap<>();
        attributeArray = new ArrayList<>();

    }

    public ElementNode(String tagName) {
        this();
        this.tagName = tagName;
        attributeArray.add(new Attribute("tag", tagName));
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public void addAttr(String key, String value) {
        attributes.put(key, value);

        addAttrToList(key,value);
    }

    public void addAttrToList(String type, String value) {
        if (type.equals("class")) {
            String[] values = value.split(" ");
            for (String subValue : values) {
                attributeArray.add(new Attribute("class", subValue));
                attributeArray.add(new Attribute("tag_class", tagName + "." + subValue));
            }
        }else {
            attributeArray.add(new Attribute(type, value));
        }
    }

    public ArrayList<Attribute> getSortedAttrArray() {
        Collections.sort(attributeArray);
        return attributeArray;
    }
}
