package org.edgesim.tool.platform.gui;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.util.PlatformUtils;
import sun.awt.image.ToolkitImage;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 记入系统的主界面，提供对问题的选择，进入不同的界面。
 *
 * @author jfqiao
 * @since 2020/01/08
 */
@Getter
@Setter
public class MainGUI {

    public static class ImagePanel extends JPanel {
        ImageIcon icon;
        Image img;

        public ImagePanel(String path) {
            //  /img/HomeImg.jpg 是存放在你正在编写的项目的bin文件夹下的img文件夹下的一个图片
            icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(PlatformUtils.getPathInResources(path)));
            img = icon.getImage();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //下面这行是为了背景图片可以跟随窗口自行调整大小，可以自己设置成固定大小
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        }

    }

    private JFrame frame;

    private EdgeGUI edgeGUI;

    private org.edgesim.tool.platform.redundancy.gui.EdgeGUI reEdgeGUI;

    private JMenuBar jMenuBar;

    private static List<ProblemDesc> problemDescList = new ArrayList<>();

    @Getter
    private static class ProblemDesc {
        private String title;
        private String desc;
        private String iconPath;
        private int problemType;

        public ProblemDesc(String title, String desc, String iconPath, int problemType) {
            this.title = title;
            this.desc = desc;
            this.title = title;
            this.iconPath = iconPath;
            this.problemType = problemType;
        }
    }

    private static String format = "<html><body><div style='color:#e9993c;font-size:18px;font-family:黑体;'>%s</div></body></html>";

    private static String descFormat = "<html><body><div style='color:#000000;font-size:13px;'>%s</div></body></html>";

    static {
        problemDescList.add(new ProblemDesc(
                "Computing Power Allocation and Traffic Scheduling for Edge Service Provisioning",
                "In the mobile edge computing scenario, services are usually deployed on multiple edge servers on the " +
                        "edge platform, and the edge server and cloud server jointly provide external services. When the edge " +
                        "device initiates a service request, after receiving the request, the access edge server forwards " +
                        "the service to the appropriate server for execution according to the forwarding policy of the edge " +
                        "platform, and obtains the final service result. This forwarding strategy minimizes request delays " +
                        "while balancing the costs associated with the resources needed to deploy the service. Therefore, " +
                        "in this system, the cost of the resources required for deployment and the average request delay " +
                        "are considered to obtain a service forwarding strategy and a deployment strategy.",
                "res_alloc.png",
                PlatformUtils.RESOURCE_ALLOC_AND_SCHEDULING_PROBLEM));
        problemDescList.add(new ProblemDesc(
                "Burst Load Evacuation Based on Dispatching and Scheduling In Edge Network",
                "In this problem, N edge servers are connected to each other according to a certain topology. Each edge server can receive the task request from the mobile terminal, forward the task request as a route, and process the task request. " +
                        "For mobile edge scenarios, when there are a large number of task requests in a certain area, the local edge server cannot effectively handle the problem of large number of task requests. Design and use algorithms to find the optimal evacuation strategy, " +
                        "In order to achieve the purpose of minimizing the time to complete all tasks.",
                "burst_load.png",
                PlatformUtils.BURST_LOAD_EVACUATION));
        problemDescList.add(new ProblemDesc(
                "Distributed Redundancy Scheduling for Microservice-based Applications at the Edge",
                "Multi-access Edge Computing (MEC) is booming as a promising paradigm to push the computation and communication " +
                        "resources from cloud to the network edge to provide services and perform computations. With container technologies, MEC enables " +
                        "mobile devices with limited battery capacity and small memory footprint to run composite microservice-based applications without " +
                        "time-consuming backbone transmission. Service deployment at the edge is of importance to put MEC from theory into practice. " +
                        "However, the state-of-the-art researches do not take the composite property of services into consideration. Besides, although " +
                        "Kubernetes has certain abilities to heal container failures, high availability of deployed microservices can not be ensured due to the " +
                        "heterogeneity and variability of edge sites.",
                "redundancy.jpg",
                PlatformUtils.SERVICE_REDUNDANCY));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainGUI window = new MainGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainGUI() {
        frame = new JFrame("EdgeSimTools");
        frame.getContentPane().setBackground(SystemColor.window);
        frame.setBounds(EdgeGUIPositionConfig.FRAME_X, EdgeGUIPositionConfig.FRAME_Y,
                EdgeGUIPositionConfig.WIN_WIDTH, EdgeGUIPositionConfig.WIN_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        init();
    }

    private void init() {
        addMenu();
        addProblem();
    }

    private void addMenu() {
        JMenu menu = new JMenu("Home");
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    frame.getContentPane().removeAll();
                    addProblem();
                    EdgeGUI.addedLabel.clear();
                    EdgeGUI.connectLineInfos.clear();
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });
        jMenuBar = new JMenuBar();
        jMenuBar.add(menu);
        frame.setJMenuBar(jMenuBar);
    }

    private void addProblem() {
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.WHITE);

        ImagePanel welcomePanel = new ImagePanel("welcome.png");
        welcomePanel.setLayout(new BorderLayout());
        int height = (int) (0.082 * frame.getContentPane().getHeight());
        welcomePanel.setBounds(0, 0, frame.getContentPane().getWidth(), height);

        JLabel title = new JLabel("<html><body><div><span style='color:#ffffff;font-size:18px;font-family:黑体;'>&nbsp;&nbsp;&nbsp;&nbsp;EdgeSimTools</span>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='color:#ffffff;font-size:14px;font-family:黑体;'>A Set of Simulators for Edge Computing</span></div></body></html>");
        title.setBounds(0, 0, welcomePanel.getWidth(), welcomePanel.getHeight());
        welcomePanel.add(title, BorderLayout.CENTER);
        frame.add(welcomePanel);

        int topGap, bottomGap, leftGap, rightGap;
        topGap = 20;
        bottomGap = 50;
        leftGap = rightGap = (int) (0.05 * frame.getContentPane().getWidth());

        JPanel panel = new JPanel();
        panel.setBounds(leftGap, topGap + welcomePanel.getHeight(), frame.getContentPane().getWidth() - leftGap - rightGap,
                frame.getContentPane().getHeight() - welcomePanel.getHeight() - topGap - bottomGap);
        panel.setBackground(Color.WHITE);

        panel.setLayout(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));
        int colHeight = (panel.getHeight() - 20) / 3;
        int colWidth = panel.getWidth() - 20;

        int picWidth = (int) (colWidth * 0.3);
        int[][] pos = new int[3][4];
        pos[0][0] = (int) (picWidth * 0.15);
        pos[0][1] = (int) (colHeight * 0.1);
        pos[0][2] = (int) (picWidth * 0.7);
        pos[0][3] = (int) (colHeight * 0.8);

        int txtWidth = colWidth - picWidth;
        pos[1][0] = (int) (txtWidth * 0.05) + picWidth;
        pos[1][1] = pos[0][1];
        pos[1][2] = (int) (txtWidth * 0.9);
        pos[1][3] = (int) (colHeight * 0.3);

        pos[2][0] = pos[1][0];
        pos[2][1] = pos[1][1] + pos[1][3];
        pos[2][2] = pos[1][2];
        pos[2][3] = (int) (colHeight * 0.6);

        for (int i = 0; i < problemDescList.size(); i++) {
            ProblemDesc problemDesc = problemDescList.get(i);
            ProblemSelection ps = new ProblemSelection(problemDesc.problemType);

            JPanel tmpPanel = new JPanel(null);
            tmpPanel.setBackground(Color.WHITE);

            JLabel imageLabel = new JLabel();
            imageLabel.setIcon(getImageIcon(problemDesc.getIconPath(), pos[0][2], pos[0][3]));
            imageLabel.addMouseListener(ps);
            imageLabel.setBounds(pos[0][0], pos[0][1], pos[0][2], pos[0][3]);
            tmpPanel.add(imageLabel);

            JLabel jLabel = new JLabel(String.format(format, problemDesc.getTitle()));
            jLabel.addMouseListener(ps);
            jLabel.setBounds(pos[1][0], pos[1][1], pos[1][2], pos[1][3]);
            tmpPanel.add(jLabel);

            JEditorPane editorPane = new JEditorPane();
            editorPane.setContentType("text/html");
            editorPane.setEditable(false);
            editorPane.setText(String.format(descFormat, problemDesc.getDesc()));
            editorPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 8));
            editorPane.setBounds(pos[2][0], pos[2][1], pos[2][2], pos[2][3]);
            tmpPanel.add(editorPane);
            editorPane.setAutoscrolls(true);

            panel.add(tmpPanel);
        }
        frame.getContentPane().add(panel);
    }

    private ImageIcon getImageIcon(String path, int width, int height) {
        ToolkitImage image = (ToolkitImage) Toolkit.getDefaultToolkit().getImage(PlatformUtils.getPathInResources(path));
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImage);
    }

    public class ProblemSelection extends MouseAdapter {

        private int problemType;

        public ProblemSelection(int problemType) {
            this.problemType = problemType;
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                PlatformUtils.PROBLEM_TYPE = problemType;
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (problemType == PlatformUtils.SERVICE_REDUNDANCY) {
                                reEdgeGUI = new org.edgesim.tool.platform.redundancy.gui.EdgeGUI(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
                                frame.getContentPane().removeAll();
                                frame.add(reEdgeGUI.getFrame());
                                frame.revalidate();
                                frame.repaint();
                            } else {
                                edgeGUI = new EdgeGUI(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
                                frame.getContentPane().removeAll();
                                frame.add(edgeGUI.getFrame());
                                frame.revalidate();
                                frame.repaint();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
