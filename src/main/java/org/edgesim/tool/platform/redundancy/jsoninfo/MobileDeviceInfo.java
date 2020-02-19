package org.edgesim.tool.platform.redundancy.jsoninfo;

import lombok.Data;

/**
 * @ClassName: MobileDeviceInfo
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2020-01-13 11:53
 * @Version: 1.0
 */
@Data
public class MobileDeviceInfo {
    private String deviceName;
    private String appName;
    private double distance;
}
