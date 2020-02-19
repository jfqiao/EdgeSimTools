package org.edgesim.tool.platform.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeLocBean;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeServerBean;
import org.edgesim.tool.platform.gui.config.burstloadbeans.TaskBean;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.*;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeLinkBean;

import java.util.List;

/**
 * 通过配置文件配置资源分配与流量调度问题，
 *
 * @author jfqiao
 * @since 2019/12/28
 */
@Getter
@Setter
public class JsonConfig {
    private List<DeviceConfig> devices;
    private List<EdgeConfig> edges;
    private List<AppConfig> apps;
    private List<LinkConfig> links;
    private List<WirelessLinkConfig> wirelessLinkConfigs;
    private List<ResourceAllocationConfig> resourceAllocationConfigs;
    private List<SchedulingConfig> schedulingConfigs;
    //---------------burst load configures----------------
    public List<EdgeServerBean> edgeServerBeanList;
    public List<EdgeLinkBean> edgeLinkBeanList;
    public List<EdgeLocBean> edgeLocBeanList;
    public List<TaskBean> taskBeanList;
}
