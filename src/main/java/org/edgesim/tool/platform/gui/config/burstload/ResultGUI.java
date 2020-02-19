package org.edgesim.tool.platform.gui.config.burstload;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.EdgeGUI;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class ResultGUI extends JPanel {
    private static final int PADDING=10;

    private JTextField textFieldDelay;

    private EdgeGUI edgeGUI;

    public ResultGUI(EdgeGUI edgeGUI) {
        this.edgeGUI = edgeGUI;
        init();
    }

    private void init(){
        this.setBounds(EdgeGUIPositionConfig.RESULT_X, EdgeGUIPositionConfig.RESULT_Y,
                EdgeGUIPositionConfig.RESULT_WIDTH, EdgeGUIPositionConfig.RESULT_HEIGHT);
        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(Color.WHITE);

        JLabel labelDelay=new JLabel("Delay: ");
        labelDelay.setBounds(10,10,100,100);
        this.add(labelDelay);

        labelDelay.setHorizontalAlignment(SwingConstants.CENTER);

        textFieldDelay=new JTextField();
        textFieldDelay.setBounds(120,40,80,40);
        this.add(textFieldDelay);
    }
}
