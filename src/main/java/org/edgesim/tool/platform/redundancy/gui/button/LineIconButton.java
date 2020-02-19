package org.edgesim.tool.platform.redundancy.gui.button;

import org.edgesim.tool.platform.redundancy.gui.IconLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LineIconButton extends EdgeIconButton {

    public static boolean isClicked;
    public static boolean isStartLabel;
    public static IconLabel startLabel;

    public LineIconButton() {
        super(EdgeIconButton.LINE_BUTTON, "", "line.png");
        setLineAction();
    }

    private void setLineAction() {
        getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("line clicked.");
                isClicked = true;
                isStartLabel = true;
            }
        });
    }
}
