package layout;

public abstract class Dimension {
    int simple, left , right, top, bottom;
    String simpleName, leftName, rightName, topName, bottomName;
    int auto = 99999;


    public void setValues(String simpleValue, String left, String right){
        if (simpleValue != null) {
            simple = toPx(simpleValue);
            this.left = simple;
            this.right = simple;
        }
        if (left != null){
            this.left = toPx(left);
        }
        if (right != null) {
            this.right = toPx(right);
        }
    }

    public void setTopAndBottomValues(String simpleValue, String top, String bottom){
        if (simpleValue != null) {
            simple = toPx(simpleValue);
            this.top = simple;
            this.bottom = simple;
        }
        if (top != null){
            this.top = toPx(top);
        }
        if (bottom != null) {
            this.bottom = toPx(bottom);
        }

        if (this.top == auto){
            this.top = 0;
        }
        if (this.bottom == auto){
            this.bottom = 0;
        }
    }

    public int toPx(String valueStr){
        int value = 0;
        if (valueStr.contains("px")){
            String valueInt = valueStr.substring(0, valueStr.length() - 2);
            value  = Integer.parseInt(valueInt);
        }else if (valueStr.equals("auto")){
            value = auto;
        }
        return value;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public boolean notAuto(){
        return left != auto && right != auto;
    }

    public boolean rightEqualAuto(){
        return right == auto;
    }

    public boolean leftEqualAuto(){
        return left == auto;
    }

    public int getSimple() {
        return simple;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getLeftName() {
        return leftName;
    }

    public String getRightName() {
        return rightName;
    }

    public String getTopName() {
        return topName;
    }

    public String getBottomName() {
        return bottomName;
    }
}
