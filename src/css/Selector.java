package css;

import java.util.ArrayList;

public class Selector {

    private final String name;
    private  SelectorType type = null;
    private DeclareBlock declareBlock;

    public Selector(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDeclareBlock(DeclareBlock declareBlock){
        this.declareBlock = declareBlock;
        declareBlock.addSelector(this);
    }

    public DeclareBlock getDeclareBlock() {
        return declareBlock;
    }

    @Override
    public String toString() {
        return declareBlock.toString();
    }


}
