package layout;

import style.StyledNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LayoutBox {
    Padding padding;
    Border border;
    Margin margin;
    int auto = 99999;
    int contentWidth = auto, contentHeight;
    int borderX, contentY;

    ArrayList<Dimension> widthProperties;
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
        widthProperties = new ArrayList<>();
        addWidthProperties();
    }

    public LayoutBox(StyledNode styledRoot, LayoutBox parent) {
        this();
        this.name = styledRoot.getName();
        this.parent = parent;
        buildTree(styledRoot);
    }

    private void addWidthProperties() {
        margin = new Margin();
        border = new Border();
        padding = new Padding();
        widthProperties.add(margin);
        widthProperties.add(border);
        widthProperties.add(padding);
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
        setValueOfProperties();
    }

    private void setValueOfProperties() {
        for (Dimension widthProperty : widthProperties) {
            setValueOfProperties(widthProperty);
        }
        String width = rules.get("width");
        if (width != null) {
            if (width.contains("px")) {
                String sub = width.substring(0, width.length() - 2);
                contentWidth = Integer.parseInt(sub);
            }
        }
    }




    private void setValueOfProperties(Dimension widthProperty) {
        String simpleValue = rules.get(widthProperty.getSimpleName());
        String leftValue = rules.get(widthProperty.getLeftName());
        String rightValue = rules.get(widthProperty.getRightName());
        String topValue = rules.get(widthProperty.getTopName());
        String bottomValue = rules.get(widthProperty.getBottomName());

        widthProperty.setValues(simpleValue, leftValue, rightValue);
        widthProperty.setTopAndBottomValues(simpleValue, topValue, bottomValue);
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
        computeContentWidth();

        computeBorderX();
        computeContentY();
        for (LayoutBox child : children) {
            child.computeLayout();
        }
    }

    private void computeBorderX() {
        borderX = parent.borderX + parent.border.left + parent.padding.left + margin.left;
    }

    private void computeContentY() {
        LayoutBox preSibling = getPreSibling();
        contentY += parent.contentY + margin.top + border.top + padding.top
                + preSibling.contentHeight +preSibling.padding.bottom+ preSibling.border.bottom
                + preSibling.margin.bottom + preSibling.padding.top + preSibling.border.top + preSibling.margin.top;
    }

    private LayoutBox getPreSibling() {
        int me = parent.children.indexOf(this);
        if (me > 0) {
            return parent.children.get(me - 1);
        }
        LayoutBox pre = new LayoutBox();

        return pre;
    }


    /**
     * 计算当前盒子的 contentWidth
     */
    private void computeContentWidth() {
        if (isOverFlow()) {
            if (contentWidth != auto && !margin.leftEqualAuto() && !margin.rightEqualAuto()) {
                margin.right = makeEqual();
            } else if (contentWidth != auto && margin.leftEqualAuto() && !margin.rightEqualAuto()) {
                margin.left = makeEqual();
            } else if (contentWidth != auto && !margin.leftEqualAuto() && margin.rightEqualAuto()) {
                margin.right = makeEqual();
            } else if (contentWidth != auto && margin.leftEqualAuto() && margin.rightEqualAuto()) {
                margin.left = makeEqual();
                margin.setRight(margin.left / 2);
                margin.setLeft(margin.right);
            } else if (contentWidth == auto) {
                if (margin.rightEqualAuto()) {
                    margin.setRight(0);
                }
                if (margin.leftEqualAuto()) {
                    margin.setLeft(0);
                }
                if (isOverFlow()) {
                    contentWidth = makeEqual();
                    if (contentWidth < 0){
                        contentWidth = 0;
                        margin.right = makeEqual();
                    }
                }

            }

        }

        // 清空auto
        for (Dimension widthProperty : widthProperties) {
            if (widthProperty.leftEqualAuto()) {
                widthProperty.left = 0;
            }
            if (widthProperty.rightEqualAuto()) {
                widthProperty.right = 0;
            }
        }
        if (contentWidth == auto){
            contentWidth = 0;
        }
    }

    private int makeEqual() {
        int total = getTotalWidth();
        return parent.contentWidth - total;
    }

    // 上下溢
    private boolean isOverFlow() {
        int totalWidth = getTotalWidth();

        return totalWidth != parent.contentWidth;
    }

    private int getTotalWidth() {
        int totalWidth = 0;
        for (Dimension widthProperty : widthProperties) {
            if (!widthProperty.leftEqualAuto()) {
                totalWidth += widthProperty.left;
            }
            if (!widthProperty.rightEqualAuto()) {
                totalWidth += widthProperty.right;
            }
        }
        if (contentWidth != auto) {
            totalWidth += contentWidth;
        }
        return totalWidth;
    }


    /**
     * 计算当前盒子的 contentHeight
     */
    private void computeContentHeight() {
        for (LayoutBox child : children) {
            child.computeContentHeight();
            contentHeight += child.contentHeight +
                    child.padding.top  + child.padding.bottom+
                    child.border.top + child.border.bottom+
                    child.margin.top + child.margin.bottom;
        }

        String height = rules.get("height");
        if (height != null) {
            if (height.contains("px")) {
                String sub = height.substring(0, height.length() - 2);
                contentHeight = Integer.parseInt(sub);
            }
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
        buffer.append("  ".repeat(Math.max(0, level)));
    }


    /*********************************************************
     *
     *********************************************************/
    public HashMap<String, String> getRules() {
        return rules;
    }

    public int getContentX(){
        return borderX + border.left;
    }

    public int getContentY(){
        return contentY;
    }

    public int getBorderX() {
        return borderX;
    }

    public int getBorderY(){
        return contentY - padding.top - border.top;
    }

    public int getBorderBoxWidth(){
        return contentWidth + border.left + border.right + padding.left + padding.right;
    }

    public int getBorderBoxHeight(){
        return contentHeight + border.top + border.bottom + padding.top + padding.bottom;
    }

    public Border getBorder() {
        return border;
    }


    public int getBorderTop(){
        return border.top;
    }

    public int getBorderBottom(){
        return border.bottom;
    }

    public int getBorderLeft(){
        return border.left;
    }

    public int getBorderRight(){
        return border.right;
    }

    public int getPaddingBoxWidth(){
        return contentWidth + padding.left + padding.right;
    }

    public int getPaddingBoxHeight(){
        return contentHeight + padding.top + padding.bottom;
    }

    public ArrayList<LayoutBox> getChildren() {
        return children;
    }
}
