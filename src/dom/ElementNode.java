package dom;

import java.util.ArrayList;
import java.util.HashMap;

public class ElementNode extends Node {
    String tagName;
    HashMap<String, String> AttrMap;

    public ElementNode() {
        AttrMap = new HashMap<>();
    }

    public ElementNode(String name){
        tagName = name;
    }

    public ElementNode(String name, ArrayList<Node> children){
        this.children = children;
        tagName = name;
    }
}
