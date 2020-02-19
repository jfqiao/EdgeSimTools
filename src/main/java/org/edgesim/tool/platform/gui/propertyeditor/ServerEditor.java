package org.edgesim.tool.platform.gui.propertyeditor;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.IconLabel;
import org.edgesim.tool.platform.util.PlatformUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Getter
@Setter
public class ServerEditor extends JFrame {
    private String edgeName;
    private double computingPower;
    private double price;

    private JLabel labelEdgeName;
    private JLabel labelComputingPower;
    private JLabel labelPrice;

    private JTextField textFieldEdgeName;
    private JTextField textFieldComputingPower;
    private JTextField textFieldPrice;

    private JButton buttonConfirm;

    private IconLabel iconLabel;

    private Point showPoint;

    public ServerEditor(String title, Point prefPos) throws HeadlessException {
        super(title);
        this.showPoint = prefPos;
    }

    public void showEditor() {
        this.setSize(260, 200);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
        this.setLocation(showPoint);
        this.setVisible(true);
    }

    private void init() {
        JPanel panel = new JPanel(null);

        labelEdgeName = new JLabel("EdgeName: ");
        labelEdgeName.setBounds(10,10,120,20);
        labelComputingPower = new JLabel("Computing Power: ");
        labelComputingPower.setBounds(10,40,120,20);
        labelPrice = new JLabel("Price: ");
        labelPrice.setBounds(10,70,120,20);

        textFieldEdgeName = new JTextField(edgeName);
        textFieldEdgeName.setBounds(130,10,120,20);
        textFieldComputingPower = new JTextField(PlatformUtils.formatDoubleData(computingPower));
        textFieldComputingPower.setBounds(130,40,120,20);
        textFieldPrice = new JTextField(PlatformUtils.formatDoubleData(price));
        textFieldPrice.setBounds(130,70,120,20);


        panel.add(labelEdgeName);
        panel.add(textFieldEdgeName);
        panel.add(labelComputingPower);
        panel.add(textFieldComputingPower);
        panel.add(labelPrice);
        panel.add(textFieldPrice);

        buttonConfirm = new JButton("确认");
        buttonConfirm.setBounds(60,120,120,25);
        panel.add(buttonConfirm);

        this.add(panel);

        setConfigAction();
    }

    // 注意名称修改后对所有的连接线的名称都要修改。
    private void setConfigAction() {
        this.buttonConfirm.addActionListener(new EditorConfirmEvent(this) {
            @Override
            public void doAction(ActionEvent e) {
                edgeName = textFieldEdgeName.getText();
                computingPower = Double.parseDouble(textFieldComputingPower.getText());
                price = Double.parseDouble(textFieldPrice.getText());
                iconLabel.getEdgeConfig().setName(edgeName);
                iconLabel.getEdgeConfig().setComputingPower(computingPower);
                iconLabel.getEdgeConfig().setPrice(price);
            }
        });
    }
}
