package org.edgesim.tool.platform.redundancy.gui;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.position.EdgeGUIPositionConfig;
import org.edgesim.tool.platform.redundancy.jsoninfo.generator.AppGenerator;

import java.awt.*;

import javax.swing.*;

@Getter
@Setter
public class ServiceConfigGUI extends JPanel {

    private JFrame frmServiceConfiguration;
    private JTextField serviceNumField;
    private JTextField maxRedundancyNumField;
    private EdgeGUI parentObject;

    private final int labelLeftPadding=10;
    private final int labelTextGap=10;
    private final int textRightPadding=20;

    private AppGenerator appGenerator;

    public void setAppGenerator() {
        this.appGenerator = new AppGenerator(
                Integer.parseInt(serviceNumField.getText()),
                Integer.parseInt(maxRedundancyNumField.getText())
        );
    }

    /**
     * Create the application.
     */
    public ServiceConfigGUI(EdgeGUI parentObject) {
        this.parentObject = parentObject;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        this.setBounds(EdgeGUIPositionConfig.SERVICE_CONFIG_X, EdgeGUIPositionConfig.SERVICE_CONFIG_Y,
                EdgeGUIPositionConfig.SERVICE_CONFIG_WIDTH, EdgeGUIPositionConfig.SERVICE_CONFIG_HEIGHT_RE);
        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(Color.WHITE);

        final int labelWidth=(int)(EdgeGUIPositionConfig.SERVICE_CONFIG_WIDTH-labelLeftPadding-labelTextGap-textRightPadding)/2,
                labelHeight=(int)(EdgeGUIPositionConfig.SERVICE_CONFIG_HEIGHT/14);
        final int textFieldWidth=labelWidth,textFieldHeight=labelHeight;

        JLabel tag = new JLabel("Service Config");
        tag.setBounds(labelLeftPadding,10,textFieldWidth * 2,textFieldHeight);
        JLabel lblServiceNumber = new JLabel("<html><body>"+"Service"+"<br>"+" Number:"+"<body></html>");
        lblServiceNumber.setBounds(labelLeftPadding,10+labelHeight*2,labelWidth,labelHeight);
        JLabel lblNewLabel = new JLabel("<html><body>"+"Redundancy"+"<br>"+" Number:"+"<body></html>");
        lblNewLabel.setBounds(labelLeftPadding,10+labelHeight*4,labelWidth,labelHeight);

        serviceNumField = new JTextField();
        serviceNumField.setBounds(labelLeftPadding+labelWidth+labelTextGap,10+textFieldHeight*2,textFieldWidth,textFieldHeight);
        maxRedundancyNumField = new JTextField();
        maxRedundancyNumField.setBounds(labelLeftPadding+labelWidth+labelTextGap,10+textFieldHeight*4,textFieldWidth,textFieldHeight);

        this.add(tag);
        this.add(lblServiceNumber);
        this.add(serviceNumField);
        this.add(lblNewLabel);
        this.add(maxRedundancyNumField);
    }
}
