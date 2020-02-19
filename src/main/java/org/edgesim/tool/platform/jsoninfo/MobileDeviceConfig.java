package org.edgesim.tool.platform.jsoninfo;

import lombok.Getter;
import lombok.Setter;

/**
 * mobile device的相关属性配置
 * @author jfqiao
 * @since 2019/10/23
 */
@Getter
@Setter
public class MobileDeviceConfig {
    private String deviceName;
    private double distPara;
    private String appName;
}
