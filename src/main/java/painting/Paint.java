package painting;

import layout.Dimension;
import layout.LayoutBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Paint {
    ArrayList<DisplayCommand> commands;
    int canvasWidth, canvasHeight;

    public Paint(){
        commands = new ArrayList<>();
    }

    public BufferedImage paint(LayoutBox layoutBoxRoot, int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        generateCommands(layoutBoxRoot);

        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, canvasWidth, canvasHeight);

        execute(graphics);

//        try {
//            ImageIO.write(image, "jpg", new File("./abc.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return image;
    }

    private void generateCommands(LayoutBox box){
        generateContentCommand(box);
        borderCommand(box);
        for (LayoutBox child : box.getChildren()) {
            generateCommands(child);
        }

    }

    private void generateContentCommand(LayoutBox box){

        DisplayCommand command = new DisplayCommand();
        String background = box.getRules().get("background");
        if (background != null){
            String bgInt = background.substring(1);
            int rgb = Integer.parseInt(bgInt, 16);
            int startX = box.getBorderX() + box.getBorderLeft();
            int startY = box.getBorderY() + box.getBorderTop();

            command.setColor(rgb);
            command.setStartPoint(startX, startY);
            command.setWidth(box.getPaddingBoxWidth());
            command.setHeight(box.getPaddingBoxHeight());

            commands.add(command);
        }

    }

    private void cutIfOutOfBoundary(){
        //todo
    }


    private void borderCommand(LayoutBox box){
//        String display = box.getRules().get("display");
//        if (display != null && display.equals("none")) {
//            return;
//        }
        String borderColor = box.getRules().get("border-color");
        if (borderColor != null){
            DisplayCommand topCommand = new DisplayCommand();
            String bgInt = borderColor.substring(1);
            int rgb = Integer.parseInt(bgInt, 16);

            topCommand.setColor(rgb);
            topCommand.setStartPoint(box.getBorderX(), box.getBorderY());
            topCommand.setWidth(box.getBorderBoxWidth());
            topCommand.setHeight(box.getBorderTop());
            commands.add(topCommand);

            DisplayCommand rightCommand = new DisplayCommand();
            int startXOfRight = box.getBorderX() + box.getBorderLeft() + box.getPaddingBoxWidth();
            rightCommand.setColor(rgb);
            rightCommand.setStartPoint(startXOfRight, box.getBorderY());
            rightCommand.setWidth(box.getBorderRight());
            rightCommand.setHeight(box.getBorderBoxHeight());
            commands.add(rightCommand);

            DisplayCommand leftCommand = new DisplayCommand();
            leftCommand.setColor(rgb);
            leftCommand.setStartPoint(box.getBorderX(), box.getBorderY());
            leftCommand.setWidth(box.getBorderLeft());
            leftCommand.setHeight(box.getBorderBoxHeight());
            commands.add(leftCommand);

            DisplayCommand bottomCommand = new DisplayCommand();
            bottomCommand.setColor(rgb);
            int startYOfBottom = box.getBorderY() + box.getBorderTop() + box.getPaddingBoxHeight();
            bottomCommand.setStartPoint(box.getBorderX(), startYOfBottom);
            bottomCommand.setWidth(box.getBorderBoxWidth());
            bottomCommand.setHeight(box.getBorderBottom());
            commands.add(bottomCommand);
        }
    }

    private void execute(Graphics graphics){
        for (DisplayCommand command : commands) {
            graphics.setColor(new Color(command.color));
            graphics.fillRect(command.startX, command.startY, command.width, command.height);
        }
    }


}

