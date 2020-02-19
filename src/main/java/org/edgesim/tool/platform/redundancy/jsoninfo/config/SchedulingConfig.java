package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 流量调度的配置。服务经由服务器a接收转发到服务器b的概率。
 * @author jfqiao
 * @since 2019/12/28
 */
@Setter
@Getter
public class SchedulingConfig {
    private String appName;
    private String nodeName;
    private String accessEdgeName;
    private String targetEdgeName;
    private double probability;
}
