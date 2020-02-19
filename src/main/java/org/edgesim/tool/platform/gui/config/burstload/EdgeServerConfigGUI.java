package org.edgesim.tool.platform.gui.config.burstload;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.jsoninfo.generator.BurstLoadLinkGenerator;
import org.edgesim.tool.platform.gui.EdgeGUI;
import org.edgesim.tool.platform.gui.position.ConfigPanelCalculator;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;
import org.edgesim.tool.platform.gui.position.PosConfig;
import org.edgesim.tool.platform.jsoninfo.generator.BurstLoadEdgeGenerator;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class EdgeServerConfigGUI extends JPanel{

    private JTextField textFieldCpuFreqLow;
    private JTextField textFieldCpuFreqHigh;
    private JTextField textFieldRamLow;
    private JTextField textFieldRamHigh;
    private JTextField textFieldStorageLow;
    private JTextField textFieldStorageHigh;

    private JTextField textFieldNumEdges;

    private final int labelLeftPadding = 10;
    private final int labelTextGap = 10;
    private final int textRightPadding = 5;

    private BurstLoadEdgeGenerator burstLoadEdgeGenerator;

    private BurstLoadLinkGenerator burstLoadLinkGenerator;

    private ConfigPanelCalculator configPanelCalculator;

    private EdgeGUI edgeGUIParent;

    public EdgeServerConfigGUI(EdgeGUI edgeGUIParent) {
        this.edgeGUIParent = edgeGUIParent;
        this.init();
    }

    private void init(){
        this.setBounds(EdgeGUIPositionConfig.EDGE_CONFIG_X,EdgeGUIPositionConfig.EDGE_CONFIG_Y,
                EdgeGUIPositionConfig.EDGE_CONFIG_WIDTH,EdgeGUIPositionConfig.EDGE_CONFIG_HEIGHT);
        configPanelCalculator = new ConfigPanelCalculator(this.getWidth(), this.getHeight(),7,
                -1,false,true);
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK
        ));
        this.setLayout(null);

        JLabel titleLabel = new JLabel("Edge Config",SwingConstants.CENTER);
        PosConfig posConfig = configPanelCalculator.getTitlePos();
        titleLabel.setBounds(posConfig.getX(), posConfig.getY(), posConfig.getWidth(),posConfig.getHeight());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titleLabel);

        int configIndex=0;
        JLabel labelCpuFreqLow = new JLabel("Freq Low: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelCpuFreqLow, configIndex++);
        this.add(labelCpuFreqLow);

        JLabel labelCpuFreqHigh = new JLabel("Freq High: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelCpuFreqHigh, configIndex++);
        this.add(labelCpuFreqHigh);

        JLabel labelRamLow = new JLabel("Ram Low: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelRamLow,configIndex++);
        this.add(labelRamLow);

        JLabel labelRamHigh = new JLabel("Ram High: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelRamHigh, configIndex++);
        this.add(labelRamHigh);

        JLabel labelStorageLow = new JLabel("Storage Low: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelStorageLow, configIndex++);
        this.add(labelStorageLow);

        JLabel labelStorageHigh = new JLabel("Storage High: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelStorageHigh, configIndex++);
        this.add(labelStorageHigh);

        JLabel labelNumEdges = new JLabel("Num Edges: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelNumEdges, configIndex++);
        this.add(labelNumEdges);

        configIndex=0;
        textFieldCpuFreqLow = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldCpuFreqLow,configIndex++);
        textFieldCpuFreqHigh = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldCpuFreqHigh, configIndex++);
        textFieldRamLow = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldRamLow, configIndex++);
        textFieldRamHigh = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldRamHigh, configIndex++);
        textFieldStorageLow = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldStorageLow, configIndex++);
        textFieldStorageHigh=new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldStorageHigh, configIndex++);
        textFieldNumEdges = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldNumEdges, configIndex++);

        this.add(textFieldCpuFreqLow);
        this.add(textFieldCpuFreqHigh);
        this.add(textFieldRamLow);
        this.add(textFieldRamHigh);
        this.add(textFieldStorageLow);
        this.add(textFieldStorageHigh);
        this.add(textFieldNumEdges);
    }

    public void setEdgeGenerator(){
        this.burstLoadEdgeGenerator = new BurstLoadEdgeGenerator(Integer.parseInt(textFieldNumEdges.getText()),
                new int[]{Integer.parseInt(textFieldCpuFreqLow.getText()),Integer.parseInt(textFieldCpuFreqHigh.getText())},
                new int[]{Integer.parseInt(textFieldRamLow.getText()), Integer.parseInt(textFieldRamHigh.getText())},
                new int[]{Integer.parseInt(textFieldStorageLow.getText()), Integer.parseInt(textFieldStorageHigh.getText())});
    }

}
