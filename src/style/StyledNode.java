package style;

import css.DeclareBlock;
import css.Selector;
import css.Stylesheet;
import dom.Node;

import java.util.*;
import java.util.function.BiConsumer;

public class StyledNode {
    ArrayList<StyledNode> children;      // 子节点
    private HashMap<String, String> allSelectorNameOfThisNode; // 当前节点的所有基本选择器名字，为查询方便用map

    String name;
    Stylesheet stylesheet;

    public StyledNode(Node domNode, Stylesheet stylesheet) {
        children = new ArrayList<>();
        this.stylesheet = stylesheet;
        this.name = domNode.getTagName();
        setSelectorNames(domNode);
        buildTree(domNode);
    }

    private void setSelectorNames(Node domNode) {
        allSelectorNameOfThisNode = getAllSimpleSelectorName(domNode.getAttributes());
    }


    private void buildTree(Node domNode) {
        for (Node child : domNode.getChildren()) {
            children.add(new StyledNode(child, stylesheet));
        }
    }


    /**
     * @return 当前节点的所有基本选择器名字，包括标签名（带前缀符号，如“＃id”）
     */
    private HashMap<String, String> getAllSimpleSelectorName(HashMap<String, String> attributes) {
        HashMap<String, String> selectorNames = new HashMap<>();
        attributes.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String type, String selectorName) {
                if (type.equals("class")) {
                    String[] classes = selectorName.split(" ");
                    for (String aClass : classes) {
                        selectorNames.put(transformToSelectorName(type, aClass), type);
                    }
                } else {
                    selectorNames.put(transformToSelectorName(type, selectorName), type);
                }
            }
        });
        selectorNames.put(name, "tag");
        return selectorNames;
    }

    /*
     * **************************************************************************
     * toString
     * ***************************************************************************/
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buildString(buffer, 0);

        return buffer.toString();
    }

    private void buildString(StringBuffer buffer, int level) {
        if (!isTestNode(buffer, level)) {
            appendTagHead(buffer, level);
            appendChildren(buffer, level);
            appendTagTail(buffer, level);
        }
    }
    /*
     * ***************************************************************
     * toString helper function
     * ****************************************************************/

    private boolean isTestNode(StringBuffer buffer, int level) {
        if (name == null) {
            addIndent(buffer, level);
            buffer.append("<text></text>\n");
            return true;
        }
        return false;
    }

    private void appendTagHead(StringBuffer buffer, int level) {
        addIndent(buffer, level);
        buffer.append("<").append(name);
        appendDeclarations(buffer);
        buffer.append(">\n");
    }

    private void appendChildren(StringBuffer buffer, int level) {
        for (StyledNode child : children) {
            child.buildString(buffer, level + 1);
        }
    }

    private void appendTagTail(StringBuffer buffer, int level) {
        addIndent(buffer, level);
        buffer.append("</").append(name).append(">\n");
    }

    /*
     * **********************************************************************
     * style parser
     * ***********************************************************************/

    /**
     * 判断选择器是否能匹配当前节点
     */
    private boolean isMatchThisNode(Selector selector) {
        String[] splitSelectorNames = splitSelector(selector);
        for (String splitSelectorName : splitSelectorNames) {
            if (allSelectorNameOfThisNode.get(splitSelectorName) == null) {
                return false;
            }
        }
        return true;
    }


    private String transformToSelectorName(String type, String value) {
        if (type.equals("class")) {
            return "." + value;
        } else if (type.equals("id")) {
            return "#" + value;
        }
        return value;
    }


    /**
     * 切分一个组合的选择器
     *
     * @param selector 从StyleSheet得到的selector
     * @return 切分的选择器的名字列表
     */
    private String[] splitSelector(Selector selector) {
        StringBuffer buffer = new StringBuffer();
        for (char ch : selector.getName().toCharArray()) {
            if (ch == '.' || ch == '#') {
                buffer.append("$");
            }
            buffer.append(ch);
        }
        if (buffer.indexOf("$") == 0) {
            buffer.delete(0, 1);
        }
        return buffer.toString().split("\\$");
    }


    /**
     * @return 返回当前节点的所有CSS规则，并格式化为一行，以空格分隔
     * (如果长度不为0，则字符串最前面带一个空格）
     */
    private void appendDeclarations(StringBuffer buffer) {
        HashMap<String, String> mergedDeclarations =
                mergeAllBlock(getAllMatchSelector());

        mergedDeclarations.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                buffer.append(" ").append(s).append("=");
                buffer.append("\"").append(s2).append("\"");
            }
        });
    }


    /**
     * 合并传入的所有selector的所有declarations，按优先级从高到低和出现先后顺序排列
     *
     * @return declarations 的 linkedHashMap
     */
    private HashMap<String, String> mergeAllBlock(ArrayList<Selector> selectors) {
        HashMap<String, String> declarations = new LinkedHashMap<>();
        for (int i = 0; i < selectors.size() - 1; i++) {
            if (selectors.get(i).getPriority() == selectors.get(i + 1).getPriority()) {
                DeclareBlock block1 = selectors.get(i).getDeclareBlock();
                DeclareBlock block2 = selectors.get(i + 1).getDeclareBlock();
                block2.getDeclarations().forEach(new BiConsumer<String, String>() {
                    @Override
                    public void accept(String s, String s2) {
                        block1.getDeclarations().remove(s);
                    }
                });
            }
        }
        for (Selector selector : selectors) {
            mergeDeclarations(declarations, selector.getDeclareBlock());
        }
        return declarations;
    }

    private void mergeDeclarations(HashMap<String, String> declarations, DeclareBlock block) {
        block.getDeclarations().forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                declarations.putIfAbsent(s, s2);
            }
        });

    }

    /**
     * @return 返回匹配当前节点的所有selector，列表按selector优先级从高到低排序
     */
    private ArrayList<Selector> getAllMatchSelector() {
        ArrayList<Selector> matchedSelectors = new ArrayList<>();
        ArrayList<Selector> selectorsInSheet = stylesheet.getSelectors();

        for (Selector selector : selectorsInSheet) {
            if (isMatchThisNode(selector)) {
                matchedSelectors.add(selector);
            }
        }
        Collections.sort(matchedSelectors);     // 按优先级和出现顺序排列
        return matchedSelectors;
    }

    private void addIndent(StringBuffer buffer, int level) {
        for (int i = 0; i < level; i++) {
            buffer.append("  ");
        }
    }
}

