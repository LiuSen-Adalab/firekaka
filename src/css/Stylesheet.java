package css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class Stylesheet {

    private final ArrayList<Selector> selectors;
    private final HashMap<String, HashMap<String, String>> mergedDeclarations;
    boolean collected;

    public Stylesheet() {
        selectors = new ArrayList<>();
        mergedDeclarations = new LinkedHashMap<>();
    }

    public void addSelector(Selector selector) {
        selectors.add(selector);
        mergedDeclarations.put(selector.getName(), new LinkedHashMap<>());
    }

    /**
     * 收集所有选择器的所有css规则，同名的选择器会合并其规则
     */
    private void collectAllDeclarations() {
        if (collected){
            return;
        }
        for (Selector selector : selectors) {
//            mergedDeclarations.get(selector.getName())
//                    .putAll(selector.getDeclareBlock().getDeclarations());
            selector.getDeclareBlock().getDeclarations().forEach(new BiConsumer<String, String>() {
                @Override
                public void accept(String s, String s2) {
                    mergedDeclarations.get(selector.getName()).remove(s);
                    mergedDeclarations.get(selector.getName()).put(s, s2);
                }
            });
        }
        collected =true;
    }

    /**
     * @param selectorName 选择器的名字
     * @return 该同名选择器的所有规则
     */
    public HashMap<String, String> getDeclarations(String selectorName){
        collectAllDeclarations();
        return mergedDeclarations.get(selectorName);
    }


    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (Selector selector : selectors) {
            buffer.append(selector.toString());
        }
        buffer.delete(buffer.length() - 1, buffer.length());
        return buffer.toString();
    }
}
