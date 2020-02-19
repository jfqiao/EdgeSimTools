package org.edgesim.tool.platform.gui.propertyeditor;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
@Setter
public class BurstLoadEdgeEditor extends JFrame {

    private String name;
    private double cpuCycle;
    private Point showPoint;

    private JLabel  labelEdgeName;
    private JLabel labelCpuCycle;

    private JTextField textFieldEdgeName;
    private JTextField textFieldCpuCycle;

    public BurstLoadEdgeEditor(String name, double cpuCycle, Point showPoint) {
        super("边缘服务器属性");
        this.name = name;
        this.cpuCycle = cpuCycle;
        this.showPoint=showPoint;
    }

    public void showEditor(){
        this.setSize(260,200);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
        this.setLocation(showPoint);
        this.setVisible(true);
    }

    private void init(){
        JPanel panel = new JPanel(null);
        labelEdgeName = new JLabel("Edge Name: ");
        labelEdgeName.setBounds(10,10,120,20);
        labelCpuCycle = new JLabel("Cpu Cycle: ");
        labelCpuCycle.setBounds(10,40,120,20);
        textFieldEdgeName = new JTextField(name);
        textFieldEdgeName.setEditable(false);
        textFieldEdgeName.setBounds(130,10,80,20);
        textFieldCpuCycle=new JTextField(String.valueOf(cpuCycle));
        textFieldCpuCycle.setBounds(130,40,80,20);

        panel.add(labelEdgeName);
        panel.add(textFieldEdgeName);
        panel.add(labelCpuCycle);
        panel.add(textFieldCpuCycle);

        JButton button = new JButton("确认");
        button.setBounds(60,80,120,25);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                BurstLoadEdgeEditor.this.dispose();
            }
        });
        panel.add(button);

        this.add(panel);
    }
}
