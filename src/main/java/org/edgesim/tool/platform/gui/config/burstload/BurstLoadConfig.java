package org.edgesim.tool.platform.gui.config.burstload;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.LineShape;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeLocBean;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeServerBean;
import org.edgesim.tool.platform.gui.ConnectLineInfo;
import org.edgesim.tool.platform.gui.EdgeGUI;
import org.edgesim.tool.platform.gui.IconLabel;
import org.edgesim.tool.platform.gui.button.EdgeIconButton;
import org.edgesim.tool.platform.gui.button.PrintIconButton;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeLinkBean;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;
import org.edgesim.tool.platform.jsoninfo.config.burstload.BurstLoadEdgeConfig;
import org.edgesim.tool.platform.jsoninfo.config.burstload.BurstLoadLinkConfig;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Getter
@Setter
public class BurstLoadConfig {
    private EdgeGUI edgeGUI;
    private EdgeServerConfigGUI edgeServerConfigGUI;
    private LinkConfigGUI linkConfigGUI;
    private TaskConfigGUI taskConfigGUI;
    private ResultGUI resultGUI;

    public BurstLoadConfig(EdgeGUI edgeGUI) {
        this.edgeGUI = edgeGUI;
    }

    /**
     * Add burst load problem configures panel
     */
    public void addConfigPanel() {
        JPanel panelConfig = new JPanel(null);
        panelConfig.setBounds(EdgeGUIPositionConfig.CONFIG_PANEL_X, EdgeGUIPositionConfig.CONFIG_PANEL_Y,
                EdgeGUIPositionConfig.CONFIG_PANEL_WIDTH, EdgeGUIPositionConfig.CONFIG_PANEL_HEIGHT);
        edgeServerConfigGUI = new EdgeServerConfigGUI(edgeGUI);
        linkConfigGUI = new LinkConfigGUI(edgeGUI);
        taskConfigGUI = new TaskConfigGUI(edgeGUI);

        panelConfig.add(edgeServerConfigGUI);
        panelConfig.add(linkConfigGUI);
        panelConfig.add(taskConfigGUI);

        edgeGUI.getFrame().add(panelConfig);
    }

    /**
     * Add the total delay time of burst load problem
     */
    public void addResultPanel() {
        resultGUI = new ResultGUI(edgeGUI);
        edgeGUI.getFrame().add(resultGUI);
    }

    /**
     * generate configure file from the inputs in the configure panel
     */
    public void generateConfig() {
        edgeServerConfigGUI.setEdgeGenerator();
        edgeGUI.getJsonConfig().setEdgeServerBeanList(edgeServerConfigGUI.getBurstLoadEdgeGenerator().generateBurstLoadEdgeConfig());

        linkConfigGUI.setLinkGenerator(edgeGUI.getJsonConfig().getEdgeServerBeanList());
        edgeGUI.getJsonConfig().setEdgeLinkBeanList(linkConfigGUI.getBurstLoadLinkGenerator().generateLinkConfigs());

        taskConfigGUI.setTaskGenerator();
        edgeGUI.getJsonConfig().setEdgeLocBeanList(edgeServerConfigGUI.getBurstLoadEdgeGenerator().
                generateEdgeLocs(new int[]{0, edgeGUI.getDrawJPanel().getWidth()}, new int[]{0, edgeGUI.getDrawJPanel().getHeight()}));

        edgeGUI.getJsonConfig().setTaskBeanList(taskConfigGUI.getTaskGenerator().generateTask());
        // convert to display
        convertJsonConfig();
    }

    /**
     * load info from json configure file
     */
    public void convertJsonConfig() {
        edgeGUI.getDrawJPanel().removeAll();
        EdgeGUI.addedLabel = new ArrayList<>();
        EdgeGUI.connectLineInfos = new ArrayList<>();
        edgeGUI.getDrawJPanel().revalidate();
        edgeGUI.getDrawJPanel().repaint();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < edgeGUI.getJsonConfig().getEdgeServerBeanList().size(); ++i) {
                    EdgeServerBean edgeServerBean = edgeGUI.getJsonConfig().getEdgeServerBeanList().get(i);
                    EdgeLocBean locBean = edgeGUI.getJsonConfig().getEdgeLocBeanList().get(i);
                    IconLabel label;
                    label = new IconLabel(EdgeIconButton.EDGE_SERVER_BUTTON,
                            PrintIconButton.ICON_CONFIG.get(EdgeIconButton.EDGE_SERVER_BUTTON).getImageIcon());
                    label.setMouseAdapter(edgeGUI.getPrintIconButtonEdge().new MyMouseAdapter(label));
                    BurstLoadEdgeConfig edgeConfig = new BurstLoadEdgeConfig();
                    edgeConfig.setName(edgeServerBean.getName());
                    edgeConfig.setEdgeCpuCycle(edgeServerBean.getFreq());
                    label.setBurstLoadEdgeConfig(edgeConfig);
                    edgeConfig.setX(locBean.getX());
                    edgeConfig.setY(locBean.getY());
                    edgeConfig.setWidth(PrintIconButton.ICON_CONFIG.get(EdgeIconButton.EDGE_SERVER_BUTTON).getIconWidth());
                    edgeConfig.setHeight(PrintIconButton.ICON_CONFIG.get(EdgeIconButton.EDGE_SERVER_BUTTON).getIconHeight());
                    label.setPos(edgeConfig);
                    // add to draw panel
                    edgeGUI.getDrawJPanel().add(label);
                    EdgeGUI.addedLabel.add(label);
                }

                for (int i = 0; i < edgeGUI.getJsonConfig().getEdgeLinkBeanList().size(); ++i) {
                    EdgeLinkBean linkBean = edgeGUI.getJsonConfig().getEdgeLinkBeanList().get(i);
                    IconLabel labelStart = null, labelEnd = null;
                    for (int k = 0; k < EdgeGUI.addedLabel.size(); ++k) {
                        if (EdgeGUI.addedLabel.get(k).getEntityName().equals(linkBean.getStartEdge())) {
                            labelStart = EdgeGUI.addedLabel.get(k);
                        }
                        if (EdgeGUI.addedLabel.get(k).getEntityName().equals(linkBean.getEndEdge())) {
                            labelEnd = EdgeGUI.addedLabel.get(k);
                        }
                    }
                    LineShape lineShape = new LineShape(labelStart, labelEnd);
                    lineShape.setLineStroke(3);
                    ;
                    lineShape.display(edgeGUI.getDrawJPanel().getGraphics(), Color.BLACK);
                    ConnectLineInfo connectLineInfo = new ConnectLineInfo(labelStart, labelEnd, Color.BLACK);

                    BurstLoadLinkConfig burstLoadLinkConfig = new BurstLoadLinkConfig();
                    burstLoadLinkConfig.setName(linkBean.getName());
                    burstLoadLinkConfig.setStartEdgeName(linkBean.getStartEdge());
                    burstLoadLinkConfig.setEndEdgeName(linkBean.getEndEdge());
                    burstLoadLinkConfig.setParallelWidth(linkBean.getMaxCapacity());
                    burstLoadLinkConfig.setTransRate(linkBean.getTravelTime());

                    connectLineInfo.setBurstLoadLinkConfig(burstLoadLinkConfig);
                    labelStart.getLines().add(connectLineInfo);
                    labelEnd.getLines().add(connectLineInfo);
                    EdgeGUI.connectLineInfos.add(connectLineInfo);

                }
            }
        });
    }
}
