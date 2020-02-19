package org.edgesim.tool.platform.jsoninfo.generator;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeServerBean;
import org.edgesim.tool.platform.gui.ConnectLineInfo;
import org.edgesim.tool.platform.gui.EdgeGUI;
import org.edgesim.tool.platform.gui.LineShape;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeLinkBean;
import org.edgesim.tool.platform.gui.config.burstloadbeans.TaskBean;
import org.edgesim.tool.platform.log.Logger;
import org.edgesim.tool.platform.util.PlatformUtils;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

/**
 * this class load the information from json file located in `jsoninfo/test/edge_num_tasks.json` and
 * `jsoninfo/test/link_cap_status.json`, the data will be used for simulation
 */
public class BurstLoadSimulationInfo {

    private JPanel jPanelDraw;
    private EdgeGUI edgeGUI;
    private List<List<Double>> numTaskEdgeTime; // 二维列表，每个列表对应一个edge server, 每个列表的长度相同，等于最大时长
    private List<List<Double>> capLinkTime;
    private int numEdges,numTasks;

    public final static Object SYNC_SIMU_OBJ = new Object();

    public BurstLoadSimulationInfo(JPanel jPanelDraw) {
        this.jPanelDraw = jPanelDraw;
    }

    public BurstLoadSimulationInfo(JPanel jPanelDraw, EdgeGUI edgeGUI) {
        this.jPanelDraw = jPanelDraw;
        this.edgeGUI = edgeGUI;
    }

    /**
     * Load simulation needed information from json files
     */
    public void loadSimInfo() throws IOException {
        File fileEdgeTask = new File(Paths.get(System.getProperty("user.dir"), "src", "main", "java", "zju",
                "edu", "cn", "platform", "test", "edge_num_tasks.json").toString());
        File fileLinkCap = new File(Paths.get(System.getProperty("user.dir"), "src", "main", "java", "zju",
                "edu", "cn", "platform", "test", "link_cap_status.json").toString());
        int ch = 0;
        BufferedReader reader = new BufferedReader(new FileReader(fileEdgeTask));
        StringBuilder stringBuilder = new StringBuilder();
        while ((ch = reader.read()) != -1) {
            stringBuilder.append((char) ch);
        }
        reader.close();
        numTaskEdgeTime = JSONObject.parseObject(stringBuilder.toString(), List.class);
        reader = new BufferedReader(new FileReader(fileLinkCap));
        stringBuilder = new StringBuilder();
        while ((ch = reader.read()) != -1) {
            stringBuilder.append((char) ch);
        }
        reader.close();
        capLinkTime = JSONObject.parseObject(stringBuilder.toString(), List.class);

    }

