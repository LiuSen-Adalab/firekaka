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
    private String abc;

    public Node parse(String input) {
        this.input = input;
        String leftReg = "<(\\w+?)\\b.*?>";
        String rightReg = "</\\w+?>";

        Pattern leftPattern = Pattern.compile(leftReg);
        Pattern rightPattern = Pattern.compile(rightReg);

        Matcher preLeftMatcher = leftPattern.matcher(input);
        Matcher leftMatcher = leftPattern.matcher(input);
        Matcher rightMatcher = rightPattern.matcher(input);


        finder(preLeftMatcher, leftMatcher, rightMatcher, 0);

        return parentNodes.get(1);
    }

    private void finder(Matcher preLeft, Matcher left, Matcher right, int curLevel) {
        right.find();
        if (left.find()) {
            ElementNode root = new ElementNode();
            parentNodes.put(0, root);
        }
        while (preLeft.find()) {
            int continuousRightCount = -1;
            while (preLeft.start() > right.start()) {
                right.find();
                continuousRightCount += 1;
            }
            curLevel -= continuousRightCount;

            ElementNode node = new ElementNode(preLeft.group(1));
            parentNodes.put(curLevel, node);
            parentNodes.get(curLevel - 1).addChild(node);

            setAttr(node, preLeft.start(), preLeft.end());
            if (left.find()) {
                findTextNode(node, preLeft.end(), left.start());
            }else {
                findTextNode(node, preLeft.end(), input.length() - 1);
            }

        }
    }

    private void findTextNode(ElementNode node, int start, int end) {
        String text = input.substring(start-1, end);
        String textReg = ">(.+?)</";
        Pattern textPattern = Pattern.compile(textReg);
        Matcher textMatcher = textPattern.matcher(text);

        if (textMatcher.find()) {
            TextNode textNode = new TextNode(textMatcher.group(1).trim());
            node.addChild(textNode);
        }
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
