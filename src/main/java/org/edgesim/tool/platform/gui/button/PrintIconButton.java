package org.edgesim.tool.platform.gui.button;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.LineShape;
import org.edgesim.tool.platform.gui.propertyeditor.DeviceEditor;
import org.edgesim.tool.platform.gui.propertyeditor.ServerEditor;
import org.edgesim.tool.platform.util.PlatformUtils;
import org.edgesim.tool.platform.gui.ConnectLineInfo;
import org.edgesim.tool.platform.gui.EdgeGUI;
import org.edgesim.tool.platform.gui.IconLabel;
import org.edgesim.tool.platform.gui.propertyeditor.BurstLoadEdgeEditor;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;

@Getter
@Setter
public class PrintIconButton extends EdgeIconButton {

    public static JLabel jLabelSelected;
    public static boolean hasLabelSelected = false;

    public static Map<Integer, IconInfo> ICON_CONFIG = new HashMap<>();

    static {
        ICON_CONFIG.put(DEVICE_BUTTON, new IconInfo("设备", "device.png", 30, 30));
        ICON_CONFIG.put(EDGE_SERVER_BUTTON, new IconInfo("边缘服务器", "base_station.png", 50, 50));
        ICON_CONFIG.put(CLOUD_SERVER_BUTTON, new IconInfo("云服务器", "cloud.png", 70, 70));
    }

    @Getter
    @Setter
    public static class IconInfo {
        private String iconName;
        private String iconPath;
        private int iconWidth;
        private int iconHeight;
        private ImageIcon imageIcon;

        public IconInfo(String iconName, String iconPath, int iconWidth, int iconHeight) {
            this.iconName = iconName;
            this.iconPath = iconPath;
            this.iconWidth = iconWidth;
            this.iconHeight = iconHeight;
            setImageIcon();
        }

        private void setImageIcon() {
            Image image = Toolkit.getDefaultToolkit().getImage(PlatformUtils.getPathInResources(iconPath));
            imageIcon = new ImageIcon(image.getScaledInstance(iconWidth, iconHeight, Image.SCALE_DEFAULT));
        }
    }

    private JPanel drawPanel;
    private JPopupMenu popupMenu;

    private Runnable repaintTask = new Runnable() {
        @Override
        public void run() {
            for (ConnectLineInfo connect : EdgeGUI.connectLineInfos) {
                new LineShape(connect.getStartJLabel(), connect.getEndJLabel())
                        .display(drawPanel.getGraphics(), Color.BLACK);
            }
            for (JLabel jLabel : EdgeGUI.addedLabel) {
                jLabel.repaint();
            }
        }
    };

    public PrintIconButton(int type, JPanel drawPanel) {
        super(type, ICON_CONFIG.get(type).getIconName(), ICON_CONFIG.get(type).getIconPath());
        setPopupMenu();
        this.drawPanel = drawPanel;
        setActionListener();
    }

    private void setActionListener() {
        getButton().addActionListener(e -> {
//            drawPanel.invalidate();
            IconLabel printLabel = new IconLabel(getType(), ICON_CONFIG.get(getType()).getImageIcon());
            printLabel.setConfig();
            printLabel.setBounds(30, 30, ICON_CONFIG.get(this.getType()).getIconWidth(),
                    ICON_CONFIG.get(this.getType()).getIconHeight());
            drawPanel.add(printLabel);
            drawPanel.repaint(printLabel.getX(), printLabel.getY(), printLabel.getWidth(), printLabel.getHeight());
            EdgeGUI.addedLabel.add(printLabel);
            MouseInputAdapter adapter = new MyMouseAdapter(printLabel);
            printLabel.addMouseListener(adapter);
            printLabel.addMouseMotionListener(adapter);
        });
    }

