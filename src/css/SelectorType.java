package css;

enum SelectorType {
    id, tag, clazz, tag_clazz;

    public int getPriority() {
        if (this == id) {
            return 100;
        } else if (this == tag) {
            return 1;
        } else if (this == clazz) {
            return 10;
        }
        return 0;
    }
}
