package org.edgesim.tool.platform.gui.config.burstload;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeServerBean;
import org.edgesim.tool.platform.jsoninfo.generator.BurstLoadLinkGenerator;
import org.edgesim.tool.platform.gui.EdgeGUI;
import org.edgesim.tool.platform.gui.position.ConfigPanelCalculator;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;
import org.edgesim.tool.platform.gui.position.PosConfig;

import javax.swing.*;
import java.awt.*;

import java.util.List;

@Getter
@Setter
public class LinkConfigGUI extends JPanel {

    private JTextField textFieldLinkBandWidthLow;
    private JTextField textFieldLinkBandWidthHigh;
    private JTextField textFieldLinkTransTimeLow;
    private JTextField textFieldLinkTransTimeHigh;
    private JTextField textFieldEdgeDegreeLow;
    private JTextField textFieldEdgeDegreeHigh;

//    private JTextField textFieldNumLinks;

    private final int labelLeftPadding = 10;
    private final int labelTextGap = 10;
    private final int textRightPadding = 5;

    private ConfigPanelCalculator configPanelCalculator;

    private BurstLoadLinkGenerator burstLoadLinkGenerator;

    private EdgeGUI edgeGUI;

    public LinkConfigGUI(EdgeGUI edgeGUI) {
        this.edgeGUI = edgeGUI;
        init();
    }

    private void init(){
        this.setBounds(EdgeGUIPositionConfig.SERVICE_CONFIG_X, EdgeGUIPositionConfig.SERVICE_CONFIG_Y,
                EdgeGUIPositionConfig.SERVICE_CONFIG_WIDTH,EdgeGUIPositionConfig.SERVICE_CONFIG_HEIGHT);
        configPanelCalculator = new ConfigPanelCalculator(this.getWidth(),this.getHeight(),
                6,-1,false,true);
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setLayout(null);

        int configIndex=0;
        JLabel titleLabel = new JLabel("Link Config",SwingConstants.CENTER);
        PosConfig posConfig = configPanelCalculator.getTitlePos();
        titleLabel.setBounds(posConfig.getX(),posConfig.getY(),posConfig.getWidth(),posConfig.getHeight());;
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titleLabel);

        JLabel labelBandWidthLow = new JLabel("BandWidth Low: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelBandWidthLow, configIndex++);
        this.add(labelBandWidthLow);

        JLabel labelBandWidthHigh = new JLabel("BandWidth High: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelBandWidthHigh, configIndex++);
        this.add(labelBandWidthHigh);

        JLabel labelTransTimeLow = new JLabel("Trans Time Low: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelTransTimeLow, configIndex++);
        this.add(labelTransTimeLow);

        JLabel labelTransTimeHigh = new JLabel("Trans Time High: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelTransTimeHigh, configIndex++);
        this.add(labelTransTimeHigh);

        JLabel labelDegreeLow = new JLabel("Degree Low: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelDegreeLow, configIndex++);
        this.add(labelDegreeLow);

        JLabel labelDegreeHigh = new JLabel("Degree High: ");
        ConfigPanelCalculator.setLabelPos(configPanelCalculator, labelDegreeHigh, configIndex++);
        this.add(labelDegreeHigh);

//        JLabel labelNumTasks = new JLabel("Num Tasks: ");
//        ConfigPanelCalculator.setLabelPos(configPanelCalculator,labelNumTasks,configIndex++);
//        this.add(labelNumTasks);

        configIndex=0;

        textFieldLinkBandWidthLow = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldLinkBandWidthLow, configIndex++);
        this.add(textFieldLinkBandWidthLow);

        textFieldLinkBandWidthHigh = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldLinkBandWidthHigh, configIndex++);
        this.add(textFieldLinkBandWidthHigh);

        textFieldLinkTransTimeLow = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldLinkTransTimeLow, configIndex++);;
        this.add(textFieldLinkTransTimeLow);

        textFieldLinkTransTimeHigh = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldLinkTransTimeHigh, configIndex++);
        this.add(textFieldLinkTransTimeHigh);

        textFieldEdgeDegreeLow = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldEdgeDegreeLow, configIndex++);
        this.add(textFieldEdgeDegreeLow);

        textFieldEdgeDegreeHigh = new JTextField();
        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldEdgeDegreeHigh, configIndex++);
        this.add(textFieldEdgeDegreeHigh);

//        textFieldNumLinks = new JTextField();
//        ConfigPanelCalculator.setInputPos(configPanelCalculator, textFieldNumLinks, configIndex++);
//        this.add(textFieldNumLinks);
    }

    public void setLinkGenerator(List<EdgeServerBean> edgeServerBeans){
        this.burstLoadLinkGenerator = new BurstLoadLinkGenerator(
                new int[]{Integer.parseInt(textFieldLinkBandWidthLow.getText()), Integer.parseInt(textFieldLinkBandWidthHigh.getText())},
                edgeServerBeans,
                new int[]{Integer.parseInt(textFieldEdgeDegreeLow.getText()), Integer.parseInt(textFieldEdgeDegreeHigh.getText())}
        );
    }
}
