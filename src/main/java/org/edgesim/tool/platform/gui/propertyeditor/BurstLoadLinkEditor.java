package org.edgesim.tool.platform.gui.propertyeditor;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.ConnectLineInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
@Setter
public class BurstLoadLinkEditor extends JFrame {

    private JLabel labelName;
    private JLabel labelStartEdgeName;
    private JLabel labelEndEdgeName;
    private JLabel labelParallelWidth;
    private JLabel labelTransRate;

    private JTextField textFieldName;
    private JTextField textFieldStartEdgeName;
    private JTextField textFieldEndEdgeName;
    private JTextField textFieldParallelWidth;
    private JTextField textFieldTransRate;

    private ConnectLineInfo connectLineInfo;
    private Point showPoint;

    public BurstLoadLinkEditor(ConnectLineInfo connectLineInfo, Point showPoint) {
        super("连接属性");
        this.connectLineInfo = connectLineInfo;
        this.showPoint = showPoint;
    }

    public void showEditor(){
        this.setSize(260,260);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
        this.setLocation(showPoint);
        this.setVisible(true);
    }

    private void init(){
        JPanel panel = new JPanel(null);

        labelName = new JLabel("Link Name: ");
        labelName.setBounds(10,10,120,20);
        labelStartEdgeName = new JLabel("Start Edge Name: ");
        labelStartEdgeName.setBounds(10,40,120,20);
        labelEndEdgeName = new JLabel("End Edge Name: ");
        labelEndEdgeName.setBounds(10,70,120,20);
        labelParallelWidth = new JLabel("Parallel Width: ");
        labelParallelWidth.setBounds(10,100,120,20);
        labelTransRate = new JLabel("Trans Rate: ");
        labelTransRate.setBounds(10,130,120,20);

        textFieldName = new JTextField(connectLineInfo.getBurstLoadLinkConfig().getName());
        textFieldName.setEditable(false);
        textFieldName.setBounds(130,10,80,20);
        textFieldStartEdgeName = new JTextField(String.valueOf(connectLineInfo.getBurstLoadLinkConfig().getStartEdgeName()));
        textFieldStartEdgeName.setEditable(false);
        textFieldStartEdgeName.setBounds(130,40,80,20);
        textFieldEndEdgeName = new JTextField(String.valueOf(connectLineInfo.getBurstLoadLinkConfig().getEndEdgeName()));
        textFieldEndEdgeName.setEditable(false);
        textFieldEndEdgeName.setBounds(130,70,80,20);
        textFieldParallelWidth = new JTextField(String.valueOf(connectLineInfo.getBurstLoadLinkConfig().getParallelWidth()));
        textFieldParallelWidth.setBounds(130,100,80,20);
        textFieldTransRate = new JTextField(String.valueOf(connectLineInfo.getBurstLoadLinkConfig().getTransRate()));
        textFieldTransRate.setBounds(130,130,80,20);

        JButton buttonConfirm = new JButton("确认");
        buttonConfirm.setBounds(60,180,120,25);
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                BurstLoadLinkEditor.this.dispose();
            }
        });

        panel.add(labelName);
        panel.add(labelStartEdgeName);
        panel.add(labelEndEdgeName);
        panel.add(labelParallelWidth);
        panel.add(labelTransRate);
        panel.add(textFieldName);
        panel.add(textFieldStartEdgeName);
        panel.add(textFieldEndEdgeName);
        panel.add(textFieldParallelWidth);
        panel.add(textFieldTransRate);
        panel.add(buttonConfirm);

        this.add(panel);
    }
}