    /**
     * generate pseudo simulation info
     * the burst load simulation info contains the time series status for num of tasks and link status
     */
    public void stoPseudoSimInfo() {
        List<EdgeServerBean> edgeServerBeanList = edgeGUI.getJsonConfig().getEdgeServerBeanList();
        List<EdgeLinkBean> edgeLinkBeanList = edgeGUI.getJsonConfig().getEdgeLinkBeanList();
        List<TaskBean> taskBeanList = edgeGUI.getJsonConfig().getTaskBeanList();
        numTaskEdgeTime = new ArrayList<>();
        capLinkTime = new ArrayList<>();
        Map<String, Integer> serverName2Idx = new HashMap<>();
        Map<Integer, String> serverIdx2Name = new HashMap<>();
        for (int i = 0; i < edgeServerBeanList.size(); ++i) {
            serverName2Idx.put(edgeServerBeanList.get(i).getName(), i);
        }
        for (int i = 0; i < edgeServerBeanList.size(); ++i) {
            serverIdx2Name.put(i, edgeServerBeanList.get(i).getName());
        }

        List<List<Integer>> connectMatrix = new ArrayList<>();
        for (int i = 0; i < edgeLinkBeanList.size(); ++i) {
            connectMatrix.add(new ArrayList<>());
            for (int j = 0; j < edgeServerBeanList.size(); ++j) {
                connectMatrix.get(i).add(-1);
            }
        }

        for (int i = 0; i < edgeLinkBeanList.size(); ++i) {
            EdgeLinkBean linkBean = edgeLinkBeanList.get(i);
            int idx1 = serverName2Idx.get(linkBean.getStartEdge()),
                    idx2 = serverName2Idx.get(linkBean.getEndEdge());
            connectMatrix.get(idx1).set(idx2, i);
            connectMatrix.get(idx2).set(idx1, i);
        }

        numEdges = connectMatrix.size();
        numTasks = taskBeanList.size();
        final int maxTime = 20;
        numTaskEdgeTime = new ArrayList<>();
        capLinkTime = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < edgeServerBeanList.size(); ++i) {
            numTaskEdgeTime.add(new ArrayList<>());
            int numTask = random.nextInt(taskBeanList.size());
            for (int j = 0; j < maxTime; ++j) {
                numTaskEdgeTime.get(i).add(0.0);
                numTaskEdgeTime.get(i).set(j, (double) Math.max(0, numTask--));
            }
        }
        for (int i = 0; i < edgeLinkBeanList.size(); ++i) {
            capLinkTime.add(new ArrayList<>());
            int numTask = random.nextInt(taskBeanList.size());
            for (int j = 0; j < maxTime; ++j) {
                capLinkTime.get(i).add(0.0);
                capLinkTime.get(i).set(j, (double) Math.max(0, numTask--));
            }
        }
    }

    /**
     * using the loaded data to simulation
     */
    public void startSimulation() {
        assert PlatformUtils.PROBLEM_TYPE == PlatformUtils.BURST_LOAD_EVACUATION;

        Logger.edgeGUI=edgeGUI;
        final int maxTime = numTaskEdgeTime.get(0).size();
        JLabel labelTimeTick = new JLabel();
        labelTimeTick.setBounds(20, 0, 100, 60);
        jPanelDraw.add(labelTimeTick);
        labelTimeTick.setText("Current Tick: " + 0);
        // add hint labels for edge servers and links
        List<JLabel> jLabelList4Edges = new ArrayList<>();
        List<JLabel> jLabelList4Links = new ArrayList<>();
        for (int i = 0; i < EdgeGUI.connectLineInfos.size(); ++i) {
            JLabel label = new JLabel("<html><body><span>bandwidth usage=" + 0 + "</span></body></html>");
            jLabelList4Links.add(label);
            int x = (EdgeGUI.connectLineInfos.get(i).getStartJLabel().getX() + EdgeGUI.connectLineInfos.get(i).getEndJLabel().getX()) / 2,
                    y = (EdgeGUI.connectLineInfos.get(i).getStartJLabel().getY() + EdgeGUI.connectLineInfos.get(i).getEndJLabel().getY()) / 2;
            label.setBounds(x + 50, y - 50, 80, 80);
            jPanelDraw.add(label);
        }
        for (int i = 0; i < EdgeGUI.addedLabel.size(); ++i) {
            JLabel label = new JLabel("<html><body><span>number of task on edge=" + 0 + "</span></body></html>");
            jLabelList4Edges.add(label);
            int x = EdgeGUI.addedLabel.get(i).getX() + 50, y = EdgeGUI.addedLabel.get(i).getY() - 50;
            label.setBounds(x, y, 80, 80);
            jPanelDraw.add(label);
        }
        jPanelDraw.revalidate();

        SwingUtilities.invokeLater(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                int finishTime=0;
                boolean finishTimeGet=false;
                synchronized (SYNC_SIMU_OBJ) {
                    for (int i = 0; i < maxTime; ++i) {
                        System.out.println("Simulation , tick=" + i);
                        labelTimeTick.setText("Current Tick: " + i);
                        Random random = new Random();
                        int numTask = random.nextInt(numTasks),src=random.nextInt(numEdges),dest=random.nextInt(numEdges);
                        Logger.log("num tasks " + String.valueOf(numTask)+" src "+src+"---> dest"+dest);
                        edgeGUI.getTextArea().paintImmediately(edgeGUI.getTextArea().getVisibleRect());
                        edgeGUI.getTextArea().setCaretPosition(edgeGUI.getTextArea().getText().length());
                        edgeGUI.getScrollPane().paintImmediately(edgeGUI.getScrollPane().getVisibleRect());
                        labelTimeTick.paintImmediately(labelTimeTick.getVisibleRect());
                        boolean allzero=true;
                        for (int j = 0; j < EdgeGUI.connectLineInfos.size(); ++j) {
                            if (capLinkTime.get(j).get(i) > 0) {
                                allzero=false;
                                new LineShape(EdgeGUI.connectLineInfos.get(j).getStartJLabel(),
                                        EdgeGUI.connectLineInfos.get(j).getEndJLabel()).
                                        display(jPanelDraw.getGraphics(), Color.WHITE);
                                new LineShape(EdgeGUI.connectLineInfos.get(j).getStartJLabel(),
                                        EdgeGUI.connectLineInfos.get(j).getEndJLabel()).
                                        display(jPanelDraw.getGraphics(), Color.GREEN);
                            } else {
                                new LineShape(EdgeGUI.connectLineInfos.get(j).getStartJLabel(),
                                        EdgeGUI.connectLineInfos.get(j).getEndJLabel()).
                                        display(jPanelDraw.getGraphics(), Color.BLACK);
                            }
                            jLabelList4Links.get(j).setText("<html><body><span>bandwidth usage=" +
                                    capLinkTime.get(j).get(i) + "</span></body></html>");
                            jLabelList4Links.get(j).paintImmediately(jLabelList4Links.get(j).getVisibleRect());
                        }
                        for (int j = 0; j < EdgeGUI.addedLabel.size(); ++j) {
                            EdgeGUI.addedLabel.get(j).repaint();
                            if (numTaskEdgeTime.get(j).get(i) > 0) {
                                EdgeGUI.addedLabel.get(j).setBackground(Color.GREEN);
                                allzero=false;
                            } else {
                                EdgeGUI.addedLabel.get(j).setBackground(Color.WHITE);
                            }
                            jLabelList4Edges.get(j).setText("<html><body><span>number of task on edge=" +
                                    numTaskEdgeTime.get(j).get(i) + "</span></body></html>");
                            jLabelList4Edges.get(j).paintImmediately(jLabelList4Edges.get(j).getVisibleRect());
                        }
                        Thread.sleep(400);
                        if(allzero && !finishTimeGet){
                            finishTime=i;
                            finishTimeGet=true;
                        }
                    }
                }
                edgeGUI.getBurstLoadConfig().getResultGUI().getTextFieldDelay().setText(String.valueOf(finishTime));
                jPanelDraw.remove(labelTimeTick);
                for (JLabel label : jLabelList4Edges) {
                    jPanelDraw.remove(label);
                }
                for (JLabel label : jLabelList4Links) {
                    jPanelDraw.remove(label);
                }
                jPanelDraw.revalidate();
                jPanelDraw.repaint();
                SwingUtilities.invokeLater(() -> {
                    for (ConnectLineInfo connectLineInfo : EdgeGUI.connectLineInfos) {
                        new LineShape(connectLineInfo.getStartJLabel(), connectLineInfo.getEndJLabel()).
                                display(jPanelDraw.getGraphics(), Color.BLACK);
                    }
                    for (JLabel label : EdgeGUI.addedLabel) {
                        label.repaint();
                    }
                });
            }
        });
    }
}
