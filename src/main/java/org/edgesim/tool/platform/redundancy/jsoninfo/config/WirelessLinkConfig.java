package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 针对无线传输的配置类。
 * @author jfqiao
 * @since 2019/12/28
 */
@Getter
@Setter
public class WirelessLinkConfig extends LineConfig {
    private String deviceName;
    private String edgeName;
    private double transmissionRate;
}
