package layout;

public class Border extends Dimension{

    Border(){
        simpleName = "border-width";
        leftName = "border-left-width";
        rightName = "border-right-width";
        topName = "border-top-width";
        bottomName = "border-bottom-width";
    }

    public int getTop(){
        return top;
    }

    public int getBottom(){
        return bottom;
    }
}
