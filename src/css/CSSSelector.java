package css;

import java.util.ArrayList;

public class CSSSelector {

    private final String name;
    private  SelectorType type = null;
    ArrayList<CssDeclare> declareBlocks;
    int outputIndex = -1;

    public CSSSelector(String name) {
        this.declareBlocks = new ArrayList<>();
        this.name = name;
        if (name.toCharArray()[0] == '.'){
            type = SelectorType.clazz;
        }
    }

    public String getName() {
        return name;
    }

    public SelectorType getType() {
        return type;
    }

    public void addDeclare(CssDeclare declare){
        declareBlocks.add(declare);
        declare.addSelector(this);
    }

    @Override
    public String toString() {
//        outputIndex += 1;
        return declareBlocks.get(0).toString();
    }


}