    private void setPopupMenu() {
        // 右键点击弹出菜单
        popupMenu = new JPopupMenu();
        JMenuItem deleteJMenuItem = new JMenuItem("删除");
        JMenuItem editJMenuItem = new JMenuItem("编辑");
        popupMenu.add(deleteJMenuItem);
        popupMenu.add(editJMenuItem);
        deleteJMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 删除控件以及相关的连接
                drawPanel.remove(jLabelSelected);
                EdgeGUI.addedLabel.remove(jLabelSelected);
                Iterator<ConnectLineInfo> iterator = EdgeGUI.connectLineInfos.iterator();
                while (iterator.hasNext()) {
                    ConnectLineInfo connectLineInfo = iterator.next();
                    if (connectLineInfo.getStartJLabel() == jLabelSelected || connectLineInfo.getEndJLabel() == jLabelSelected) {
                        iterator.remove();
                    }
                }
                // 刷新界面
                drawPanel.revalidate();
                drawPanel.repaint();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Graphics graphics = drawPanel.getGraphics();
                        Graphics2D graphics2D = (Graphics2D) (graphics);
                        graphics2D.setStroke(new BasicStroke(3));
                        for (ConnectLineInfo conn : EdgeGUI.connectLineInfos) {
                            new LineShape(conn.getStartJLabel(), conn.getEndJLabel()).display(drawPanel.getGraphics(), Color.BLACK);
                        }
                        for (JLabel label : EdgeGUI.addedLabel) {
                            label.repaint();
                        }
                    }
                });
            }
        });
        // 编辑控件属性
        editJMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 判断当前选中控件的种类
                IconLabel iconLabel = (IconLabel) jLabelSelected;
                if (iconLabel.getIconType() == CLOUD_SERVER_BUTTON) {
                    ServerEditor editor = new ServerEditor("云服务器属性编辑", drawPanel.getMousePosition());
                    editor.setIconLabel(iconLabel);
                    editor.setEdgeName(iconLabel.getEdgeConfig().getName());
                    editor.setPrice(iconLabel.getEdgeConfig().getPrice());
                    editor.setComputingPower(iconLabel.getEdgeConfig().getComputingPower());
                    editor.showEditor();
                } else if (iconLabel.getIconType() == EDGE_SERVER_BUTTON) {
                    ServerEditor editor = new ServerEditor("边缘服务器属性编辑", drawPanel.getMousePosition());
                    editor.setIconLabel(iconLabel);
                    if(PlatformUtils.PROBLEM_TYPE == PlatformUtils.RESOURCE_ALLOC_AND_SCHEDULING_PROBLEM){
                        editor.setEdgeName(iconLabel.getEdgeConfig().getName());
                        editor.setPrice(iconLabel.getEdgeConfig().getPrice());
                        editor.setComputingPower(iconLabel.getEdgeConfig().getComputingPower());
                        editor.showEditor();
                    }else if(PlatformUtils.PROBLEM_TYPE == PlatformUtils.BURST_LOAD_EVACUATION){
                        String name = iconLabel.getBurstLoadEdgeConfig().getName();
                        double cpuCycle=iconLabel.getBurstLoadEdgeConfig().getEdgeCpuCycle();
                        BurstLoadEdgeEditor burstLoadEdgeEditor = new BurstLoadEdgeEditor(name,
                                cpuCycle,drawPanel.getMousePosition());
                        burstLoadEdgeEditor.showEditor();
                    }
                } else if (iconLabel.getIconType() == DEVICE_BUTTON) {
                    DeviceEditor editor = new DeviceEditor("移动端属性编辑", drawPanel.getMousePosition());
                    editor.setIconLabel(iconLabel);
                    editor.setDeviceName(iconLabel.getDeviceConfig().getName());
                    editor.setReqGeneratedRate(iconLabel.getDeviceConfig().getReqGeneratedRate());
                    editor.showEditor();
                }
            }
        });
    }

    /**
     * 绘制虚线框
     *
     * @param graphics
     * @param x
     * @param y
     * @param width
     * @param height
     */
    private void displayDottedBox(Graphics graphics, int x, int y, int width, int height) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0));
        graphics.setColor(new Color(0, 0, 255, 150));
        graphics.drawRoundRect(x, y, width, height, 10, 10);
    }

    public class MyMouseAdapter extends MouseInputAdapter {

        private Point point;
        private IconLabel printLabel;

        public MyMouseAdapter(IconLabel printLabel) {
            this.printLabel = printLabel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            System.out.println("mouse position  " + drawPanel.getMousePosition());
            if (e.getButton() == MouseEvent.BUTTON1) {
                // 触发添加连接线
                if (LineIconButton.isClicked) {
                    // 判断是否为起始节点
                    if (LineIconButton.isStartLabel) {
                        System.out.println("start label clicked.");
                        LineIconButton.startLabel = printLabel;
                        LineIconButton.isStartLabel = false;
                        drawPanel.addMouseMotionListener(new LineMotionListener(printLabel));
                    } else {
                        // 不是起始节点，直接画线
                        System.out.println("end label clicked.");
                        Graphics2D graphics2D = (Graphics2D) (drawPanel.getGraphics());
                        graphics2D.setStroke(new BasicStroke(5));
                        ((LineMotionListener) drawPanel.getMouseMotionListeners()[0]).line.
                                display(drawPanel.getGraphics(), Color.WHITE);
                        drawPanel.removeMouseMotionListener(drawPanel.getMouseMotionListeners()[0]);
                        LineShape line = new LineShape(LineIconButton.startLabel, printLabel);
                        line.setLineStroke(3);
                        line.display(drawPanel.getGraphics(), Color.BLACK);
                        LineIconButton.isClicked = false;
                        ConnectLineInfo connectLineInfo = new ConnectLineInfo(LineIconButton.startLabel, printLabel, Color.BLACK);
                        connectLineInfo.setCommonLinkConfig();
                        if (LineIconButton.startLabel.getIconType() == DEVICE_BUTTON) {
                            LineIconButton.startLabel.getDeviceConfig()
                                    .setAccessEdgeName(printLabel.getEdgeConfig().getName());
                        } else if (printLabel.getIconType() == DEVICE_BUTTON) {
                            printLabel.getDeviceConfig()
                                    .setAccessEdgeName(LineIconButton.startLabel.getEdgeConfig().getName());
                        }
                        EdgeGUI.connectLineInfos.add(connectLineInfo);
                        // 控件repaint
                        drawPanel.repaint();
                        SwingUtilities.invokeLater(repaintTask);
                    }
                } else {
                    // 如果已经有选中的Jlabel，需要判断一下当前点击的label是否是选中的label
                    // 如果当前label是之前已经选中的，则当前点击之后取消选中
                    // 如果不是，则取消之前label的选中，设置当前Label为选中状态
                    if (hasLabelSelected && jLabelSelected == printLabel) {
                        System.out.println("cancel selection");
                        hasLabelSelected = false;
                        jLabelSelected = null;
                        drawPanel.repaint();
                        SwingUtilities.invokeLater(repaintTask);
                    } else if (hasLabelSelected) {
                        System.out.println("change selection");
                        // 之前选中的是另外的label
                        jLabelSelected = printLabel;
                        drawPanel.repaint();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                for (ConnectLineInfo connect : EdgeGUI.connectLineInfos) {
                                    new LineShape(connect.getStartJLabel(), connect.getEndJLabel()).
                                            display(drawPanel.getGraphics(), Color.BLACK);
                                }
                                for (JLabel label : EdgeGUI.addedLabel) {
                                    label.repaint();
                                }
                                // 绘制虚线框
                                int width = 0, height = 0;
                                if (((IconLabel) jLabelSelected).getIconType() == DEVICE_BUTTON) {
                                    width = 40;
                                    height = 40;
                                } else if (((IconLabel) jLabelSelected).getIconType() == EDGE_SERVER_BUTTON) {
                                    width = 60;
                                    height = 60;
                                } else {
                                    width = 80;
                                    height = 80;
                                }
                                displayDottedBox(drawPanel.getGraphics(), jLabelSelected.getX() - 5,
                                        jLabelSelected.getY() - 5, width, height);
                            }
                        });
                    } else {
                        // 判断之前是否有选中连接，如果是，则需要刷新界面
                        boolean flag = false;
                        if (EdgeGUI.isConnectSelected()) {
                            EdgeGUI.clearConnSelected();
                            drawPanel.repaint();
                            flag = true;
                        }
                        final boolean needRepaint = flag;
                        System.out.println("select label");
                        // 非连接线点击，在控件周围显示虚线框，表示选中
                        hasLabelSelected = true;
                        jLabelSelected = printLabel; // 记录当前选中的控件
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (needRepaint) {
                                    for (ConnectLineInfo connect : EdgeGUI.connectLineInfos) {
                                        new LineShape(connect.getStartJLabel(), connect.getEndJLabel())
                                                .display(drawPanel.getGraphics(), Color.BLACK);
                                    }
                                    for (JLabel label : EdgeGUI.addedLabel) {
                                        label.repaint();
                                    }
                                }
                                // 绘制虚线框
                                int width = 0, height = 0;
                                if (((IconLabel) jLabelSelected).getIconType() == DEVICE_BUTTON) {
                                    width = 40;
                                    height = 40;
                                } else if (((IconLabel) jLabelSelected).getIconType() == EDGE_SERVER_BUTTON) {
                                    width = 60;
                                    height = 60;
                                } else {
                                    width = 80;
                                    height = 80;
                                }
                                displayDottedBox(drawPanel.getGraphics(), jLabelSelected.getX() - 5,
                                        jLabelSelected.getY() - 5, width, height);
                            }
                        });
                    }
                }
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                // 只有当选中控件时，右键点击才会弹出菜单
                if (hasLabelSelected) {
                    popupMenu.show(drawPanel, printLabel.getX() + 40, printLabel.getY());
                    // 鼠标右键点击弹出菜单显示，周围区域的连接会出现残缺，重绘以修复该问题
                    drawPanel.repaint();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            Graphics graphics = drawPanel.getGraphics();
                            Graphics2D graphics2D = (Graphics2D) graphics;
                            for (ConnectLineInfo connect : EdgeGUI.connectLineInfos) {
                                new LineShape(connect.getStartJLabel(), connect.getEndJLabel()).
                                        display(drawPanel.getGraphics(), Color.BLACK);
                            }
                            for (JLabel label : EdgeGUI.addedLabel) {
                                label.repaint();
                            }
                            // 绘制虚线框
                            int width = 0, height = 0;
                            if (((IconLabel) jLabelSelected).getIconType() == DEVICE_BUTTON) {
                                width = 40;
                                height = 40;
                            } else if (((IconLabel) jLabelSelected).getIconType() == EDGE_SERVER_BUTTON) {
                                width = 60;
                                height = 60;
                            } else {
                                width = 80;
                                height = 80;
                            }
                            displayDottedBox(drawPanel.getGraphics(), jLabelSelected.getX() - 5,
                                    jLabelSelected.getY() - 5, width, height);
                        }
                    });
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            Point newPoint = SwingUtilities.convertPoint(printLabel, e
                    .getPoint(), drawPanel);
            printLabel.setLocation(printLabel.getX() + (int) (newPoint.getX() - point.getX()),
                    printLabel.getY() + (int) (newPoint.getY() - point.getY()));
            point = newPoint;
            drawPanel.revalidate();
            drawPanel.repaint();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    for (ConnectLineInfo lineInfo : EdgeGUI.connectLineInfos) {
                        Graphics2D graphics2D = (Graphics2D) drawPanel.getGraphics();
                        graphics2D.setStroke(new BasicStroke(10));
                        LineShape lineShape = new LineShape(lineInfo.getStartJLabel(), lineInfo.getEndJLabel());
                        lineShape.display(drawPanel.getGraphics(), Color.BLACK);
                    }
                    for (JLabel label : EdgeGUI.addedLabel) {
                        label.repaint();
                    }
                }
            });
        }

        @Override
        public void mousePressed(MouseEvent e) {
            point = SwingUtilities.convertPoint(printLabel, e.getPoint(),
                    drawPanel);

        }
    }

    private class LineMotionListener extends MouseInputAdapter {

        private IconLabel startLabel;
        @Getter
        private LineShape line;

        public LineMotionListener(IconLabel startLabel) {
            this.startLabel = startLabel;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (line == null) {
                line = new LineShape();
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    line.setLineStroke(5);
                    line.display(drawPanel.getGraphics(), Color.WHITE);
                    for (int i = 0; i < EdgeGUI.addedLabel.size(); i++) {
//                        JLabel label = EdgeGUI.addedLabel.get(i);
                        EdgeGUI.addedLabel.get(i).repaint();
                    }
                    line.setLineStroke(3);
                    line.setPos(e.getX(), e.getY(), startLabel);
                    line.display(drawPanel.getGraphics(), Color.BLACK);
                }
            });
        }
    }
}
