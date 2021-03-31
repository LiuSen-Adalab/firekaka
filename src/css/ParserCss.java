package css;

import java.math.BigDecimal;

public class ParserCss {
    private Stylesheet stylesheet;

    public Stylesheet parse(String input) {
        stylesheet = new Stylesheet();
        char[] inputChars = input.trim().toCharArray();
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

        DeclareBlock declarationBlock = parseSelector(buffer.toString().trim());
        buffer.delete(0, buffer.length());

        buffer.append(inputChars[index]);
        do {
            index += 1;
            buffer.append(inputChars[index]);
        }
        while (inputChars[index] != '}');

        buffer.delete(0, 1);
        buffer.delete(buffer.length() - 1, buffer.length());
        parseDeclarations(declarationBlock, buffer.toString().trim());

        decomposeInput(inputChars, index + 1);
    }

    private DeclareBlock parseSelector(String selectorsStr) {
        String[] selectors = selectorsStr.split(",");
        DeclareBlock declaration = new DeclareBlock();
        for (String select : selectors) {
            Selector selector = new Selector(select.trim());
            selector.setDeclareBlock(declaration);
            stylesheet.addSelector(selector);
        }
        return declaration;
    }

    private void parseDeclarations(DeclareBlock block, String inputStr) {
        if (inputStr.length() == 0) {
            return;
        }
        inputStr = inputStr.trim().substring(0, inputStr.length() - 1);
        String[] declarationsArray = inputStr.split(";");
        for (String decStr : declarationsArray) {
            String[] keyAndValue = decStr.trim().split(":");
            block.addDeclare(keyAndValue[0].trim(), keyAndValue[1].trim());
        }
    }

}

