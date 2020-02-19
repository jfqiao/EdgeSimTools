package org.edgesim.tool.platform.redundancy.gui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class LineShape{
    public int x1, x2, y1, y2;
    public int lineStroke = 3;

    public LineShape() {
        super();
    }

    public LineShape(JLabel startLabel, JLabel endLabel) {
        super();
        this.x1 = startLabel.getX() + 20;
        this.y1 = startLabel.getY() + 20;
        this.x2 = endLabel.getX() + 20;
        this.y2 = endLabel.getY() + 20;
//        if (startLabel.getX() > endLabel.getX()) {
//            JLabel tmpLabel = startLabel;
//            startLabel = endLabel;
//            endLabel = tmpLabel;
//        }
//
//        if (endLabel.getX() == startLabel.getX()) {
//            // 垂直方向的线
//            this.x1 = this.x2 = startLabel.getX();
//            if (startLabel.getY() > endLabel.getY()) {
//                this.y1 = endLabel.getY();
//                this.y2 = startLabel.getY();
//            } else {
//                this.y1 = startLabel.getY();
//                this.y2 = startLabel.getY();
//            }
//        } else {
//            // 非垂直方向画线
//            double k = (double) (endLabel.getY() - startLabel.getY()) / (double) (endLabel.getX() - startLabel.getX());
//            x1 = startLabel.getX() + 40;
//            x2 = endLabel.getX();
//            y1 = (int) (k * 20) + startLabel.getY() + 20;
//            y2 = -(int) (k * (endLabel.getX() - startLabel.getX() - 20)) + startLabel.getY() + 20;
//        }

    }

    public void setPos(int x, int y, IconLabel startLabel) {
        this.x2 = x;
        this.y2 = y;
        this.x1 = startLabel.getX() + 20;
        this.y1 = startLabel.getY() + 20;
//        System.out.println(this.x2 + " " + (startLabel.getX() + 20));
//        double k = (this.y2 - startLabel.getY() - 20) / (double) (this.x2 - startLabel.getX() - 20);
//        if (this.x2 < startLabel.getX())  {
//            this.x1 = startLabel.getX();
//            this.y1 = (int) (k * (-20)) + startLabel.getY() + 20;
//        } else if (this.x2 < startLabel.getX() + 20 || (this.x2 > startLabel.getX() + 20 && this.x2 < startLabel.getX() + 40)) {
//            // 左上画线
//            this.y1 = startLabel.getY();
//            this.x1 = (int) ((this.y1 - startLabel.getY() - 20) / k) + 20 + startLabel.getX();
//        } else {
//            // 向上或者向下画线
//            this.x1 = this.x2;
//            if (this.y2 > startLabel.getY()) {
//                this.y1 = startLabel.getY();
//            } else {
//                this.y1 = startLabel.getY();
//            }
//        }
    }

    public void setPos(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }



    public void display(Graphics graphics, Color color) {
        graphics.setColor(color);
        Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setStroke(new BasicStroke(this.lineStroke));    // 线条的粗细
        graphics.drawLine(x1,y1,x2,y2);
    }

//    public void delete(Graphics graphics) {
//        graphics.setColor(Color.WHITE);
//        graphics.drawLine(x1, y1, x2, y2);
//    }
}
