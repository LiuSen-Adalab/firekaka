package layout;

import style.StyledNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LayoutBox {
    int contentWidth, contentHeight, padding, margin, borderWidth;
    int borderX, contentY;
    ArrayList<LayoutBox> children;
    LayoutBox parent;
    HashMap<String, String> rules;

    String name;


    public LayoutBox(StyledNode styledRoot) {
        children = new ArrayList<>();
        rules = new LinkedHashMap<>();
        name = styledRoot.getName();

        buildTree(styledRoot);
    }

    private void buildTree(StyledNode styledRoot) {
        buildThisNode(styledRoot);
        buildChildren(styledRoot);
    }

    private void buildChildren(StyledNode styledRoot) {
        for (StyledNode styleChild : styledRoot.getChildren()) {
            if (styleChild.getName() != null) {
                children.add(new LayoutBox(styleChild));
            }
        }
    }

    private void buildThisNode(StyledNode styledRoot) {
        rules.putAll(styledRoot.getMergedDeclarations());
    }

    public void layoutTree(int viewPOrtWidth) {

    }




    private void computeBorderX() {
        //todo

    }

    private void computeContentY() {
        //todo
    }


    /**
     * 计算当前盒子的 contentWidth
     */
    private void computeContentWidth() {
    }

    /**
     * 计算当前盒子的 contentHeight
     */
    private void computeHeight() {
        for (LayoutBox child : children) {
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buildString(buffer, 0);

        return buffer.toString();
    }

    private void buildString(StringBuffer buffer, int level) {
        appendIndentTo(buffer, level);
        appendHeadTo(buffer);
        appendChildrenTo(buffer, level);
        appendIndentTo(buffer, level);
        appendTailTo(buffer);
    }

    private void appendHeadTo(StringBuffer buffer) {
        buffer.append("<").append(name);
        appendLayoutInfo(buffer);
        buffer.append(">\n");
    }

    private void appendLayoutInfo(StringBuffer buffer) {
        buffer.append(" ").append("borderX").append("=");
        buffer.append("\"").append(borderX).append("\"");

        buffer.append(" ").append("contentY").append("=");
        buffer.append("\"").append(contentY).append("\"");
    }

    private void appendChildrenTo(StringBuffer buffer, int level) {
        for (LayoutBox child : children) {
            child.buildString(buffer, level + 1);
        }
    }

    private void appendTailTo(StringBuffer buffer) {
        buffer.append("</").append(name).append(">\n");
    }

    private void appendIndentTo(StringBuffer buffer, int level) {
        for (int i = 0; i < level; i++) {
            buffer.append("  ");
        }
    }
}
