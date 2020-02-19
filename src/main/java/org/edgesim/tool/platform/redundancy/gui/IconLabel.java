package org.edgesim.tool.platform.redundancy.gui;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.DeviceConfig;
import org.edgesim.tool.platform.redundancy.gui.button.EdgeIconButton;
import org.edgesim.tool.platform.redundancy.gui.button.PrintIconButton;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.EdgeConfig;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.EntityConfig;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class IconLabel extends JLabel {
    private static int DEVICE_CNT = 0;
    private static int EDGE_CNT = 0;
    private int iconType;
    private List<ConnectLineInfo> lines;
    private EdgeConfig edgeConfig;
    private DeviceConfig deviceConfig;

    public IconLabel(int iconType, Icon icon) {
        super(icon);
        this.iconType = iconType;
        this.lines = new ArrayList<>();
    }

    public void setMouseAdapter(PrintIconButton.MyMouseAdapter myMouseAdapter) {
        this.addMouseListener(myMouseAdapter);
        this.addMouseMotionListener(myMouseAdapter);
    }

    public void setConfig() {
        if (iconType == EdgeIconButton.DEVICE_BUTTON) {
            deviceConfig = new DeviceConfig();
            deviceConfig.setName(String.format("device-%d", DEVICE_CNT++));
            deviceConfig.setReqGeneratedRate(0);
        } else {
            edgeConfig = new EdgeConfig();
            if (iconType == EdgeIconButton.EDGE_SERVER_BUTTON)
                edgeConfig.setName(String.format("server-%d", EDGE_CNT++));
            else
                edgeConfig.setName("cloud-server");
            edgeConfig.setPrice(0);
            edgeConfig.setComputingPower(0);
        }
    }

    public String getEntityName() {
        if (iconType == EdgeIconButton.DEVICE_BUTTON) {
            return deviceConfig.getName();
        } else {
            return edgeConfig.getName();
        }
    }

    public void setPos(EntityConfig entityConfig) {
       this.setBounds(entityConfig.getX(), entityConfig.getY(),
               entityConfig.getWidth(), entityConfig.getHeight());
    }
}
