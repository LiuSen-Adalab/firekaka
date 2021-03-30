package css;

import java.math.BigDecimal;

public class CSSParser {
    private Stylesheet stylesheet;

    public Stylesheet parse(String input) {
        stylesheet = new Stylesheet();
        char[] inputChars = input.toCharArray();
        decomposeInput(inputChars, 0);

        return stylesheet;
    }


    private void decomposeInput(char[] inputChars, int index) {
        if (index == inputChars.length) {
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (inputChars[index] != '{') {
            buffer.append(inputChars[index]);
            index += 1;
        }
        CssDeclare declarationBlock = parseSelector(buffer.toString().trim());
        buffer.delete(0, buffer.length());

        do {
            buffer.append(inputChars[index]);
            index += 1;
        }
        while (inputChars[index] != '}');
        buffer.append(inputChars[index]);
        index +=1;
        buffer.delete(0, 1);
        buffer.delete(buffer.length() - 1, buffer.length());
        buffer.delete(buffer.lastIndexOf(";"), buffer.lastIndexOf(";") + 1);
        parseDeclarations(declarationBlock, buffer.toString().trim());

        decomposeInput(inputChars, index);

    }

    private CssDeclare parseSelector(String selectorsStr) {
        String[] selectors = selectorsStr.split(",");
        CssDeclare declaration = new CssDeclare();
        for (String select : selectors) {
            CSSSelector selector = new CSSSelector(select);
            selector.addDeclare(declaration);
            stylesheet.addSelector(selector);
        }
        return declaration;
    }

    private void parseDeclarations(CssDeclare block, String inputStr) {
        String[] declarationsArray = inputStr.split(";");
        for (String decStr : declarationsArray) {
            String[] keyAndValue = decStr.trim().split(": ");
            if (isNumber(keyAndValue[1])) {
                String subStr = keyAndValue[1].substring(0, keyAndValue[1].length() - 2);
                keyAndValue[1] = String.valueOf(Math.round(Double.parseDouble(subStr))) + "px";
            }
            block.addDeclare(keyAndValue[0], keyAndValue[1]);
        }
    }

    private boolean isNumber(String str){
        try {
            new BigDecimal(str.substring(0, str.length() - 3));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}

