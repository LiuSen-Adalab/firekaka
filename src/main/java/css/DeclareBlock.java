package css;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class DeclareBlock {

    private final LinkedHashMap<String, String> declarations;
    HashMap<String, Selector> selectors;
    boolean hasOutput = false;

    DeclareBlock(){
        declarations = new LinkedHashMap<>();
        selectors = new LinkedHashMap<>();
    }

    public HashMap<String, String> getDeclarations() {
        return declarations;
    }

    public void addDeclare(String property, String value){
        declarations.put(property, value);
    }

    public void addSelector(Selector selector){
        selectors.put(selector.getName(), selector);
    }



    @Override
    public String toString() {
        if(hasOutput){
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        selectors.forEach(new BiConsumer<String, Selector>() {
            @Override
            public void accept(String s, Selector selector) {
                buffer.append(selector.getName()).append(", ");
            }
        });
        buffer.delete(buffer.length() - 2, buffer.length());

        buffer.append(" {\n");

        declarations.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String property, String value) {
             buffer.append("  ");
             buffer.append(property.trim()).append(": ");
             buffer.append(value.trim()).append(";\n");
            }
        });

        buffer.append("}\n\n");

        hasOutput = true;
        return buffer.toString();
    }
}
