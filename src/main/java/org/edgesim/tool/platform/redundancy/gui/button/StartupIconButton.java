package org.edgesim.tool.platform.redundancy.gui.button;

import org.edgesim.tool.platform.redundancy.gui.EdgeGUI;
import org.edgesim.tool.platform.redundancy.jsoninfo.ParaBuilder;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.ConfigUtil;
import org.edgesim.tool.platform.redundancy.log.Logger;
import org.edgesim.tool.platform.redundancy.test.OPCA_CATS;
import org.edgesim.tool.platform.redundancy.util.PlatformUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StartupIconButton extends EdgeIconButton {

    private static final String BOOT_START_ICON_PATH = "boot_start.png";

    private static final String BOOT_END_ICON_PATH = "boot_end.png";

    private static final ImageIcon BOOT_START_ICON = PlatformUtils.getImageIconByPath(BOOT_START_ICON_PATH);

    private static final ImageIcon BOOT_END_ICON = PlatformUtils.getImageIconByPath(BOOT_END_ICON_PATH);

    private EdgeGUI edgeGUI;

    public StartupIconButton(EdgeGUI edgeGUI) {
        super(START_BUTTON, "", BOOT_END_ICON_PATH);
        this.edgeGUI = edgeGUI;
        setActionListener();
    }

    private void setActionListener() {
        getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logger.edgeGUI = edgeGUI;
                startSim();
            }
        });
    }

    private void startSim() {
        if (edgeGUI.getJsonConfig() == null) {
            return;
        }
        getButton().setIcon(BOOT_START_ICON);
        getButton().repaint();
        ParaBuilder pb = ConfigUtil.convertJsonConfigToParaBuilder(edgeGUI.getJsonConfig());
        try {
            OPCA_CATS.startSim(pb, "1");
        } catch (IOException e) {
            e.printStackTrace();
        }

        edgeGUI.getCostField().setText(String.format("%.4f", pb.calculateCt()));
        edgeGUI.getDelayField().setText(String.format("%.4f", pb.calculateDt()));
        getButton().setIcon(BOOT_END_ICON);
    }
}
