package org.edgesim.tool.platform.redundancy.jsoninfo.generator;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.gui.EdgeGUI;
import org.edgesim.tool.platform.redundancy.gui.LineShape;
import org.edgesim.tool.platform.redundancy.gui.ConnectLineInfo;
import org.edgesim.tool.platform.redundancy.gui.IconLabel;
import org.edgesim.tool.platform.redundancy.gui.button.EdgeIconButton;
import org.edgesim.tool.platform.redundancy.gui.button.PrintIconButton;

import javax.swing.*;
import java.awt.*;

/**
 * The generator to generate default layout
 * This class needs to know the edge server number and the mobile device number
 * The panel to display this generated connection structure is needed
 * The default layout will be in a tree like structure, the root is a cloud Server
 * Since the EdgeGUI has static member to record the label and connections added to the panel, this class
 *      will not offer corresponds methods repeatedly.
 */
@Getter
@Setter
public class DefaultLayoutGenerator {
    private int edgeServerNum;
    private int mobileDeviceNum;
    private JPanel panelDraw;

    public DefaultLayoutGenerator(int edgeServerNum, int mobileDeviceNum, JPanel panelDraw) {
        this.edgeServerNum = edgeServerNum;
        this.mobileDeviceNum = mobileDeviceNum;
        this.panelDraw = panelDraw;

        generateLayout();
    }

    public DefaultLayoutGenerator(JPanel panelDraw) {
        this.panelDraw = panelDraw;
        this.edgeServerNum=5;
        this.mobileDeviceNum=10;

        generateLayout();
    }

    public void generateLayout(){
        final int cloudLabelWidth=PrintIconButton.ICON_CONFIG.get(EdgeIconButton.CLOUD_SERVER_BUTTON).getIconWidth(),
                cloudLabelHeight=PrintIconButton.ICON_CONFIG.get(EdgeIconButton.CLOUD_SERVER_BUTTON).getIconHeight(),
                edgeServerLabelWidth=PrintIconButton.ICON_CONFIG.get(EdgeIconButton.EDGE_SERVER_BUTTON).getIconWidth(),
                edgeServerLabelHeight=PrintIconButton.ICON_CONFIG.get(EdgeIconButton.EDGE_SERVER_BUTTON).getIconHeight(),
                mobileLabelWidth=PrintIconButton.ICON_CONFIG.get(EdgeIconButton.DEVICE_BUTTON).getIconWidth(),
                mobileLabelHeight=PrintIconButton.ICON_CONFIG.get(EdgeIconButton.DEVICE_BUTTON).getIconHeight(),
                panelWidth=panelDraw.getWidth();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IconLabel iconLabelCloud=new IconLabel(EdgeIconButton.CLOUD_SERVER_BUTTON,
                        PrintIconButton.ICON_CONFIG.get(EdgeIconButton.CLOUD_SERVER_BUTTON).getImageIcon());
                iconLabelCloud.setBounds(panelWidth/2-cloudLabelWidth/2,20, cloudLabelWidth,cloudLabelHeight);
                panelDraw.add(iconLabelCloud);
                EdgeGUI.addedLabel.add(iconLabelCloud); // record the added label
                // add edge servers
                final int edgeLevelY=150,edgeHGap=100;
                final int leftXStart=iconLabelCloud.getX()-edgeServerNum/2*(edgeServerLabelWidth+edgeHGap);
                final int mobileLevelY=250,mobileHGap=60,numMobilePerEdge=mobileDeviceNum/edgeServerNum;
                int addMobileCount=0;
                for(int i=0;i<edgeServerNum;++i){
                    IconLabel iconLabelEdgeServer=new IconLabel(EdgeIconButton.EDGE_SERVER_BUTTON,
                            PrintIconButton.ICON_CONFIG.get(EdgeIconButton.EDGE_SERVER_BUTTON).getImageIcon());
                    iconLabelEdgeServer.setBounds(leftXStart+i*(edgeServerLabelWidth+edgeHGap),
                            edgeLevelY,edgeServerLabelWidth,edgeServerLabelHeight);
                    // connect cloud server and each edge server, only save the connection info, the line will be
                    // drawn at last
                    EdgeGUI.connectLineInfos.add(new ConnectLineInfo(iconLabelCloud,iconLabelEdgeServer, Color.BLACK));
                    panelDraw.add(iconLabelEdgeServer);
                    EdgeGUI.addedLabel.add(iconLabelEdgeServer);
                    // add mobile devices to current edge server
                    int mobileLeftXStart=iconLabelEdgeServer.getX()+iconLabelEdgeServer.getWidth()/2-
                            Math.min(numMobilePerEdge,mobileDeviceNum-addMobileCount)/2*(mobileLabelWidth+mobileHGap),
                            numCurrentAssign=numMobilePerEdge;
                    if(i==edgeServerNum-1)
                        numCurrentAssign=mobileDeviceNum-addMobileCount;
                    for(int j=0;j<numCurrentAssign;++j){
                        IconLabel iconLabelMobileDevice=new IconLabel(EdgeIconButton.DEVICE_BUTTON,
                                PrintIconButton.ICON_CONFIG.get(EdgeIconButton.DEVICE_BUTTON).getImageIcon());
                        iconLabelMobileDevice.setBounds(mobileLeftXStart+j*(mobileLabelWidth+mobileHGap),
                                mobileLevelY,mobileLabelWidth,mobileLabelHeight);
                        // record this label
                        EdgeGUI.addedLabel.add(iconLabelMobileDevice);
                        // save connection info
                        EdgeGUI.connectLineInfos.add(new ConnectLineInfo(iconLabelEdgeServer,iconLabelMobileDevice,Color.BLACK));
                        panelDraw.add(iconLabelMobileDevice);
                    }
                    addMobileCount += numCurrentAssign;
                }
                panelDraw.revalidate();
                panelDraw.repaint();
                // display the connections
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        for(ConnectLineInfo connect : EdgeGUI.connectLineInfos){
                            new LineShape(connect.getStartJLabel(),connect.getEndJLabel()).display(panelDraw.getGraphics(),Color.BLACK);
                        }
                        for(JLabel label : EdgeGUI.addedLabel){
                            label.repaint();
                        }
                    }
                });
            }
        });
    }

}
