package css;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class Stylesheet {

//    private LinkedHashMap<String, CSSSelector> selectors;
    private ArrayList<CSSSelector> selectors;

    public Stylesheet() {
        selectors = new ArrayList<>();
    }

    public void addSelector(CSSSelector selector) {
        selectors.add(selector);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (CSSSelector selector : selectors) {
            buffer.append(selector.toString());
        }
        buffer.delete(buffer.length() - 1, buffer.length());
        return buffer.toString();
    }
}
