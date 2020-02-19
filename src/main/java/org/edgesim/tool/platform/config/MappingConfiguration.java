package org.edgesim.tool.platform.config;


import org.cloudbus.cloudsim.core.SimEntity;
import org.edgesim.tool.platform.application.Application;
import org.edgesim.tool.platform.entity.ApplicationService;
import org.edgesim.tool.platform.application.ApplicationServiceNode;
import org.edgesim.tool.platform.entity.EdgeServer;
import org.edgesim.tool.platform.entity.MobileDevice;

import java.util.*;

/**
 * 保存全局映射信息.
 * @author jfqiao
 * @since 2019/10/20
 */
public class MappingConfiguration {

    public static Map<Integer, ApplicationService> appServiceIdMapping = new HashMap<>();

    public static Map<Integer, Application> appIdMapping = new HashMap<>();

    public static Map<String, Application> appNameMapping = new HashMap<>();

    public static Map<String, ApplicationServiceNode> appServiceNodeNameMapping = new HashMap<>();

    public static Map<Integer, SimEntity> entityIdMapping = new HashMap<>();

    public static Map<String, SimEntity> entityNameMapping = new HashMap<>();

    public static Map<Integer, Integer> appServiceIdEntityIdMapping = new HashMap<>();

    public static Map<String, List<Integer>> appServiceNameAndIdMapping = new HashMap<>();

    public static Map<String, Double> processingRatePara = new HashMap<>();

    public static List<SimEntity> simEntities = new ArrayList<>();

    public static List<EdgeServer> edgeServers = new ArrayList<>();

    public static List<ApplicationServiceNode> applicationServiceNodes = new ArrayList<>();

    public static List<Application> applications = new ArrayList<>();

    public static List<ApplicationService> applicationServices = new ArrayList<>();

    public static List<MobileDevice> devices = new ArrayList<>();

    public static Map<String, Double> probabilityMap = new HashMap<>();

    public static void putAppService(ApplicationService applicationService) {
        appServiceIdMapping.put(applicationService.getId(), applicationService);
        String appServiceName = applicationService.getAppServiceName();
        int appServiceId = applicationService.getId();
        appServiceNameAndIdMapping.computeIfAbsent(appServiceName, k -> new ArrayList<>());
        appServiceNameAndIdMapping.get(appServiceName).add(appServiceId);
        applicationServices.add(applicationService);
//        entityIdMapping.put(applicationService.getId(), applicationService);
//        entityNameMapping.put(applicationService.getAppServiceName(), applicationService);
    }

    public static void putApp(Application application) {
        appIdMapping.put(application.getAppId(), application);
        appNameMapping.put(application.getAppName(), application);
        applications.add(application);
    }

    public static void putApplicationServiceNode(ApplicationServiceNode applicationServiceNode) {
        applicationServiceNodes.add(applicationServiceNode);
        appServiceNodeNameMapping.put(applicationServiceNode.getAppServiceName(), applicationServiceNode);
    }

    public static void putEntity(SimEntity simEntity) {
        entityIdMapping.put(simEntity.getId(), simEntity);
        entityNameMapping.put(simEntity.getName(), simEntity);
        simEntities.add(simEntity);
    }

    public static void putAppServiceIdAndEntityId(int appServiceId, int entityId) {
        appServiceIdEntityIdMapping.put(appServiceId, entityId);
    }

    public static void putProcessingInfo(String machineName, String appServiceName, double processingRate) {
        // 指数分布的lambda参数为泊松分布的倒数。
        processingRatePara.put(machineName + SimConfiguration.STRING_SEPARATOR + appServiceName, processingRate);
    }

    public static SimEntity getEntityById(int id) {
        return entityIdMapping.get(id);
    }

    public static SimEntity getEntityByName(String name) {
        return entityNameMapping.get(name);
    }

    public static Application getAppById(int id) {
        return appIdMapping.get(id);
    }

    public static Application getAppByName(String appName) {
        return appNameMapping.get(appName);
    }

    public static ApplicationService getAppServiceById(int id) {
        return appServiceIdMapping.get(id);
    }

    public static List<Integer> getAppServicesByName(String appServiceName) {
        return appServiceNameAndIdMapping.get(appServiceName);
    }

    public static int getEntityIdByAppServiceId(int appServiceId) {
        return appServiceIdEntityIdMapping.get(appServiceId);
    }

    public static Set<String> getEntityNames() {
        return entityNameMapping.keySet();
    }

    public static double getProcessingRatePara(String machineName, String appServiceName) {
        return processingRatePara.get(machineName + SimConfiguration.STRING_SEPARATOR + appServiceName);
    }
}

