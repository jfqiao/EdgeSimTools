package org.edgesim.tool.platform.redundancy.application;

import lombok.Data;
import org.edgesim.tool.platform.redundancy.entity.ApplicationService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @ClassName: Application
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2019-12-16 21:00
 * @Version: 1.0
 */
@Data
public class Application {
    private int appId;
    private String appName;
    /**
     * 这里Application为全局保存所有ApplicationService信息,
     * map中可能同一类ApplicationService有多个，使用不同的id进行区分。
     */
    private Map<Integer, ApplicationService> serviceMap;

    private Map<String, ApplicationServiceNode> serviceNodeMap;

    private LinkedList<ApplicationServiceNode> appGraph;


    /**
     * 全局应该保证，每一种Application全局只有一个实例。
     * @param appName
     * @param
     */
    public Application(String appName, int appId) {
        this.appName = appName;
        this.appId = appId;
        this.serviceMap = new HashMap<>();
        this.serviceNodeMap = new HashMap<>();
        this.appGraph = new LinkedList<>();
    }

    public void putAppService(ApplicationService applicationService) {
        serviceMap.put(applicationService.getId(), applicationService);
    }

    /**
     * 注意app service node必须按照顺序添加到Application中。
     * @param applicationServiceNode
     */
    public void putServiceNode(ApplicationServiceNode applicationServiceNode) {
        this.serviceNodeMap.put(applicationServiceNode.getAppServiceName(), applicationServiceNode);
    }

    public ApplicationServiceNode getAppServiceNode(String appServiceName) {
        return serviceNodeMap.get(appServiceName);
    }

}
