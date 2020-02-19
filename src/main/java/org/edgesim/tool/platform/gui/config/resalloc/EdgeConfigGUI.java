package org.edgesim.tool.platform.gui.config.resalloc;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.jsoninfo.generator.EdgeGenerator;
import org.edgesim.tool.platform.gui.EdgeGUI;
import org.edgesim.tool.platform.gui.position.ConfigPanelCalculator;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;
import org.edgesim.tool.platform.gui.position.PosConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.EdgeConfig;
import org.edgesim.tool.platform.jsoninfo.generator.LinkGenerator;
import org.edgesim.tool.platform.jsoninfo.generator.PositionGenerator;

import java.awt.*;
import java.util.List;

import javax.swing.*;

@Getter
@Setter
public class EdgeConfigGUI extends JPanel {

    private JTextField edgeNumField;
    private JTextField bandwidthMeanField;
    private JTextField bandwidthDevField;
    private JTextField priceMeanField;
    private JTextField priceDevField;
    private JTextField computingPowerMeanField;
    private JTextField computingPowerDevField;

    private final int labelLeftPadding = 10;
    private final int labelTextGap = 10;
    private final int textRightPadding = 5;

    private EdgeGenerator edgeGenerator;

    private LinkGenerator linkGenerator;

    private ConfigPanelCalculator configPanelCalculator;

    private EdgeGUI parentGui;

    public void setEdgeGenerator(PositionGenerator positionGenerator) {
        this.edgeGenerator = new EdgeGenerator(
                Integer.parseInt(edgeNumField.getText()),
                Double.parseDouble(computingPowerMeanField.getText()),
                Double.parseDouble(computingPowerDevField.getText()),
                Double.parseDouble(priceMeanField.getText()),
                Double.parseDouble(priceDevField.getText()),
                positionGenerator
        );
    }

    public void setLinkGenerator(List<EdgeConfig> edgeConfigs) {
        this.linkGenerator = new LinkGenerator(edgeConfigs, Double.parseDouble(bandwidthMeanField.getText()),
                Double.parseDouble(bandwidthDevField.getText()));
    }

    /**
     * Create the application.
     */
    public EdgeConfigGUI(EdgeGUI parentEdgeGUI) {
        this.parentGui = parentEdgeGUI;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        this.setBounds(EdgeGUIPositionConfig.EDGE_CONFIG_X, EdgeGUIPositionConfig.EDGE_CONFIG_Y,
                EdgeGUIPositionConfig.EDGE_CONFIG_WIDTH, EdgeGUIPositionConfig.EDGE_CONFIG_HEIGHT);
        configPanelCalculator = new ConfigPanelCalculator(this.getWidth(), this.getHeight(),
                7, -1, false, true);
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setLayout(null);

        JLabel titleLabel = new JLabel("<html><body><p align='center'><b>Edge Config</b></p></body></html>",SwingConstants.CENTER);
        PosConfig posConfig = configPanelCalculator.getTitlePos();
        titleLabel.setBounds(posConfig.getX(), posConfig.getY(), posConfig.getWidth(), posConfig.getHeight());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titleLabel);

        int configIndex = 0;
        JLabel lblEdgeNumber = new JLabel("Edge Number:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblEdgeNumber, configIndex++);
        this.add(lblEdgeNumber);

        JLabel lblBandwidthMean = new JLabel("Bandwidth Mean:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblBandwidthMean, configIndex++);
        this.add(lblBandwidthMean);

        JLabel lblBandwidthDev = new JLabel("Bandwidth Dev:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblBandwidthDev, configIndex++);
        this.add(lblBandwidthDev);

        JLabel lblPriceMean = new JLabel("Price Mean:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblPriceMean, configIndex++);
        this.add(lblPriceMean);

        JLabel lblPriceDev = new JLabel("Price Dev:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblPriceDev, configIndex++);
        this.add(lblPriceDev);

        JLabel lblPowerMean = new JLabel("Power Mean:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblPowerMean, configIndex++);
        this.add(lblPowerMean);

        JLabel lblPowerDev = new JLabel("Power Dev:");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, lblPowerDev, configIndex);
        this.add(lblPowerDev);

        configIndex = 0;
        edgeNumField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, edgeNumField, configIndex++);
        bandwidthMeanField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, bandwidthMeanField, configIndex++);
        bandwidthDevField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, bandwidthDevField, configIndex++);
        priceMeanField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, priceMeanField, configIndex++);
        priceDevField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, priceDevField, configIndex++);
        computingPowerMeanField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, computingPowerMeanField, configIndex++);
        computingPowerDevField = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, computingPowerDevField, configIndex);

        this.add(edgeNumField);
        this.add(bandwidthMeanField);
        this.add(bandwidthDevField);
        this.add(priceMeanField);
        this.add(priceDevField);
        this.add(computingPowerMeanField);
        this.add(computingPowerDevField);
    }
}