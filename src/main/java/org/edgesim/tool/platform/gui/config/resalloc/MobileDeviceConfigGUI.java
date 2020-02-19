package org.edgesim.tool.platform.gui.config.resalloc;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.EdgeGUI;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.AppConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.EdgeConfig;
import org.edgesim.tool.platform.jsoninfo.generator.PositionGenerator;
import org.edgesim.tool.platform.jsoninfo.generator.DeviceGenerator;

import java.awt.*;
import java.util.List;

import javax.swing.*;

@Getter
@Setter
public class MobileDeviceConfigGUI extends JPanel {

    private JLabel titleLabel;

    private JLabel labelTransmissionRateMean;
    private JLabel labelTransmissionRateVar;
    private JLabel labelReqGeneratedMean;
    private JLabel labelReqGeneratedVar;

    private JTextField textFieldTransmissionRateMean;
    private JTextField textFieldTransmissionRateVar;
    private JTextField textFieldReqGeneratedMean;
    private JTextField textFieldReqGeneratedVar;

    private final int labelTextGap = 10;
    private final int labelPanelPadding = 10;

    private DeviceGenerator deviceGenerator;

    private EdgeGUI parentGui;

    public void setDeviceGenerator(List<EdgeConfig> edgeConfigs, List<AppConfig> appConfigs,
                                   PositionGenerator positionGenerator) {
        this.deviceGenerator = new DeviceGenerator(
                edgeConfigs,
                appConfigs,
                Double.parseDouble(textFieldTransmissionRateMean.getText()),
                Double.parseDouble(textFieldTransmissionRateVar.getText()),
                Double.parseDouble(textFieldReqGeneratedMean.getText()),
                Double.parseDouble(textFieldReqGeneratedVar.getText()),
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
        this.setBounds(EdgeGUIPositionConfig.DEVICE_CONFIG_X, EdgeGUIPositionConfig.DEVICE_CONFIG_Y,
                EdgeGUIPositionConfig.DEVICE_CONFIG_WIDTH, EdgeGUIPositionConfig.DEVICE_CONFIG_HEIGHT);
        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(Color.WHITE);

        int oneLabelHeight = (int) ((this.getHeight() - 2 * labelPanelPadding) / 4.5);
        int labelWidth = (this.getWidth() - 3 * labelPanelPadding) / 4;

        JLabel titleLabel = new JLabel("<html><body><p><b>Device Config</b></p></body></html>");
        titleLabel.setBounds(labelPanelPadding, labelPanelPadding, this.getWidth() - 2 * labelPanelPadding, (int) (oneLabelHeight * 0.75 + labelPanelPadding));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titleLabel);

        labelTransmissionRateMean = new JLabel("<html><body><p align=\"center\">Transmission<br/>Rate Mean:</p></body></html>");
        labelTransmissionRateMean.setBounds(labelPanelPadding, labelPanelPadding + oneLabelHeight,
                labelWidth, (int) (oneLabelHeight * 1.5));
        labelTransmissionRateVar = new JLabel("<html><body><p align=\"center\">Transmission<br/>Rate Dev:</p></body></html>");
        labelTransmissionRateVar.setBounds(labelPanelPadding, labelPanelPadding + 3 * oneLabelHeight,
                labelWidth, labelTransmissionRateMean.getHeight());
        labelReqGeneratedMean = new JLabel("<html><body><p align=\"center\">Req Generated<br/>Rate Mean:</p></body></html>");
        labelReqGeneratedMean.setBounds(2 * labelPanelPadding + 2 * labelWidth, labelTransmissionRateMean.getY(),
                labelWidth, labelTransmissionRateMean.getHeight());
        labelReqGeneratedVar = new JLabel("<html><body><p align=\"center\">Req Generated<br/>Rate Dev:</p></body></html>");
        labelReqGeneratedVar.setBounds(labelReqGeneratedMean.getX(), labelTransmissionRateVar.getY(),
                labelWidth, labelTransmissionRateMean.getHeight());

        textFieldTransmissionRateMean = new JTextField();
        textFieldTransmissionRateMean.setBounds(labelTransmissionRateMean.getX() + labelWidth, labelTransmissionRateMean.getY(),
                labelWidth, labelTransmissionRateMean.getHeight());
        textFieldTransmissionRateVar = new JTextField();
        textFieldTransmissionRateVar.setBounds(labelTransmissionRateVar.getX() + labelWidth, labelTransmissionRateVar.getY(),
                labelWidth, labelTransmissionRateMean.getHeight());
        textFieldReqGeneratedMean = new JTextField();
        textFieldReqGeneratedMean.setBounds(labelReqGeneratedMean.getX() + labelWidth, labelReqGeneratedMean.getY(),
                labelWidth, labelReqGeneratedMean.getHeight());
        textFieldReqGeneratedVar = new JTextField();
        textFieldReqGeneratedVar.setBounds(labelReqGeneratedVar.getX() + labelWidth, labelReqGeneratedVar.getY(),
                labelWidth, labelReqGeneratedVar.getHeight());

        this.add(labelTransmissionRateMean);
        this.add(textFieldTransmissionRateMean);
        this.add(labelReqGeneratedMean);
        this.add(textFieldReqGeneratedMean);
        this.add(labelTransmissionRateVar);
        this.add(textFieldTransmissionRateVar);
        this.add(labelReqGeneratedVar);
        this.add(textFieldReqGeneratedVar);
    }

}