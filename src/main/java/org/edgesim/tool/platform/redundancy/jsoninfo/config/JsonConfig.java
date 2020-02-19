package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 通过配置文件配置资源分配与流量调度问题，
 * @author jfqiao
 * @since 2019/12/28
 */
@Getter
@Setter
public class JsonConfig {
    private int mobileDeviceNum;
    private int requestNum;
    private List<EdgeConfig> edges;
    private List<DeviceConfig> devices;
    private List<AppConfig> apps;
    private List<LinkConfig> links;
    private List<WirelessLinkConfig> wirelessLinkConfigs;
    private List<ResourceAllocationConfig> resourceAllocationConfigs;
    private List<SchedulingConfig> schedulingConfigs;

}
