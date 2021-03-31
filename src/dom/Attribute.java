package dom;

public class Attribute implements Comparable<Attribute> {
    int priority;
    String value;
    String type;
    String selectorName;

    public Attribute(String type, String value) {
        this.value = value;
        this.type = type;
        setPriority(type);
    }

    private void setPriority(String type) {
        if (type.equals("id")) {
            priority = 100;
        } else if (type.equals("class")) {
            priority = 10;
        } else if (type.equals("tag")) {
            priority = 1;
        } else if (type.equals("tag_class")) {
            priority = 11;
        }
    }

    public String getSelectorName() {
        StringBuffer buffer = new StringBuffer(value);
        if (type.equals("id")) {
            selectorName = buffer.insert(0, "#").toString();
        } else if (type.equals("class")) {
            selectorName = buffer.insert(0, ".").toString();
        } else if (type.equals("tag")) {
            selectorName = value;
        } else if (type.equals("tag_class")) {
            selectorName = value;
        } else {
            selectorName = "unknown";
        }

        return selectorName;
    }


    @Override
    public int compareTo(Attribute o) {
        return o.priority - priority;
    }
}
