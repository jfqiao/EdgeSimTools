package org.edgesim.tool.platform.gui.propertyeditor;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.IconLabel;
import org.edgesim.tool.platform.util.PlatformUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 针对移动设备的属性修改。移动设备修改的主要属性为：设备名称、设备产生请求的速度。
 * @author jfqiao
 * @since 2019/12/27
 * TransmissionRateMean、TransmissionRateVar、ReqGeneratedMean、ReqGeneratedVar
 */
@Getter
@Setter
public class DeviceEditor extends JFrame {

    private String deviceName;
//    private double transmissionRate;
    private double reqGeneratedRate;
//    private double reqGeneratedVar;

    private JLabel labelDeviceName;
//    private JLabel labelTransmissionRate;
    private JLabel labelReqGeneratedRate;
//    private JLabel labelReqGeneratedVar;

    private JTextField textFieldDeviceName;
//    private JTextField textFieldTransmissionRate;
    private JTextField textFieldReqGeneratedRate;
//    private JTextField textFieldReqGeneratedVar;

    private JButton buttonConfirm;

    private Point showPoint;

    private IconLabel iconLabel;

    public DeviceEditor(String title, Point prefPos) throws HeadlessException {
        super(title);
        this.showPoint = prefPos;
    }

    public void showEditor() {
        this.setSize(280,180);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
        this.setLocation(showPoint);
        this.setVisible(true);
    }

    private void init(){
        JPanel panel=new JPanel(null);

        labelDeviceName =new JLabel("Device Name: ");
        labelDeviceName.setBounds(10,10,150,20);
        labelReqGeneratedRate =new JLabel("Request Generated Rate: ");
        labelReqGeneratedRate.setBounds(10,40,150,20);

        textFieldDeviceName =new JTextField(deviceName);
        textFieldDeviceName.setBounds(160,10,120,20);
        textFieldReqGeneratedRate =new JTextField(PlatformUtils.formatDoubleData(reqGeneratedRate));
        textFieldReqGeneratedRate.setBounds(160,40,120,20);
        buttonConfirm=new JButton("确认");
        buttonConfirm.setBounds(80,100,120,25);

        panel.add(labelDeviceName);
        panel.add(labelReqGeneratedRate);
        panel.add(textFieldDeviceName);
        panel.add(textFieldReqGeneratedRate);
        panel.add(buttonConfirm);

        this.add(panel);

        setConfirmAction();
    }

    private void setConfirmAction() {
        this.buttonConfirm.addActionListener(new EditorConfirmEvent(this) {
            @Override
            public void doAction(ActionEvent e) {
                deviceName = textFieldDeviceName.getText();
//                transmissionRate = Double.parseDouble(textFieldTransmissionRate.getText());
                reqGeneratedRate = Double.parseDouble(textFieldReqGeneratedRate.getText());
                iconLabel.getDeviceConfig().setName(deviceName);
                iconLabel.getDeviceConfig().setReqGeneratedRate(reqGeneratedRate);
            }
        });
    }
}
