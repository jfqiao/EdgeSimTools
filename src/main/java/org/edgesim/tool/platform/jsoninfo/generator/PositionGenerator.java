package org.edgesim.tool.platform.jsoninfo.generator;

import lombok.Getter;
import org.edgesim.tool.platform.gui.button.PrintIconButton;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.EntityConfig;
import org.edgesim.tool.platform.gui.button.EdgeIconButton;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 针对配置生成对每个控件的位置信息。
 *
 * @author jfqiao
 * @since 2020/01/02
 */
@Getter
public class PositionGenerator {

    private JPanel drawPanel;
    private EntityConfig cloudConfig;
    private List<EntityConfig> edgeConfigs;
    private List<EntityConfig> deviceConfigs;

    private int cloudLabelWidth = PrintIconButton.ICON_CONFIG.get(EdgeIconButton.CLOUD_SERVER_BUTTON).getIconWidth();
    private int edgeServerLabelWidth = PrintIconButton.ICON_CONFIG.get(EdgeIconButton.EDGE_SERVER_BUTTON).getIconWidth();
    private int mobileLabelWidth = PrintIconButton.ICON_CONFIG.get(EdgeIconButton.DEVICE_BUTTON).getIconWidth();
    private int panelWidth;

    public PositionGenerator(JPanel drawPanel) {
        this.drawPanel = drawPanel;
        panelWidth = drawPanel.getWidth();
    }

    public void generateCloudPos() {
        cloudConfig = new EntityConfig(EdgeIconButton.CLOUD_SERVER_BUTTON);
        cloudConfig.setX(panelWidth / 2 - cloudLabelWidth / 2);
        cloudConfig.setY(20);
    }

    public void generateEdgePos(int edgeNums) {
        edgeConfigs = new ArrayList<>();
        final int edgeLevelY = 150, edgeHGap = 80;
        final int leftXStart = cloudConfig.getX() - edgeNums / 2 * (edgeServerLabelWidth + edgeHGap);
        for (int i = 0; i < edgeNums; ++i) {
            EntityConfig entityConfig = new EntityConfig(EdgeIconButton.EDGE_SERVER_BUTTON);
            entityConfig.setX(leftXStart + i * (edgeServerLabelWidth + edgeHGap));
            entityConfig.setY(edgeLevelY);
            edgeConfigs.add(entityConfig);
        }
    }

    public void generateMobileDevicePos(int deviceNum) {
        deviceConfigs = new ArrayList<>();
        final int mobileLevelY = 250, mobileHGap = 20, numMobilePerEdge = deviceNum / edgeConfigs.size();
        int addMobileCount = 0;
        for (int i = 0; i < edgeConfigs.size(); ++i) {
            EntityConfig edgeConfig = this.edgeConfigs.get(i);
            int mobileLeftXStart = edgeConfig.getX() + edgeConfig.getWidth() / 2 -
                    Math.min(numMobilePerEdge, deviceNum - addMobileCount) / 2 * (mobileLabelWidth + mobileHGap),
                    numCurrentAssign = numMobilePerEdge;
            if (i == edgeConfigs.size() - 1)
                numCurrentAssign = deviceNum - addMobileCount;
            for (int j = 0; j < numCurrentAssign; ++j) {
                EntityConfig deviceConfig = new EntityConfig(EdgeIconButton.DEVICE_BUTTON);
                deviceConfig.setX(mobileLeftXStart + j * (mobileLabelWidth + mobileHGap));
                deviceConfig.setY(mobileLevelY);
                this.deviceConfigs.add(deviceConfig);
            }
            addMobileCount += numCurrentAssign;
        }
    }
}
