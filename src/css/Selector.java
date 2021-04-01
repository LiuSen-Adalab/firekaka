package css;

public class Selector implements Comparable<Selector>{

    private final String name; //name contains "#"/"." ...
    private DeclareBlock declareBlock;
    private int priority = 0;

    public Selector(String name) {
        this.name = name;
        setPriority();
    }

    public String getName() {
        return name;
    }

    private void setPriority() {
        String[] classCount = name.split("\\.");
        String[] idCount = name.split("#");
        priority += (classCount.length - 1) * 10;
        priority += (idCount.length - 1) * 100;

        if (!classCount[0].equals("") && !idCount[0].equals("")) {
            priority += 1;
        }
    }

    public DeclareBlock getDeclareBlock() {
        return declareBlock;
    }

    @Override
    public String toString() {
        return declareBlock.toString();
    }

    @Override
    public int compareTo(Selector other) {
        return other.priority - priority;
    }

    public void setDeclarationBlock(DeclareBlock block) {
        this.declareBlock = block;
    }
}
