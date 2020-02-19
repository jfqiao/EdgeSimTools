package org.edgesim.tool.platform.redundancy.jsoninfo.generator;

import lombok.Getter;
import org.edgesim.tool.platform.redundancy.gui.button.EdgeIconButton;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.EntityConfig;
import org.edgesim.tool.platform.redundancy.gui.button.PrintIconButton;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        cloudConfig.setY(45);
    }

    public void generateEdgePos(int edgeNums) {
        edgeConfigs = new ArrayList<>();
        final int edgeLevelY = 225, edgeHGap = 28;
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
        final int mobileLevelY = 335, mobileHGap = 25, numMobilePerEdge = deviceNum / edgeConfigs.size();
        int addMobileCount = 0;
        for (int i = 0; i < edgeConfigs.size(); ++i) {
            EntityConfig edgeConfig = this.edgeConfigs.get(i);
            int mobileLeftXStart = edgeConfig.getX() + edgeConfig.getWidth() / 2 -
                    Math.min(numMobilePerEdge, deviceNum - addMobileCount) / 2 * (mobileLabelWidth + mobileHGap),
                    numCurrentAssign = numMobilePerEdge;
            if (i == edgeConfigs.size() - 1) {
                numCurrentAssign = deviceNum - addMobileCount;
            }
            for (int j = 0; j < numCurrentAssign; ++j) {
                EntityConfig deviceConfig = new EntityConfig(EdgeIconButton.DEVICE_BUTTON);
                deviceConfig.setX(mobileLeftXStart + j * (mobileLabelWidth + mobileHGap));
                deviceConfig.setY(mobileLevelY + (int)(23 * new Random().nextDouble()));
                this.deviceConfigs.add(deviceConfig);
            }
            addMobileCount += numCurrentAssign;
        }
    }
}
