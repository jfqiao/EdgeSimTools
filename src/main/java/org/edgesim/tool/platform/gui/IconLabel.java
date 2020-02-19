package org.edgesim.tool.platform.gui;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.button.EdgeIconButton;
import org.edgesim.tool.platform.gui.button.PrintIconButton;
import org.edgesim.tool.platform.jsoninfo.config.burstload.BurstLoadEdgeConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.DeviceConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.EdgeConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.EntityConfig;
import org.edgesim.tool.platform.util.PlatformUtils;

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
    // --------variables for problem burst load--------------
    private String iconName;
    private List<ConnectLineInfo> linesBurstLoad;
    private BurstLoadEdgeConfig burstLoadEdgeConfig;

    public IconLabel(int iconType, Icon icon) {
        super(icon);
        this.iconType = iconType;
        this.lines = new ArrayList<>();
    }

    /**
     * burst load problem constructor
     *
     * @param linesBurstLoad
     * @param burstLoadEdgeConfig
     */
    public IconLabel(List<ConnectLineInfo> linesBurstLoad, BurstLoadEdgeConfig burstLoadEdgeConfig) {
        this.linesBurstLoad = linesBurstLoad;
        this.burstLoadEdgeConfig = burstLoadEdgeConfig;
    }

    public void setMouseAdapter(PrintIconButton.MyMouseAdapter myMouseAdapter) {
        this.addMouseListener(myMouseAdapter);
        this.addMouseMotionListener(myMouseAdapter);
    }

    public void setConfig() {
        if (PlatformUtils.PROBLEM_TYPE == PlatformUtils.RESOURCE_ALLOC_AND_SCHEDULING_PROBLEM) {
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
    }

    public String getEntityName() {
        if (PlatformUtils.PROBLEM_TYPE == PlatformUtils.RESOURCE_ALLOC_AND_SCHEDULING_PROBLEM) {
            if (iconType == EdgeIconButton.DEVICE_BUTTON) {
                return deviceConfig.getName();
            } else {
                return edgeConfig.getName();
            }
        } else if (PlatformUtils.PROBLEM_TYPE == PlatformUtils.BURST_LOAD_EVACUATION) {
            return burstLoadEdgeConfig.getName();
        } else {
            return "";
        }
    }

    public void setPos(EntityConfig entityConfig) {
        this.setBounds(entityConfig.getX(), entityConfig.getY(),
                entityConfig.getWidth(), entityConfig.getHeight());
    }
}
