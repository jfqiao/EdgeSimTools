package org.edgesim.tool.platform.jsoninfo.generator;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeServerBean;
import org.edgesim.tool.platform.gui.config.burstloadbeans.JsonEntitiesBean;
import org.edgesim.tool.platform.gui.ConnectLineInfo;
import org.edgesim.tool.platform.gui.EdgeGUI;
import org.edgesim.tool.platform.gui.IconLabel;
import org.edgesim.tool.platform.gui.LineShape;
import org.edgesim.tool.platform.gui.button.EdgeIconButton;
import org.edgesim.tool.platform.gui.button.PrintIconButton;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeLinkBean;
import org.edgesim.tool.platform.jsoninfo.config.burstload.BurstLoadEdgeConfig;
import org.edgesim.tool.platform.jsoninfo.config.burstload.BurstLoadLinkConfig;
import org.edgesim.tool.platform.jsoninfo.config.JsonConfig;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generating the default net structure for the problem burst load
 */
@Getter
@Setter
public class BurstLoadDefaultLayoutGenerator {

    private JPanel panelDraw;
    private JsonConfig jsonConfig;

    public BurstLoadDefaultLayoutGenerator(JPanel panelDraw) {
        this.panelDraw = panelDraw;
        this.jsonConfig = new JsonConfig();
    }

    /**
     * load edge servers and links' info from json file
     */
    public void loadDefaultInfo(String jsonInfo) throws IOException {
        JsonEntitiesBean jsonEntitiesBean = JSONObject.parseObject(jsonInfo, JsonEntitiesBean.class);
        jsonConfig.setEdgeServerBeanList(jsonEntitiesBean.getEdge_servers());
        jsonConfig.setEdgeLinkBeanList(jsonEntitiesBean.getEdge_links());
        jsonConfig.setEdgeLocBeanList(jsonEntitiesBean.getEdge_locs());
    }

    /**
     * generate default layout for `burst load` problem, the layout will be displayed on the drawPanel
     * the information of edge servers and related links is loaded from  file `test/burst_load_config.json`
     */
    public void generateDefaultLayout() {
        final int edgeIconWidth = PrintIconButton.ICON_CONFIG.get(EdgeIconButton.EDGE_SERVER_BUTTON).getIconWidth(),
                edgeIconHeight = PrintIconButton.ICON_CONFIG.get(EdgeIconButton.EDGE_SERVER_BUTTON).getIconHeight();
        if (jsonConfig.getEdgeServerBeanList() == null || jsonConfig.getEdgeLinkBeanList() == null) {
            return;
        }
        // generate default layout using the json data loaded
        List<IconLabel> tmpIconEdgeServers = new ArrayList<>();
        int i = 0;
        for (EdgeServerBean edgeServerBean : jsonConfig.getEdgeServerBeanList()) {
            IconLabel iconLabelEdgeServer = new IconLabel(EdgeIconButton.EDGE_SERVER_BUTTON,
                    PrintIconButton.ICON_CONFIG.get(EdgeIconButton.EDGE_SERVER_BUTTON).getImageIcon());
            iconLabelEdgeServer.setBounds(jsonConfig.getEdgeLocBeanList().get(i).getX(),
                    jsonConfig.getEdgeLocBeanList().get(i).getY(),
                    edgeIconWidth, edgeIconHeight);
            BurstLoadEdgeConfig edgeConfig = new BurstLoadEdgeConfig();
            edgeConfig.setName(edgeServerBean.getName());
            edgeConfig.setEdgeCpuCycle(edgeServerBean.getFreq());

//            iconLabelEdgeServer.setPROBLEM_TYPE(PlatformUtils.BURST_LOAD_EVACUATION);
            iconLabelEdgeServer.setBurstLoadEdgeConfig(edgeConfig);
            PrintIconButton.MyMouseAdapter myMouseAdapter = new PrintIconButton(0, panelDraw).
                    new MyMouseAdapter(iconLabelEdgeServer);
            iconLabelEdgeServer.addMouseListener(myMouseAdapter);
            iconLabelEdgeServer.addMouseMotionListener(myMouseAdapter);
            tmpIconEdgeServers.add(iconLabelEdgeServer);
            EdgeGUI.addedLabel.add(iconLabelEdgeServer);
            i++;
        }
        for (EdgeLinkBean linkBean : jsonConfig.getEdgeLinkBeanList()) {
            int startEdgeIdx = Integer.parseInt(linkBean.getStartEdge().substring(1)),
                    endEdgeIdx = Integer.parseInt(linkBean.getEndEdge().substring(1));
            ConnectLineInfo connectLineInfo = new ConnectLineInfo(tmpIconEdgeServers.get(startEdgeIdx),
                    tmpIconEdgeServers.get(endEdgeIdx), Color.BLACK);

            BurstLoadLinkConfig burstLoadLinkConfig = new BurstLoadLinkConfig();
            burstLoadLinkConfig.setName(linkBean.getName());
            burstLoadLinkConfig.setStartEdgeName(linkBean.getStartEdge());
            burstLoadLinkConfig.setEndEdgeName(linkBean.getEndEdge());
            burstLoadLinkConfig.setParallelWidth((int) linkBean.getMaxCapacity());
            burstLoadLinkConfig.setTransRate(linkBean.getTravelTime());

            connectLineInfo.setBurstLoadLinkConfig(burstLoadLinkConfig);
            EdgeGUI.connectLineInfos.add(connectLineInfo);
        }
        panelDraw.repaint();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (IconLabel iconLabel : EdgeGUI.addedLabel) {
                    panelDraw.add(iconLabel);
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        for (ConnectLineInfo lineInfo : EdgeGUI.connectLineInfos) {
                            new LineShape(lineInfo.getStartJLabel(), lineInfo.getEndJLabel()).
                                    display(panelDraw.getGraphics(), Color.BLACK);
                        }
                        for (IconLabel iconLabel : EdgeGUI.addedLabel) {
                            iconLabel.repaint();
                        }
                    }
                });
                panelDraw.revalidate();
            }
        });
    }
}
