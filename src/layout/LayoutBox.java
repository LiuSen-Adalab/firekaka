package layout;

import style.StyledNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LayoutBox {
    int contentWidth, contentHeight, padding, margin, borderWidth;
    int borderX, contentY;
    ArrayList<LayoutBox> children;
    HashMap<String, String> rules;

    LayoutBox parent;
    String name;


    /************************************************
     * initialize this tree
     ************************************************/
    public LayoutBox(StyledNode styledRoot) {
        this();
        parent = new LayoutBox();

        name = styledRoot.getName();
        buildTree(styledRoot);
    }

    private LayoutBox() {
        children = new ArrayList<>();
        rules = new LinkedHashMap<>();
    }

    public LayoutBox(StyledNode styledRoot, LayoutBox parent) {
        this();
        this.name = styledRoot.getName();
        this.parent = parent;
        computeContentWidth();
        buildTree(styledRoot);
    }

    private void buildTree(StyledNode styledRoot) {
        buildThisNode(styledRoot);
        buildChildren(styledRoot);
    }

    private void buildChildren(StyledNode styledRoot) {
        for (StyledNode styleChild : styledRoot.getChildren()) {
            if (styleChild.getName() != null) {
                children.add(new LayoutBox(styleChild, this));
            }
        }
    }

    private void buildThisNode(StyledNode styledRoot) {
        rules.putAll(styledRoot.getMergedDeclarations());
        parseRules();
    }

    private void parseRules() {
        padding = findRule("padding");
        margin = findRule("margin");
        borderWidth = findRule("border");
    }

    private int findRule(String property) {
        if (rules.get(property) != null) {
            String a = rules.get(property);
            String b = a.substring(0, a.length() - 2);
            return Integer.parseInt(b);
        }
        return 0;
    }

    /******************************************************************************
     * compute layout of the tree
     ******************************************************************************/

    public void layoutTree(int viewPOrtWidth) {
        parent.contentWidth = viewPOrtWidth;

        computeContentHeight();
        computeLayout();
    }

    private void computeLayout() {
        computeBorderX();
        computeContentY();
        for (LayoutBox child : children) {
            child.computeLayout();
        }
    }

    private void computeBorderX() {
        borderX = parent.borderX + parent.padding + margin;
    }

    private void computeContentY() {
        LayoutBox preSibling = getPreSibling();
        contentY += parent.contentY + margin + borderWidth + padding
        + preSibling.contentHeight  + preSibling.borderWidth+ preSibling.margin;
    }

    private LayoutBox getPreSibling() {
        int me = parent.children.indexOf(this);
        if (me > 0) {
            return parent.children.get(me - 1);
        }
        LayoutBox pre = new LayoutBox();
        pre.contentY = parent.contentY + parent.padding;
        return pre;
    }


    /**
     * 计算当前盒子的 contentWidth
     */
    private void computeContentWidth() {
        contentWidth = parent.contentWidth - padding * 2 - margin * 2 - borderWidth * 2;
    }

    /**
     * 计算当前盒子的 contentHeight
     */
    private void computeContentHeight() {
        for (LayoutBox child : children) {
            child.computeContentHeight();
            contentHeight += child.contentHeight +
                    child.padding * 2 +
                    child.borderWidth * 2 +
                    child.margin * 2;
        }
    }


    /**************************************************************************************
     * toString
     **************************************************************************************/

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
