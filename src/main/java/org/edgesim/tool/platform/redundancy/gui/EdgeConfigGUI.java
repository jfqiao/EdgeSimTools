package org.edgesim.tool.platform.redundancy.gui;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;
import org.edgesim.tool.platform.redundancy.jsoninfo.generator.EdgeGenerator;
import org.edgesim.tool.platform.redundancy.jsoninfo.generator.PositionGenerator;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.EdgeConfig;
import org.edgesim.tool.platform.redundancy.jsoninfo.generator.LinkGenerator;

import java.awt.*;
import java.util.List;
import java.util.Random;

import javax.swing.*;

@Getter
@Setter
public class EdgeConfigGUI extends JPanel{

    private JTextField edgeNumField;
    private JTextField appCapacityField;

    private final int labelLeftPadding=10;
    private final int labelTextGap=10;
    private final int textRightPadding=5;

    private EdgeGenerator edgeGenerator;

    private LinkGenerator linkGenerator;

    private EdgeGUI parentGui;

    public void setEdgeGenerator(PositionGenerator positionGenerator) {
        this.edgeGenerator = new EdgeGenerator(
                Integer.parseInt(edgeNumField.getText()),
                Integer.parseInt(appCapacityField.getText()),
                positionGenerator
        );
    }

    public void setLinkGenerator(List<EdgeConfig> edgeConfigs) {
        double distance = 10 * new Random().nextDouble();
        this.linkGenerator = new LinkGenerator(edgeConfigs, distance);
    }

    /**
     * Create the application.
     */
    public EdgeConfigGUI(EdgeGUI parentEdgeGUI) {
        this.parentGui = parentEdgeGUI;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        this.setBounds(EdgeGUIPositionConfig.EDGE_CONFIG_X, EdgeGUIPositionConfig.EDGE_CONFIG_Y,
                EdgeGUIPositionConfig.EDGE_CONFIG_WIDTH, EdgeGUIPositionConfig.EDGE_CONFIG_HEIGHT_RE);
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setLayout(null);

        final int labelWidth =(int)(( EdgeGUIPositionConfig.EDGE_CONFIG_WIDTH-labelLeftPadding-labelTextGap-textRightPadding)/2);
        final int textFieldWidth=labelWidth;
        final int labelHeight=(int)(EdgeGUIPositionConfig.EDGE_CONFIG_HEIGHT/14);
        final int textHeight=labelHeight;


        JLabel tag = new JLabel("Edge Server Config");
        tag.setBounds(labelLeftPadding,10,textFieldWidth*2,textHeight);
        JLabel lblServiceNumber = new JLabel("Edge Number:");
        lblServiceNumber.setBounds(labelLeftPadding,10+labelHeight*2,labelWidth,labelHeight);
        JLabel lblNewLabel = new JLabel("App Capacity:");
        lblNewLabel.setBounds(labelLeftPadding,10+labelHeight*4,labelWidth,labelHeight);

        edgeNumField = new JTextField();
        edgeNumField.setBounds(labelLeftPadding+labelWidth+labelTextGap,10+labelHeight*2,textFieldWidth,textHeight);
        appCapacityField = new JTextField();
        appCapacityField.setBounds(labelLeftPadding+labelWidth+labelTextGap,10+labelHeight*4,textFieldWidth,textHeight);

        this.add(tag);
        this.add(lblServiceNumber);
        this.add(edgeNumField);
        this.add(lblNewLabel);
        this.add(appCapacityField);
    }
}