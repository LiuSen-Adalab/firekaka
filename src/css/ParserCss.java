package css;

public class ParserCss {
    private Stylesheet stylesheet;

    public Stylesheet parse(String input) {
        stylesheet = new Stylesheet();
        parseAllBlock(input);
        return stylesheet;
    }


    private void parseAllBlock(String input) {
        String[] bigBlocks = cutIntoSelectorAndBlock(input);
        for (String bigBlock : bigBlocks) {
            parseBlock(bigBlock);
        }
    }

    private void parseBlock(String bigBlock){
        String[] separatedBlock = bigBlock.split("\\{");
            DeclareBlock block = buildSelectors(separatedBlock[0]);
        if (separatedBlock.length > 1) {
            buildDeclarationBlock(block, separatedBlock[1]);
        }
    }

    private void buildDeclarationBlock(DeclareBlock block, String input) {
        String[] declarations = getDeclarationBlock(input);
        for (String declaration : declarations) {
            String[] d = declaration.split(":");
            block.addDeclare(d[0].trim(), d[1].trim());
        }
        stylesheet.addBlock(block);
    }


    private DeclareBlock buildSelectors(String selectorsStr) {
        String[] selectors = selectorsStr.split(",");
        DeclareBlock block = new DeclareBlock();
        for (String selectorName : selectors) {
            Selector selector = new Selector(selectorName.trim());
            selector.setDeclarationBlock(block);
            block.addSelector(selector);
            stylesheet.addSelector(selector);
        }
        return block;
    }


    private String[] cutIntoSelectorAndBlock(String input) {
        input = input.trim().substring(0, input.trim().length() - 1);
        return input.split("}");
    }

    private String[] getDeclarationBlock(String declarations) {
        declarations = declarations.trim().substring(0, declarations.trim().length() - 1);
        return declarations.split(";");
    }
}

