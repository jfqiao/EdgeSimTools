package org.edgesim.tool.platform.gui.propertyeditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class EditorConfirmEvent implements ActionListener {

    private JFrame frame;

    public EditorConfirmEvent(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        doAction(e);
        frame.dispose();
    }

    public abstract void doAction(ActionEvent e);

}
