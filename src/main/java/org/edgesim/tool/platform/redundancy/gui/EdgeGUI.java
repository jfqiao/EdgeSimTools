package org.edgesim.tool.platform.redundancy.gui;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;
import org.edgesim.tool.platform.redundancy.gui.button.*;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.*;
import org.edgesim.tool.platform.redundancy.gui.propertyeditor.LinkEditor;
import org.edgesim.tool.platform.redundancy.jsoninfo.generator.PositionGenerator;
import org.edgesim.tool.platform.redundancy.jsoninfo.generator.SolutionConfigGenerator;
import org.edgesim.tool.platform.redundancy.util.PlatformUtils;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

@Getter
@Setter
public class EdgeGUI {

    // 保存添加的label控件
    public static List<IconLabel> addedLabel = new ArrayList<>();
    // 保存添加的直线
    public static List<ConnectLineInfo> connectLineInfos = new ArrayList<>();

    private JPanel frame;
    private JLabel lblCost;
    private JTextField costField;
    private JTextField delayField;
    private JLabel lblDelay;
    private JPanel drawJPanel;
    private JPanel lineJPanel;
    private JTextArea textArea;

    private ServiceConfigGUI serviceConfigGUI;
    private EdgeConfigGUI edgeConfigGUI;
    private MobileDeviceConfigGUI mobileDeviceConfigGUI;
    private  GeneralConfigGUI generalConfigGUI;

    private JPopupMenu popupMenu; // 在主界面上右键点击弹出菜单
    private ConnectLineInfo connectSelected;

    private JsonConfig jsonConfig;

    private PositionGenerator positionGenerator;

    private PrintIconButton printIconButtonDevice, printIconButtonEdge, printIconButtonCloud;
    public static PanelMouseListener panelMouseListener;

    /**
     * Launch the application.
     */
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    EdgeGUI window = new EdgeGUI();
//                    window.frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * Create the application.
     */
    public EdgeGUI() {
        initialize();
    }

    public EdgeGUI(int width, int height) {
        frame = new JPanel();
        frame.setBackground(SystemColor.window);
        frame.setBounds(0, 0, width, height);
        frame.setLayout(null);

        initialize();
    }


    private void initialize() {
//        frame = new JFrame();
//        frame.getContentPane().setBackground(SystemColor.window);
//        frame.setBounds(EdgeGUIPositionConfig.WIN_X, EdgeGUIPositionConfig.WIN_Y,
//                EdgeGUIPositionConfig.WIN_WIDTH, EdgeGUIPositionConfig.WIN_HEIGHT);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().setLayout(null);

        textArea = new JTextArea(); // 运行日志输出
        textArea.setBackground(Color.WHITE);
        textArea.setBounds(EdgeGUIPositionConfig.LOG_X, EdgeGUIPositionConfig.LOG_Y, EdgeGUIPositionConfig.LOG_WIDTH, EdgeGUIPositionConfig.LOG_HEIGHT);
        textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, EdgeGUIPositionConfig.LOG_BORDER));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(textArea);
        scrollPane.createVerticalScrollBar();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(EdgeGUIPositionConfig.LOG_X, EdgeGUIPositionConfig.LOG_Y, EdgeGUIPositionConfig.LOG_WIDTH, EdgeGUIPositionConfig.LOG_HEIGHT);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(scrollPane);
//        frame.getContentPane().add(scrollPane);
//        frame.getContentPane().add(textArea);

        drawJPanel = new JPanel(null);   // 绘图区域
        drawJPanel.setBackground(Color.WHITE);
        drawJPanel.setBounds(EdgeGUIPositionConfig.DRAW_PANEL_X, EdgeGUIPositionConfig.DRAW_PANEL_Y,
                EdgeGUIPositionConfig.DRAW_PANEL_WIDTH, EdgeGUIPositionConfig.DRAW_PANEL_HEIGHT);
        drawJPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        PanelMouseListener panelMouseListener1 = new PanelMouseListener();
        panelMouseListener = panelMouseListener1;
        drawJPanel.addMouseListener(panelMouseListener1);
        frame.add(drawJPanel);
