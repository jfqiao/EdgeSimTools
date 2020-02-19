package org.edgesim.tool.platform.gui.config.resalloc;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.EdgeGUI;
import org.edgesim.tool.platform.gui.position.ConfigPanelCalculator;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;
import org.edgesim.tool.platform.gui.position.PosConfig;
import org.edgesim.tool.platform.jsoninfo.generator.AppGenerator;

import java.awt.*;

import javax.swing.*;

@Getter
@Setter
public class ServiceConfigGUI extends JPanel {

    private JTextField serviceNumField;
    private JTextField inputMeanField;
    private JTextField inputDevField;
    private JTextField outputMeanField;
    private JTextField outputDevField;
    private JTextField workloadMeanField;
    private JTextField workloadDevField;
    private EdgeGUI parentObject;

    private ConfigPanelCalculator configPanelCalculator;

    private AppGenerator appGenerator;

    public void setAppGenerator() {
        this.appGenerator = new AppGenerator(
                Integer.parseInt(serviceNumField.getText()),
                Double.parseDouble(inputMeanField.getText()),
                Double.parseDouble(inputDevField.getText()),
                Double.parseDouble(outputMeanField.getText()),
                Double.parseDouble(outputDevField.getText()),
                Double.parseDouble(workloadMeanField.getText()),
                Double.parseDouble(workloadDevField.getText())
        );
    }

    /**
     * Create the application.
     */
    public ServiceConfigGUI(EdgeGUI parentObject) {
        this.parentObject = parentObject;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        this.setBounds(EdgeGUIPositionConfig.SERVICE_CONFIG_X, EdgeGUIPositionConfig.SERVICE_CONFIG_Y,
                EdgeGUIPositionConfig.SERVICE_CONFIG_WIDTH, EdgeGUIPositionConfig.SERVICE_CONFIG_HEIGHT);
        configPanelCalculator = new ConfigPanelCalculator(
                this.getWidth(), this.getHeight(), 7, -1, false, true
        );

        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(Color.WHITE);

        PosConfig titleConfig = configPanelCalculator.getTitlePos();
        JLabel titleLabel = new JLabel("<html><body><p align=\"center\"><b>Service Config</b></p></body></html>",SwingConstants.CENTER);
        titleLabel.setBounds(titleConfig.getX(), titleConfig.getY(), titleConfig.getWidth(), titleConfig.getHeight());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);


        int configIndex = 0;
        JLabel lblServiceNumber = new JLabel("Service Number:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblServiceNumber, configIndex++);

        JLabel lblNewLabel = new JLabel("Input Mean:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblNewLabel, configIndex++);

        JLabel lblInputSizeDev = new JLabel("Input Dev:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblInputSizeDev, configIndex++);

        JLabel lblOutputSizeMean = new JLabel("Output Mean:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblOutputSizeMean, configIndex++);

        JLabel lblOutputSizeDev = new JLabel("Output Dev:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblOutputSizeDev, configIndex++);

        JLabel lblWorkloadSizeMean = new JLabel("Workload Mean:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblWorkloadSizeMean, configIndex++);

        JLabel lblWorkloadDev = new JLabel("Workload Dev:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblWorkloadDev, configIndex);

        configIndex = 0;
        serviceNumField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, serviceNumField, configIndex++);
        inputMeanField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator,inputMeanField, configIndex++);
        inputDevField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, inputDevField, configIndex++);
        outputMeanField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, outputMeanField, configIndex++);
        outputDevField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator,outputDevField, configIndex++);
        workloadMeanField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, workloadMeanField, configIndex++);
        workloadDevField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, workloadDevField, configIndex);

        this.add(titleLabel);
        this.add(lblServiceNumber);
        this.add(serviceNumField);
        this.add(lblNewLabel);
        this.add(inputMeanField);
        this.add(lblInputSizeDev);
        this.add(inputDevField);
        this.add(lblOutputSizeMean);
        this.add(outputMeanField);
        this.add(lblOutputSizeDev);
        this.add(outputDevField);
        this.add(lblWorkloadSizeMean);
        this.add(workloadMeanField);
        this.add(lblWorkloadDev);
        this.add(workloadDevField);
    }
}
