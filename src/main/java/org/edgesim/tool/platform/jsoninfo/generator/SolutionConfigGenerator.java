package org.edgesim.tool.platform.jsoninfo.generator;

import org.edgesim.tool.platform.jsoninfo.config.resalloc.AppConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.EdgeConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.ResourceAllocationConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.SchedulingConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源分配与流量调度默认策略生成方案。
 * @author jfqiao
 * @since 2020/01/03
 */
public class SolutionConfigGenerator {

    public static List<ResourceAllocationConfig> generateResourceAllocConfig(List<AppConfig> appConfigs, List<EdgeConfig> edgeConfigs) {
        List<ResourceAllocationConfig> resourceAllocationConfigs = new ArrayList<>();
        for (int i = 0; i < appConfigs.size(); i++) {
            for (int j = 0; j < edgeConfigs.size(); j++) {
                ResourceAllocationConfig resourceAllocationConfig = new ResourceAllocationConfig();
                resourceAllocationConfig.setEdgeName(edgeConfigs.get(j).getName());
                resourceAllocationConfig.setAppName(appConfigs.get(i).getAppName());
                resourceAllocationConfig.setAppNodeName(appConfigs.get(i).getNodes().get(0).getNodeName());
                // 采用平均的方式分配资源
                resourceAllocationConfig.setAllocComputingPower(edgeConfigs.get(j).getComputingPower() / (double)appConfigs.size());
                resourceAllocationConfigs.add(resourceAllocationConfig);
            }
        }
        return resourceAllocationConfigs;
    }

    public static List<SchedulingConfig> generateShedulingConfig(List<AppConfig> appConfigs, List<EdgeConfig> edgeConfigs) {
        List<SchedulingConfig> schedulingConfigs = new ArrayList<>();
        for (int i = 0; i < appConfigs.size(); i++) {
            // 注意云服务器没有接收的
            for (int j = 0; j < edgeConfigs.size() - 1; j++) {
                for (int k = 0; k < edgeConfigs.size(); k++) {
                    SchedulingConfig schedulingConfig = new SchedulingConfig();
                    schedulingConfig.setAccessEdgeName(edgeConfigs.get(j).getName());
                    schedulingConfig.setAppName(appConfigs.get(i).getAppName());
                    schedulingConfig.setNodeName(appConfigs.get(i).getNodes().get(0).getNodeName());
                    schedulingConfig.setTargetEdgeName(edgeConfigs.get(k).getName());
                    // 采用Round-Robin的默认方式调度
                    schedulingConfig.setProbability(1.0 / edgeConfigs.size());
                    schedulingConfigs.add(schedulingConfig);
                }
            }
        }
        return schedulingConfigs;
    }
}
