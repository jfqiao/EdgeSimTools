package org.edgesim.tool.platform.gui.position;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jfqiao
 * @since 2020/01/09
 */
public class ConfigPanelCalculator {
    private int panelWidth;
    private int panelHeight;
    private int configNum;
    private int padding = 10;
    private int gap;
    private boolean withConfirmButton = false;
    private boolean withTitle = false;

    private List<PosConfig> labelConfigs;
    private List<PosConfig> inputConfigs;
    private PosConfig buttonConfig;

    private int labelHeight = -1;
    private int labelWidth = -1;

    public ConfigPanelCalculator(int panelWidth, int panelHeight, int configNum,
                                 int padding, boolean withConfirmButton, boolean withTitle) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.configNum = configNum;
        if (padding > 0) {
            this.padding = padding;
        }
        this.withConfirmButton = withConfirmButton;
        this.withTitle = withTitle;
        setLabelHeight();
        setLabelWidth();
    }

    private void setLabelHeight() {
        double totalCnt = 0;
        if (withTitle)
            totalCnt += 1.5;
        if (withConfirmButton) {
            panelHeight -= 25;
        }
        labelHeight = (int) ((panelHeight - 2 * padding) / (totalCnt + configNum * 1.5 - 0.5));
        gap = labelHeight / 2;
    }

    private void setLabelWidth() {
        labelWidth = (panelWidth - 2 * padding) / 2;
    }

    public PosConfig getTitlePos() {
        PosConfig posConfig = new PosConfig();
        posConfig.setX(padding);
        posConfig.setY(padding);
        posConfig.setHeight(labelHeight);
        posConfig.setWidth(panelWidth - 2 * padding);
        return posConfig;
    }

    public List<PosConfig> getLabelConfigPos() {
        if (labelConfigs != null) {
            return labelConfigs;
        }
        setLabelAndInputConfigs();
        return labelConfigs;
    }

    public List<PosConfig> getInputConfigs() {
        if (inputConfigs != null) {
            return inputConfigs;
        }
        setLabelAndInputConfigs();
        return inputConfigs;
    }

    public PosConfig getButtonConfig() {
        return buttonConfig;
    }

    private void setLabelAndInputConfigs() {
        labelConfigs = new ArrayList<>();
        inputConfigs = new ArrayList<>();
        for (int i = 0; i < configNum; i++) {
            PosConfig labelConfig = new PosConfig();
            labelConfig.setX(padding);
            labelConfig.setY(padding + (gap + labelHeight) * (i + 1) - gap);
            labelConfig.setWidth(labelWidth);
            labelConfig.setHeight(labelHeight);
            labelConfigs.add(labelConfig);

            PosConfig inputConfig = new PosConfig();
            inputConfig.setX(padding + labelWidth);
            inputConfig.setY(labelConfig.getY());
            inputConfig.setHeight(labelHeight);
            inputConfig.setWidth(labelWidth);
            inputConfigs.add(inputConfig);
        }
        if (withConfirmButton) {
            buttonConfig = new PosConfig();
            buttonConfig.setX(padding + labelWidth);
            buttonConfig.setY(padding + (gap + labelHeight) * configNum + labelHeight);
            buttonConfig.setWidth(labelWidth);
            buttonConfig.setHeight(25);
        }
    }

    public static void setLabelPos(ConfigPanelCalculator panelCalculator, JLabel label, int index) {
        PosConfig config = panelCalculator.getLabelConfigPos().get(index);
        label.setBounds(
                config.getX(),
                config.getY(),
                config.getWidth(),
                config.getHeight()
        );
    }

    public static void setInputPos(ConfigPanelCalculator panelCalculator, JTextField textField, int index) {
        PosConfig config = panelCalculator.getInputConfigs().get(index);
        textField.setBounds(
                config.getX(),
                config.getY(),
                config.getWidth(),
                config.getHeight()
        );
    }

}
