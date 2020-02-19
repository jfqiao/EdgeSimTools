package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 对资源分配的配置
 * @author jfqiao
 * @since 2019/12/28
 */
@Getter
@Setter
public class ResourceAllocationConfig {
    private String appName;
    private String appNodeName;
    private String edgeName;
    private double allocComputingPower;
}
