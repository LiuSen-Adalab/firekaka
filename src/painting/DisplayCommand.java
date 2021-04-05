package painting;

import java.awt.*;

public class DisplayCommand {
    int startX, startY;
    int width, height;
    int color;


    public void execute(Graphics graphics){

    }

    private void checkBoundary(Graphics graphics){
        //todo
    }

    public void setStartPoint(int startX, int startY){
        this.startX = startX;
        this.startY = startY;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setColor(int color){
        this.color = color;
    }
}