//        frame.getContentPane().add(drawJPanel);

        JMenuItem menuItemDelete = new JMenuItem("删除");
        JMenuItem menuItemEdit = new JMenuItem("编辑");
        popupMenu = new JPopupMenu();
        popupMenu.add(menuItemDelete);
        popupMenu.add(menuItemEdit);
        menuItemDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 删除选中的连接
                connectLineInfos.remove(connectSelected);
                // 刷新界面
                drawJPanel.repaint();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        for (ConnectLineInfo connect : connectLineInfos) {
                            Graphics graphics = drawJPanel.getGraphics();
                            Graphics2D graphics2D = (Graphics2D) graphics;
                            graphics2D.setStroke(new BasicStroke(3));
                            new LineShape(connect.getStartJLabel(), connect.getEndJLabel()).display(graphics, Color.BLACK);
                        }
                        // 控件刷新
                        for (JLabel label : addedLabel) {
                            label.repaint();
                        }
                    }
                });
            }
        });
        menuItemEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 弹出连接编辑框
                LinkEditor linkEditor = new LinkEditor("连接属性编辑窗口", frame.getMousePosition());
                linkEditor.setConnectLineInfo(connectSelected);
                linkEditor.showEditor();
            }
        });
        setMenuPanel();
//        frame.setResizable(false);

        addConfigsPanel();
        setCostDelayArea();
        jsonConfig = new JsonConfig();

    }


    /**
     * 在主面板上添加配置项
     */
    private void addConfigsPanel() {
        if (PlatformUtils.PROBLEM_TYPE == PlatformUtils.RESOURCE_ALLOC_AND_SCHEDULING_PROBLEM) {
            JPanel panelConfigs = new JPanel(null);
            panelConfigs.setBounds(EdgeGUIPositionConfig.CONFIG_PANEL_X, EdgeGUIPositionConfig.CONFIG_PANEL_Y,
                    EdgeGUIPositionConfig.CONFIG_PANEL_WIDTH, EdgeGUIPositionConfig.CONFIG_PANEL_HEIGHT);

            serviceConfigGUI = new ServiceConfigGUI(this); // 传入自身，获取数据
            edgeConfigGUI = new EdgeConfigGUI(this);
            mobileDeviceConfigGUI = new MobileDeviceConfigGUI(this);
            generalConfigGUI = new GeneralConfigGUI(this);

            panelConfigs.add(edgeConfigGUI);
            panelConfigs.add(serviceConfigGUI);
            panelConfigs.add(mobileDeviceConfigGUI);
            panelConfigs.add(generalConfigGUI);

            frame.add(panelConfigs);
        } else if (PlatformUtils.PROBLEM_TYPE == PlatformUtils.BURST_LOAD_EVACUATION) {
            // 添加三个配置项。

        }
    }

    private void setCostDelayArea() {
        // cost 与 delay 展示区域
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(EdgeGUIPositionConfig.RESULT_X, EdgeGUIPositionConfig.RESULT_Y,
                EdgeGUIPositionConfig.RESULT_WIDTH, EdgeGUIPositionConfig.RESULT_HEIGHT);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(panel);
//        frame.getContentPane().add(panel);

        lblCost = new JLabel("Cost:");
//        lblCost.setPreferredSize(new Dimension(60, 50));
        lblCost.setBounds(10, 10, 100, 100);
        panel.add(lblCost);
        lblCost.setHorizontalAlignment(SwingConstants.CENTER);

        costField = new JTextField();
        costField.setBounds(120, 40, 80, 40);
        panel.add(costField);

        lblDelay = new JLabel("Delay:");
//        lblDelay.setPreferredSize(new Dimension(60, 20));
        lblDelay.setBounds(220, 10, 100, 100);
        panel.add(lblDelay);
        lblDelay.setHorizontalAlignment(SwingConstants.CENTER);

        delayField = new JTextField();
        delayField.setBounds(330, 40, 80, 40);
        panel.add(delayField);
    }

    private void setMenuPanel() {
        // 顶部工具栏
        JPanel headJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
        headJPanel.setBackground(Color.WHITE);
        headJPanel.setBounds(EdgeGUIPositionConfig.MENU_HEAD_PANEL_X, EdgeGUIPositionConfig.MENU_HEAD_PANEL_Y,
                EdgeGUIPositionConfig.MENU_HEAD_PANEL_WIDTH, EdgeGUIPositionConfig.MENU_HEAD_PANEL_HEIGHT);

        printIconButtonDevice = new PrintIconButton(EdgeIconButton.DEVICE_BUTTON, drawJPanel);
        printIconButtonEdge = new PrintIconButton(EdgeIconButton.EDGE_SERVER_BUTTON, drawJPanel);
        printIconButtonCloud = new PrintIconButton(EdgeIconButton.CLOUD_SERVER_BUTTON, drawJPanel);
        headJPanel.add(printIconButtonDevice.getButton());
        headJPanel.add(printIconButtonEdge.getButton());
        headJPanel.add(printIconButtonCloud.getButton());
        headJPanel.add(new LineIconButton().getButton());
        headJPanel.add(new ImportIconButton(this).getButton());
        headJPanel.add(new ExportIconButton(this).getButton());
        headJPanel.add(new StartupIconButton(this).getButton());
        headJPanel.add(new StatisticsButton().getButton());

        ConfirmConfigButton confirmConfigButton = new ConfirmConfigButton();
        confirmConfigButton.setEdgeGUI(this);
        headJPanel.add(confirmConfigButton.getButton());

        frame.add(headJPanel);
    }

    public void generateConfig() {
        positionGenerator = new PositionGenerator(drawJPanel);
        edgeConfigGUI.setEdgeGenerator(positionGenerator);
        jsonConfig.setEdges(edgeConfigGUI.getEdgeGenerator().generateEdge());

        edgeConfigGUI.setLinkGenerator(jsonConfig.getEdges());
        jsonConfig.setLinks(edgeConfigGUI.getLinkGenerator().generateLink());

        serviceConfigGUI.setAppGenerator();
        jsonConfig.setApps(serviceConfigGUI.getAppGenerator().generateApp());

        mobileDeviceConfigGUI.setDeviceGenerator(jsonConfig.getEdges(), jsonConfig.getApps(), positionGenerator);
        jsonConfig.setDevices(mobileDeviceConfigGUI.getDeviceGenerator().generateDeviceConfigs());
        jsonConfig.setWirelessLinkConfigs(mobileDeviceConfigGUI.getDeviceGenerator().generateWirelessConfig(jsonConfig.getDevices()));
        jsonConfig.setResourceAllocationConfigs(SolutionConfigGenerator.generateResourceAllocConfig(jsonConfig.getApps(),
                jsonConfig.getEdges()));
        jsonConfig.setSchedulingConfigs(SolutionConfigGenerator.generateShedulingConfig(jsonConfig.getApps(),
                jsonConfig.getEdges()));
        // 生成配置后，需要将配置转化出来。

        jsonConfig.setMobileDeviceNum(mobileDeviceConfigGUI.getDeviceGenerator().getMobileDeviceNum());
        jsonConfig.setRequestNum(mobileDeviceConfigGUI.getDeviceGenerator().getRequestNum());
        convertJsonConfig();
        repaintTask.run();
    }

    public void convertJsonConfig() {
        drawJPanel.removeAll();
        EdgeGUI.addedLabel = new ArrayList<>();
        EdgeGUI.connectLineInfos = new ArrayList<>();
        for (int i = 0; i < this.getJsonConfig().getEdges().size(); i++) {
            EdgeConfig edgeConfig = this.getJsonConfig().getEdges().get(i);
            IconLabel iconLabel;
            if (i == this.getJsonConfig().getEdges().size() - 1) {
                iconLabel = new IconLabel(EdgeIconButton.CLOUD_SERVER_BUTTON,
                        PrintIconButton.ICON_CONFIG.get(EdgeIconButton.CLOUD_SERVER_BUTTON).getImageIcon());
            } else {
                iconLabel = new IconLabel(EdgeIconButton.EDGE_SERVER_BUTTON,
                        PrintIconButton.ICON_CONFIG.get(EdgeIconButton.EDGE_SERVER_BUTTON).getImageIcon());
            }
            iconLabel.setMouseAdapter(printIconButtonEdge.new MyMouseAdapter(iconLabel));
            iconLabel.setEdgeConfig(edgeConfig);
            iconLabel.setPos(edgeConfig);
            // 加入到画板中
            this.getDrawJPanel().add(iconLabel);
            EdgeGUI.addedLabel.add(iconLabel);
        }
        for (int i = 0; i < this.getJsonConfig().getDevices().size(); i++) {
            DeviceConfig deviceConfig = this.getJsonConfig().getDevices().get(i);
            IconLabel iconLabel = new IconLabel(EdgeIconButton.DEVICE_BUTTON,
                    PrintIconButton.ICON_CONFIG.get(EdgeIconButton.DEVICE_BUTTON).getImageIcon());
            iconLabel.setMouseAdapter(printIconButtonDevice.new MyMouseAdapter(iconLabel));
            iconLabel.setDeviceConfig(deviceConfig);
            iconLabel.setPos(deviceConfig);
            this.getDrawJPanel().add(iconLabel);
            EdgeGUI.addedLabel.add(iconLabel);
        }
        drawJPanel.revalidate();
        drawJPanel.repaint();
        for (int i = 0; i < this.getJsonConfig().getWirelessLinkConfigs().size(); i++) {
            WirelessLinkConfig wirelessLinkConfig = this.getJsonConfig().getWirelessLinkConfigs().get(i);
            IconLabel startLabel = null;
            IconLabel endLabel = null;
            for (int k = 0; k < EdgeGUI.addedLabel.size(); k++) {
                if (EdgeGUI.addedLabel.get(k).getEntityName().equals(wirelessLinkConfig.getDeviceName())) {
                    startLabel = EdgeGUI.addedLabel.get(k);
                }
                if (EdgeGUI.addedLabel.get(k).getEntityName().equals(wirelessLinkConfig.getEdgeName())) {
                    endLabel = EdgeGUI.addedLabel.get(k);
                }
            }
            LineShape lineShape = new LineShape(startLabel, endLabel);
            lineShape.setLineStroke(3);
            lineShape.display(this.getDrawJPanel().getGraphics(), Color.BLACK);
            ConnectLineInfo connectLineInfo = new ConnectLineInfo(startLabel, endLabel, Color.BLACK);
            connectLineInfo.setWirelessLinkConfig(wirelessLinkConfig);
            startLabel.getLines().add(connectLineInfo);
            endLabel.getLines().add(connectLineInfo);
            EdgeGUI.connectLineInfos.add(connectLineInfo);
        }
        for (int i = 0; i < this.getJsonConfig().getLinks().size(); i++) {
            LinkConfig linkConfig = this.getJsonConfig().getLinks().get(i);
            IconLabel startLabel = null;
            IconLabel endLabel = null;
            for (int k = 0; k < EdgeGUI.addedLabel.size(); k++) {
                if (EdgeGUI.addedLabel.get(k).getEntityName().equals(linkConfig.getStartEdgeName())) {
                    startLabel = EdgeGUI.addedLabel.get(k);
                }
                if (EdgeGUI.addedLabel.get(k).getEntityName().equals(linkConfig.getEndEdgeName())) {
                    endLabel = EdgeGUI.addedLabel.get(k);
                }
            }
            LineShape lineShape = new LineShape(startLabel, endLabel);
            lineShape.setLineStroke(3);
            lineShape.display(this.getDrawJPanel().getGraphics(), Color.BLACK);
            ConnectLineInfo connectLineInfo = new ConnectLineInfo(startLabel, endLabel, Color.BLACK);
            connectLineInfo.setLinkConfig(linkConfig);
            startLabel.getLines().add(connectLineInfo);
            endLabel.getLines().add(connectLineInfo);
            EdgeGUI.connectLineInfos.add(connectLineInfo);
        }
    }

    /**
     * 用于在外部判断是否有连接被选中
     *
     * @return
     */
    public static boolean isConnectSelected() {
        return panelMouseListener.hasConnSelected;
    }

    public static void clearConnSelected() {
        panelMouseListener.hasConnSelected = false;
    }

    private Runnable repaintTask = new Runnable() {
        @Override
        public void run() {
            for (ConnectLineInfo conn : connectLineInfos) {
                new LineShape(conn.getStartJLabel(), conn.getEndJLabel())
                        .display(drawJPanel.getGraphics(), Color.BLACK);
            }
            for (JLabel label : addedLabel) {
                label.repaint();
            }
        }
    };

    /**
     * jpanel 区域鼠标监听
     */
    private class PanelMouseListener extends MouseAdapter {
        private boolean hasConnSelected = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            // 仅监听连接选中，控件的点击在PrintIconButton中处理
            if (e.getButton() == MouseEvent.BUTTON1) {
                ConnectLineInfo connectLineInfoCurrent = null;
                for (ConnectLineInfo connect : connectLineInfos) {
                    if (connect.dotLineDistance(e.getX(), e.getY()) <= 50) {
                        connectLineInfoCurrent = connect;
                        break;
                    }
                }
                if (connectLineInfoCurrent != null) {
                    if (!hasConnSelected) {
                        // 如果之前选中了Label,清除选中状态
                        boolean needRepaint = false;
                        if (PrintIconButton.hasLabelSelected) {
                            PrintIconButton.hasLabelSelected = false;
                            PrintIconButton.jLabelSelected = null;
                            drawJPanel.repaint();
                            needRepaint = true;
                        }
                        // 之前没有选中过连接，设置当前连接为选中状态，绘制虚线框
                        // 绘制虚线框
                        Graphics graphics = drawJPanel.getGraphics();
                        Graphics2D graphics2D = (Graphics2D) graphics;
                        graphics2D.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                0, new float[]{9}, 0));
                        graphics.setColor(new Color(0, 0, 255, 150));
                        int x = Math.min(connectLineInfoCurrent.getStartJLabel().getX(), connectLineInfoCurrent.getEndJLabel().getX()),
                                y = Math.min(connectLineInfoCurrent.getStartJLabel().getY(), connectLineInfoCurrent.getEndJLabel().getY()),
                                width = Math.abs(connectLineInfoCurrent.getStartJLabel().getX() - connectLineInfoCurrent.getEndJLabel().getX()),
                                height = Math.abs(connectLineInfoCurrent.getStartJLabel().getY() - connectLineInfoCurrent.getEndJLabel().getY());
                        if (needRepaint) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    for (ConnectLineInfo connect : connectLineInfos) {
                                        new LineShape(connect.getStartJLabel(), connect.getEndJLabel()).
                                                display(drawJPanel.getGraphics(), Color.BLACK);
                                    }
                                    for (JLabel label : addedLabel) {
                                        label.repaint();
                                    }
                                    graphics.drawRoundRect(x + 15, y + 15,
                                            width + 5, height + 5, 10, 10);
                                }
                            });
                        } else {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    graphics.drawRoundRect(x + 15, y + 15,
                                            width + 5, height + 5, 10, 10);
                                }
                            });
                        }
                        hasConnSelected = true;
                        connectSelected = connectLineInfoCurrent;
                    } else if (connectSelected != connectLineInfoCurrent) {
                        // 当前选中的连接不是之前选中的，更新选中目标
                        connectSelected = connectLineInfoCurrent;
                        drawJPanel.repaint();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                for (ConnectLineInfo conn : connectLineInfos) {
                                    new LineShape(conn.getStartJLabel(), conn.getEndJLabel())
                                            .display(drawJPanel.getGraphics(), Color.BLACK);
                                }
                                for (JLabel label : addedLabel) {
                                    label.repaint();
                                }
                                // 绘制虚线框
                                Graphics graphics = drawJPanel.getGraphics();
                                Graphics2D graphics2D = (Graphics2D) graphics;
                                graphics2D.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                        0, new float[]{9}, 0));
                                graphics.setColor(new Color(0, 0, 255, 150));
                                int x = Math.min(connectSelected.getStartJLabel().getX(), connectSelected.getEndJLabel().getX()),
                                        y = Math.min(connectSelected.getStartJLabel().getY(), connectSelected.getEndJLabel().getY()),
                                        width = Math.abs(connectSelected.getStartJLabel().getX() - connectSelected.getEndJLabel().getX()),
                                        height = Math.abs(connectSelected.getStartJLabel().getY() - connectSelected.getEndJLabel().getY());
                                graphics.drawRoundRect(x + 15, y + 15,
                                        width + 5, height + 5, 10, 10);
                            }
                        });
                    } else {
                        // 再次点击当前连接，取消选中
                        hasConnSelected = false;
                        connectSelected = null;
                        drawJPanel.repaint();
                        SwingUtilities.invokeLater(repaintTask);
                    }
                } else {
                    // 当前左键点击没有选中连接或label，判断之前是否选中连接，如果有，则清除，并刷新界面
                    if (hasConnSelected) {
                        hasConnSelected = false;
                        connectSelected = null;
                    }
                    if (PrintIconButton.hasLabelSelected) {
                        PrintIconButton.hasLabelSelected = false;
                        PrintIconButton.jLabelSelected = null;
                    }
                    drawJPanel.repaint();
                    SwingUtilities.invokeLater(repaintTask);
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                // 鼠标右键点击
                if (hasConnSelected) {
                    popupMenu.show(drawJPanel, e.getX() + 40, e.getY());
                    // 鼠标右键点击弹出菜单显示，周围区域的连接会出现残缺，重绘以修复该问题
                    drawJPanel.repaint();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            Graphics graphics = drawJPanel.getGraphics();
                            Graphics2D graphics2D = (Graphics2D) graphics;
                            graphics2D.setStroke(new BasicStroke(3));
                            for (ConnectLineInfo connect : connectLineInfos) {
                                new LineShape(connect.getStartJLabel(), connect.getEndJLabel()).
                                        display(drawJPanel.getGraphics(), Color.BLACK);
                            }
                            for (JLabel label : addedLabel) {
                                label.repaint();
                            }
                            // 同时显示选中连接周围的虚线框
                            // 绘制虚线框
                            graphics2D.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                    0, new float[]{9}, 0));
                            graphics.setColor(new Color(0, 0, 255, 150));
                            int x = Math.min(connectSelected.getStartJLabel().getX(), connectSelected.getEndJLabel().getX()),
                                    y = Math.min(connectSelected.getStartJLabel().getY(), connectSelected.getEndJLabel().getY()),
                                    width = Math.abs(connectSelected.getStartJLabel().getX() - connectSelected.getEndJLabel().getX()),
                                    height = Math.abs(connectSelected.getStartJLabel().getY() - connectSelected.getEndJLabel().getY());
                            graphics.drawRoundRect(x + 15, y + 15,
                                    width + 5, height + 5, 10, 10);
                        }
                    });
                }
            }
        }
    }
}
