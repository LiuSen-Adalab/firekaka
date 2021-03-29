package dom;

import java.util.ArrayList;
import java.util.HashMap;

public class ElementNode extends Node {


    public ElementNode() {
        attrMap = new HashMap<>();
    }

    public ElementNode(String tagName){
        attrMap = new HashMap<>();
        this.tagName = tagName;
    }

    // utils
    public void addChild(Node child){
        children.add(child);
    }

    public ElementNode getChild(int index) {
        return (ElementNode) children.get(index);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }
    public void addAttr(String key, String value){
        attrMap.put(key, value);
    }



}
