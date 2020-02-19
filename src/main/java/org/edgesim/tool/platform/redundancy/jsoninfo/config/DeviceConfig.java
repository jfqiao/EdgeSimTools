package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.gui.button.EdgeIconButton;

import java.util.List;

/**
 * @author jfqiao
 * @since 2019/12/28
 */
@Getter
@Setter
public class DeviceConfig extends EntityConfig {
    private int mobileDeviceNum;
    private int requestNum;
    private String name;
    private double reqGeneratedRate;
    private String appName;
    private String accessEdgeName;
    private List<Integer> reqList;
    public DeviceConfig() {
        super(EdgeIconButton.DEVICE_BUTTON);
    }
}
