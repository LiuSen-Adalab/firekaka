package html;

import dom.ElementNode;
import dom.Node;
import dom.TextNode;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {
    HashMap<Integer, ElementNode> parentNodes = new HashMap<>();
    String input;

    public HTMLParser(String html) {
        input = html;
    }

    public Node parse() {
        return parse(input);
    }

    public HTMLParser(){

    }

    public Node parse(String input) {
        this.input = input;
        String leftReg = "<(\\w+?)\\b.*?>";
        String rightReg = "</\\w+?>";

        Pattern leftPattern = Pattern.compile(leftReg);
        Pattern rightPattern = Pattern.compile(rightReg);

        Matcher leftMatcher = leftPattern.matcher(input);
        Matcher rightMatcher = rightPattern.matcher(input);

        rightMatcher.find();
        parentNodes.put(0, new ElementNode());

        buildDomTree(leftMatcher, rightMatcher, 0);

        return parentNodes.get(1);
    }

    private void buildDomTree(Matcher left, Matcher right, int lastLevel) {
        if (!left.find()) {
            return;
        }
        int curLevel = getLevel(left, right, lastLevel);     // 获取当前读取节点的等级
        ElementNode node = setupNode(left, curLevel);        // 建立当前节点，并将自己加入父节点的子列表中
        setAttr(node, left.start(), left.end());             // 设置节点的属性
        findTextNode(node, left);                            // 寻找当前节点是否包含文字节点

        buildDomTree(left, right, curLevel);                 // 递归搜索输入字符串，建立节点
    }

    private void findTextNode(ElementNode node, Matcher left) {
        String textReg = left.group(0) + "(.+?)" + "</";
        Pattern textPattern = Pattern.compile(textReg);
        Matcher textMatcher = textPattern.matcher(input);

        if (textMatcher.find(left.start())) {
            TextNode textNode = new TextNode(textMatcher.group(1));
            node.addChild(textNode);
        }
    }

    private ElementNode setupNode(Matcher left, int curLevel) {
        ElementNode node = new ElementNode(left.group(1));
        parentNodes.put(curLevel, node);
        parentNodes.get(curLevel - 1).addChild(node);
        return node;
    }

    private int getLevel(Matcher left, Matcher right, int lastLevel) {
        int count = 0;
        while (right.start() < left.start()) {
            right.find();
            count += 1;
        }

        return lastLevel - count + 1;
    }

    private void setAttr(ElementNode node, int start, int end) {
        String subString = input.substring(start, end);

        String attrReg = "\\b(\\w+)=\"(.*?)\"";
        Pattern attrPattern = Pattern.compile(attrReg);
        Matcher attrMatcher = attrPattern.matcher(subString);

        while (attrMatcher.find()) {
            node.addAttr(attrMatcher.group(1), attrMatcher.group(2));
        }
    }


}
