package org.edgesim.tool.platform.redundancy.config;



import org.cloudbus.cloudsim.core.SimEntity;
import org.edgesim.tool.platform.redundancy.application.Application;
import org.edgesim.tool.platform.redundancy.application.ApplicationServiceNode;
import org.edgesim.tool.platform.redundancy.entity.ApplicationService;

import java.util.*;

/**
 * @ClassName: MappingConfiguration
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2019-12-16 20:36
 * @Version: 1.0
 */
public class MappingConfiguration {
    public static Map<Integer, ApplicationService> appServiceIdMapping = new HashMap<>();
    public static Map<Integer, Application> appIdMapping = new HashMap<>();
    public static Map<String, Application> appNameMapping = new HashMap<>();
    public static Map<Integer, ApplicationServiceNode> appServiceNodeIdMapping = new HashMap<>();
    public static Map<Integer, SimEntity> entityIdMapping = new HashMap<>();
    public static Map<String, SimEntity> entityNameMapping = new HashMap<>();
    public static Map<Integer, Integer> appServiceIdEntityIdMapping = new HashMap<>();
    public static Map<Integer, ArrayList<Integer>> appServiceIdEntityIdsMapping = new HashMap<>();
    public static Map<String, List<Integer>> appServiceNameAndIdMapping = new HashMap<>();
    public static Map<String, Double> processingRatePara = new HashMap<>();
    public static List<SimEntity> simEntities = new ArrayList<>();
    public static List<ApplicationServiceNode> applicationServiceNodes = new ArrayList<>();
    public static List<Application> applications = new ArrayList<>();
    public static List<ApplicationService> applicationServices = new ArrayList<>();

    public static ArrayList<? super List> routeOfMobile = new ArrayList<>();

    public static void putRouteOfMobile(ArrayList<Integer> route){
        routeOfMobile.add(route);
    }

    public static void putAppServiceIdEntityIdsMapping(Integer serviceId, ArrayList<Integer> entityIds){
        appServiceIdEntityIdsMapping.put(serviceId, entityIds);

    }

    public static ArrayList<Integer> getAppServiceIdEntityIdsMapping(Integer serviceId){
        return appServiceIdEntityIdsMapping.get(serviceId) != null ? appServiceIdEntityIdsMapping.get(serviceId) : new ArrayList<>();

    }

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
        appServiceNodeIdMapping.put(applicationServiceNode.getMsId(), applicationServiceNode);
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

}
