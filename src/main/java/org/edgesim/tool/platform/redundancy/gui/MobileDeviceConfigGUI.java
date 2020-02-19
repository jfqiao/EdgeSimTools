package org.edgesim.tool.platform.redundancy.gui;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.AppConfig;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.EdgeConfig;
import org.edgesim.tool.platform.redundancy.jsoninfo.generator.DeviceGenerator;
import org.edgesim.tool.platform.redundancy.jsoninfo.generator.PositionGenerator;

import java.awt.*;
import java.util.List;

import javax.swing.*;

@Getter
@Setter
public class MobileDeviceConfigGUI extends JPanel {

    private JLabel labelTransmissionRateVar;
    private JLabel labelRequestNum;

    private JTextField mobileDeviceNumFiled;
    private JTextField requestNumField;

    private final int labelTextGap=10;
    private final int labelPanelPadding=10;

    private DeviceGenerator deviceGenerator;

    private EdgeGUI parentGui;

    public void setDeviceGenerator(List<EdgeConfig> edgeConfigs, List<AppConfig> appConfigs,
                                   PositionGenerator positionGenerator) {
        this.deviceGenerator = new DeviceGenerator(
                edgeConfigs,
                appConfigs,
                Integer.parseInt(mobileDeviceNumFiled.getText()),
                Integer.parseInt(requestNumField.getText()),
                positionGenerator
        );
    }

    /**
     * Create the application.
     */
    public MobileDeviceConfigGUI(EdgeGUI parentEdgeGUI) {
        this.parentGui = parentEdgeGUI;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        this.setBounds(EdgeGUIPositionConfig.DEVICE_CONFIG_X, EdgeGUIPositionConfig.DEVICE_CONFIG_Y_RE,
                EdgeGUIPositionConfig.DEVICE_CONFIG_WIDTH, EdgeGUIPositionConfig.DEVICE_CONFIG_HEIGHT);

        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(Color.WHITE);

        final int labelWidth=(int)(EdgeGUIPositionConfig.DEVICE_CONFIG_WIDTH/2-labelPanelPadding-labelTextGap-10)/2,
                labelHeight=(int)(EdgeGUIPositionConfig.DEVICE_CONFIG_HEIGHT/4);
        final int textFieldWidth=labelWidth,textFieldHeight=labelHeight;

        JLabel tag = new JLabel("Mobile Device Config");
        tag.setBounds(labelPanelPadding,10,textFieldWidth * 2,textFieldHeight);

        labelTransmissionRateVar=new JLabel("<html><body>"+"Mobile Device"+"<br>"+" Number:"+"<body></html>");
        labelTransmissionRateVar.setBounds(labelPanelPadding,10+labelHeight*2,labelWidth,labelHeight);
        labelRequestNum=new JLabel("Request Num:");
        labelRequestNum.setBounds(labelPanelPadding * 2 + 2 * labelWidth,10+labelHeight*2,labelWidth,labelHeight);

        mobileDeviceNumFiled = new JTextField();
        mobileDeviceNumFiled.setBounds(labelPanelPadding+labelWidth,10+labelHeight*2,labelWidth,labelHeight);
        requestNumField = new JTextField();
        requestNumField.setBounds(labelPanelPadding+labelWidth * 3+labelTextGap,10+labelHeight*2,labelWidth,labelHeight);

        this.add(tag);
        this.add(mobileDeviceNumFiled);
        this.add(labelTransmissionRateVar);
        this.add(labelRequestNum);
        this.add(requestNumField);

    }

}