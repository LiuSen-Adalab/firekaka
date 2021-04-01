package style;

import css.DeclareBlock;
import css.Selector;
import css.Stylesheet;
import dom.Node;

import java.util.*;
import java.util.function.BiConsumer;

public class StyledNode {
    HashMap<String, String> attributes;
    ArrayList<StyledNode> children;
    HashMap<String, String> selectorNameOfThisNode;

    String name;
    Stylesheet stylesheet;
    private HashMap<String, String> allSelectorNameOfThisNode;


    public StyledNode(Node domNode, Stylesheet stylesheet) {
        attributes = new LinkedHashMap<>();
        selectorNameOfThisNode = new HashMap<>();
        attributes.putAll(domNode.getAttributes());
        children = new ArrayList<>();
        this.stylesheet = stylesheet;
        this.name = domNode.getTagName();

        allSelectorNameOfThisNode = getAllSimpleSelectorName();
        buildTree(domNode);
    }

    public void setAttributes(HashMap<String, String> attributesOfDOMNode) {
        attributesOfDOMNode.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                if (s.equals("class")) {
                    String[] classes = s2.split(" ");
                    for (String aClass : classes) {
                        attributes.put(s, aClass);
                    }
                }
            }
        });
    }

    private void buildTree(Node domNode) {
        for (Node child : domNode.getChildren()) {
            children.add(new StyledNode(child, stylesheet));
        }
    }


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

    /**
     * @return 当前节点的所有基本选择器名字，包括标签名（带前缀符号，如“＃id”）
     */
    private HashMap<String, String> getAllSimpleSelectorName() {
        HashMap<String, String> selectorNames = new HashMap<>();
        attributes.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String type, String s2) {
                if (type.equals("class")) {
                    String[] classes = s2.split(" ");
                    for (String aClass : classes) {
                        selectorNames.put(transformToSelectorName(type, aClass), type);
                    }
                }else {
                    selectorNames.put(transformToSelectorName(type, s2), type);
                }
            }
        });
        selectorNames.put(name, "tag");
        return selectorNames;
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
    private String allDeclarationToString() {
        StringBuffer buffer = new StringBuffer();
        HashMap<String, String> mergedDeclarations =
                mergeAllBlock(getAllMatchSelector());

        formatDeclarationString(buffer, mergedDeclarations);

        return buffer.toString();
    }

    private void formatDeclarationString(StringBuffer buffer, HashMap<String, String> mergedDeclarations) {
        mergedDeclarations.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                buffer.append(" ").append(s).append("=");
                buffer.append("\"").append(s2).append("\"");
            }
        });
    }

    /**
     * 合并传入的所有selector的所有declaratioformatDeclarationStringns，按优先级从高到低和出现先后顺序排列
     * @return declarations 的 linkedHashMap
     */
    private HashMap<String, String> mergeAllBlock(ArrayList<Selector> selectors) {
        HashMap<String, String> declarations = new LinkedHashMap<>();
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
     * @return 返回匹配当前节点的所有selector，并且列表按selector优先级从高到低排序
     */
    private ArrayList<Selector> getAllMatchSelector() {
        ArrayList<Selector> matchedSelectors = new ArrayList<>();
        ArrayList<Selector> selectorsInSheet = stylesheet.getSelectors();
        int appear = 0;
        for (Selector selector : selectorsInSheet) {
            if (isMatchThisNode(selector)) {
                matchedSelectors.add(selector);
            }
        }
        Collections.reverse(matchedSelectors);  //为了确保后出现的同名选择器可以在排序操作排在前面，要先反转列表
        Collections.sort(matchedSelectors);     // 只按优先级排序
        return matchedSelectors;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buildString(buffer, 0);

        return buffer.toString();
    }

    private void buildString(StringBuffer buffer, int level) {
        if (name == null) {
            addIndent(buffer, level);
            buffer.append("<text></text>\n");
            return;
        }
        appendTagHead(buffer, level);
        appendChildren(buffer, level);
        appendTagTail(buffer, level);
    }

    private void appendTagTail(StringBuffer buffer, int level) {
        addIndent(buffer, level);
        buffer.append("</").append(name).append(">\n");
    }

    private void appendChildren(StringBuffer buffer, int level) {
        for (StyledNode child : children) {
            child.buildString(buffer, level + 1);
        }
    }

    private void appendTagHead(StringBuffer buffer, int level) {
        addIndent(buffer, level);
        buffer.append("<").append(name);
        buffer.append(allDeclarationToString()).append(">\n");
    }

    private void addIndent(StringBuffer buffer, int level) {
        for (int i = 0; i < level; i++) {
            buffer.append("  ");
        }
    }
}

