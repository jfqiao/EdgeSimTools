package org.edgesim.tool.platform.gui.propertyeditor;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.util.PlatformUtils;
import org.edgesim.tool.platform.gui.ConnectLineInfo;
import org.edgesim.tool.platform.gui.button.EdgeIconButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 对连接进行编辑，服务器之间的连接、边缘服务器与移动设备
 * 的连接都采用这个类处理。
 */
@Getter
@Setter
public class LinkEditor extends JFrame {

    private String startName;    // 仅展示，但是不可编辑
    private String endName;      // 进展示，但是不可编辑
    private double uplink;
    private double downlink;
    private double wirelessRate;
    private boolean isWirelessDevice; // 不可编辑，用来检查是边缘服务器与移动设备连接
    // 或是边缘服务器之间的连接。

    private JLabel labelStartName;
    private JLabel labelEndName;
    private JLabel labelUpLink;
    private JLabel labelDownLink;
    private JLabel labelWirelessRate;
//    private JLabel labelisWirelessDevice;

    private JTextField textFieldStartName;
    private JTextField textFieldEndName;
    private JTextField textFieldUpLink;
    private JTextField textFieldDownLink;
    private JTextField textFieldWirelessRate;
//    private JTextField textFieldIsWirelessDevice;

    private JButton buttonConfirm;

    private JPanel panel;

    private Point showPoint;

    private ConnectLineInfo connectLineInfo;

    public LinkEditor(String title, Point prefPos) throws HeadlessException {
        super(title);
        this.showPoint = prefPos;
    }

    public void setConnectLineInfo(ConnectLineInfo connectInfo) {
        this.connectLineInfo = connectInfo;
        startName = connectInfo.getStartJLabel().getEntityName();
        endName = connectInfo.getEndJLabel().getEntityName();
        if (connectInfo.getStartJLabel().getIconType() == EdgeIconButton.DEVICE_BUTTON ||
                connectInfo.getEndJLabel().getIconType() == EdgeIconButton.DEVICE_BUTTON) {
            isWirelessDevice = true;
            wirelessRate = connectInfo.getWirelessLinkConfig().getTransmissionRate();
        } else {
            isWirelessDevice = false;
            uplink = connectInfo.getLinkConfig().getUpLink();
            downlink = connectInfo.getLinkConfig().getDownLink();
        }
    }

    public void showEditor() {
        this.setSize(260, 260);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
        this.setLocation(showPoint);
        this.setVisible(true);
    }

    private void init() {
        panel = new JPanel(null);

        labelStartName = new JLabel("StartName: ");
        labelStartName.setBounds(10,10,120,20);
        labelEndName = new JLabel("EndName: ");
        labelEndName.setBounds(10,40,120,20);
        labelUpLink = new JLabel("UpLink: ");
        labelUpLink.setBounds(10,70,120,20);
        labelDownLink = new JLabel("DownLink: ");
        labelDownLink.setBounds(10,100,120,20);
        labelWirelessRate = new JLabel("WirelessRate: ");
        labelWirelessRate.setBounds(10,130,120,20);

        textFieldStartName = new JTextField(startName);
        textFieldStartName.setBounds(130,10,120,20);
        textFieldStartName.setEditable(false);
        textFieldEndName = new JTextField(endName);
        textFieldEndName.setBounds(130,40,120,20);
        textFieldEndName.setEditable(false);
        textFieldUpLink = new JTextField(PlatformUtils.formatDoubleData(uplink));
        textFieldUpLink.setBounds(130,70,120,20);
        textFieldDownLink = new JTextField(PlatformUtils.formatDoubleData(downlink));
        textFieldDownLink.setBounds(130,100,120,20);
        textFieldWirelessRate = new JTextField(PlatformUtils.formatDoubleData(wirelessRate));
        textFieldWirelessRate.setBounds(130,130,120,20);
        buttonConfirm = new JButton("确认");
        buttonConfirm.setBounds(80,180,120,25);

        panel.add(labelStartName);
        panel.add(textFieldStartName);
        panel.add(labelEndName);
        panel.add(textFieldEndName);
        panel.add(labelUpLink);
        panel.add(textFieldUpLink);
        panel.add(labelDownLink);
        panel.add(textFieldDownLink);
        panel.add(labelWirelessRate);
        panel.add(textFieldWirelessRate);

//        panel.add(labelisWirelessDevice);
//        panel.add(textFieldIsWirelessDevice);
        panel.add(buttonConfirm);
        setIsWirelessDevice();
        setComponent();
        this.add(panel);
        setConfirmAction();
    }

    private void setIsWirelessDevice() {
        if (connectLineInfo.getStartJLabel().getIconType() == EdgeIconButton.DEVICE_BUTTON
                || connectLineInfo.getEndJLabel().getIconType() == EdgeIconButton.DEVICE_BUTTON)
            isWirelessDevice = true;
    }

    private void setComponent() {
        if (isWirelessDevice) {
//            labelDownLink.setVisible(false);
//            labelUpLink.setVisible(false);
            textFieldUpLink.setVisible(false);
            textFieldDownLink.setVisible(false);
        } else {
//            textFieldWirelessRate.setEditable(false);
//            labelWirelessRate.setVisible(false);
            textFieldWirelessRate.setVisible(false);
        }
    }

    private void setConfirmAction() {
        this.buttonConfirm.addActionListener(new EditorConfirmEvent(this) {
            @Override
            public void doAction(ActionEvent e) {
                if (isWirelessDevice) {
                    wirelessRate = Double.parseDouble(textFieldWirelessRate.getText());
                    connectLineInfo.getWirelessLinkConfig().setTransmissionRate(wirelessRate);
                } else {
                    uplink = Double.parseDouble(textFieldUpLink.getText());
                    downlink = Double.parseDouble(textFieldDownLink.getText());
                    connectLineInfo.getLinkConfig().setUpLink(uplink);
                    connectLineInfo.getLinkConfig().setDownLink(downlink);
                }
            }
        });
    }

}
