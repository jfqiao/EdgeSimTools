package org.edgesim.tool.platform.application;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.entity.ApplicationService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 应用的表示类。
 * 根据对场景的抽象理解，一个应用为一连串服务的组合，例如服务s1, s2, s3共同组成应用a1,
 * 应用的请求方式为：device -> s1 -> s2 -> s3，s3处理完毕后向后返回处理结果，s3 -> s2 -> s1 -> device.
 * @author jfqiao
 * @since 2019/10/20
 */
@Getter
@Setter
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
     * @param appId
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

    public void addLinkedNode(ApplicationServiceNode firstNode) {
        // 不许有环存在
        while (firstNode != null) {
            appGraph.add(firstNode);
            firstNode = firstNode.getNextAppServiceNode();
        }
    }

}
