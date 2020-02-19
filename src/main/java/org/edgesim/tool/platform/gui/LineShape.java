package org.edgesim.tool.platform.gui;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.button.EdgeIconButton;

import java.awt.*;

@Getter
@Setter
public class LineShape{
    private IconLabel startLabel;
    private IconLabel endLabel;
    public int x1, x2, y1, y2;
    public int lineStroke = 3;

    public LineShape() {
        super();
    }

    public LineShape(IconLabel startLabel, IconLabel endLabel) {
        super();
        this.startLabel = startLabel;
        this.endLabel = endLabel;
        this.x1 = startLabel.getX() + 20;
        this.y1 = startLabel.getY() + 20;
        this.x2 = endLabel.getX() + 20;
        this.y2 = endLabel.getY() + 20;
    }

    public void setPos(int x, int y, IconLabel startLabel) {
        this.x2 = x;
        this.y2 = y;
        this.x1 = startLabel.getX() + 20;
        this.y1 = startLabel.getY() + 20;
    }

    public void display(Graphics graphics, Color color) {
        Color lineColor;
        if (startLabel == null || endLabel == null) {
            lineColor = color;
        }
        else {
            if (startLabel.getIconType() == EdgeIconButton.DEVICE_BUTTON
                    || endLabel.getIconType() == EdgeIconButton.DEVICE_BUTTON) {
                lineColor = Color.BLUE;
            } else {
                if (startLabel.getIconType() == EdgeIconButton.CLOUD_SERVER_BUTTON
                        || endLabel.getIconType() == EdgeIconButton.CLOUD_SERVER_BUTTON) {
                    lineColor = Color.RED;
                } else {
                    lineColor = Color.BLACK;
                }
            }
        }
        graphics.setColor(lineColor);
        Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setStroke(new BasicStroke(this.lineStroke));    // 线条的粗细
        graphics.drawLine(x1,y1,x2,y2);
    }

}
