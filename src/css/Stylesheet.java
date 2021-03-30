package css;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class Stylesheet {

    LinkedHashMap<String, CSSSelector> selectors;

    public Stylesheet() {
        selectors = new LinkedHashMap<>();
    }

    public void addSelector(CSSSelector selector) {
        selectors.put(selector.getName(), selector);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        selectors.forEach(new BiConsumer<String, CSSSelector>() {
            @Override
            public void accept(String s, CSSSelector selector) {
                buffer.append(selector.toString());
//                buffer.append("\n");
            }
        });
//        buffer.delete(buffer.length() - 1, buffer.length());
        return buffer.toString();
    }
}
