package org.edgesim.tool.platform.redundancy.jsoninfo;

import com.alibaba.fastjson.JSONObject;
import org.cloudbus.cloudsim.core.SimEntity;
import org.edgesim.tool.platform.redundancy.entity.MobileDevice;
import org.edgesim.tool.platform.redundancy.application.Application;
import org.edgesim.tool.platform.redundancy.config.DistanceConfiguration;
import org.edgesim.tool.platform.redundancy.entity.ApplicationService;
import org.edgesim.tool.platform.redundancy.application.ApplicationServiceNode;
import org.edgesim.tool.platform.redundancy.config.MappingConfiguration;
import org.edgesim.tool.platform.redundancy.entity.EdgeServer;
import org.edgesim.tool.platform.redundancy.util.PlatformUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 将Info相关类构建为模拟需要的实体类
 *
 * @author jfqiao
 * @since 2019/10/22
 */
public class InfoBuilder {

    private static final String ENTITY_TYPE_MOBILE_DEVICE = "mobile-device";
    private static final String ENTITY_TYPE_EDGE_SERVER = "edge-server";

    public static int mdCounter = 0;

    private static void parseJsonConfig(ConfigInfo configInfo, int mobileDeviceNum) {

        // 必须先构建Entity
        buildEntities(configInfo.getEntities());

        buildS2sDistance(configInfo.getS2sDistance());

        // 必须先构建app
        buildApps(configInfo.getApplications());


        buildDeploy(configInfo.getDeployInfo());

        buildMobileDeviceInfo(mobileDeviceNum);

        initializeRequestRouteOfMobile(mobileDeviceNum);

    }

    public static void buildEntities(List<EntityInfo> entityInfos) {
        for (EntityInfo entityInfo: entityInfos) {
            SimEntity entity = buildEntityByInfo(entityInfo);
            MappingConfiguration.putEntity(entity);
        }
        entityInfos.forEach(entityInfo -> {
            if (ENTITY_TYPE_MOBILE_DEVICE.equals(entityInfo.getType())) {
                MobileDevice md = (MobileDevice) MappingConfiguration.getEntityByName(entityInfo.getName());
                md.setAccessEdgeServerId(MappingConfiguration.getEntityByName(entityInfo.getAccessEdgeServerName()).getId());
                md.setMdId(mdCounter++);
            }
        });
    }

    private static SimEntity buildEntityByInfo(EntityInfo entityInfo) {
        switch (entityInfo.getType()) {
            case ENTITY_TYPE_EDGE_SERVER:
                return new EdgeServer(entityInfo.getName());
            case ENTITY_TYPE_MOBILE_DEVICE:
                return new MobileDevice(entityInfo.getName());
            default:
                throw new RuntimeException("Wrong entity type info.");
        }
    }

    public static void buildS2sDistance(List<List<Double>> distance) {
        for (int i = 0; i < distance.size(); i++) {
            for (int j = 0; j < distance.get(i).size(); j++) {
                DistanceConfiguration.putDistance(MappingConfiguration.simEntities.get(i).getId(),
                        MappingConfiguration.simEntities.get(j).getId(), distance.get(i).get(j));
            }
        }
    }

    public static void buildApps(List<AppInfo> appInfos) {
        appInfos.forEach(appInfo -> {
            Application app = buildAppByInfo(appInfo);
            MappingConfiguration.putApp(app);
        });
    }

    private static Application buildAppByInfo(AppInfo appInfo) {
        Application app = new Application(appInfo.getName(),PlatformUtils.generateApplicationId());
        for (AppInfo.Node node : appInfo.getNodes()) {
            ApplicationServiceNode serviceNode = new ApplicationServiceNode(node.getName());
            serviceNode.setAppName(app.getAppName());
            serviceNode.setMsId(node.getId());
            app.putServiceNode(serviceNode);
            MappingConfiguration.putApplicationServiceNode(serviceNode);
            SimEntity entity = new ApplicationService(node.getName(), serviceNode);
            MappingConfiguration.putEntity(entity);
        }
        return app;
    }



    public static void buildDeploy(List<List<Integer>> deployInfo) {
        for (int i = 0; i < deployInfo.size(); i++) {
            EdgeServer edgeServer = (EdgeServer) MappingConfiguration.simEntities.get(i);
            for (int j = 0; j < deployInfo.get(i).size(); j++) {
                if (deployInfo.get(i).get(j) >= 0) {
                    int num = deployInfo.get(i).get(j);
                    ArrayList<Integer> entityIds = MappingConfiguration.getAppServiceIdEntityIdsMapping(num);
                    entityIds.add(edgeServer.getId());
                    MappingConfiguration.putAppServiceIdEntityIdsMapping(num, entityIds);
                }
            }
        }
    }

    public static void buildMobileDeviceInfo(int hostNum) {
        for (int i = 0; i < mdCounter; i++) {
            MobileDevice mobileDevice = (MobileDevice) MappingConfiguration.getEntityById(i + hostNum + 2);
            Application app = MappingConfiguration.getAppByName("app_0");
            mobileDevice.setAppId(app.getAppId());
            /**
             * random distance
             */
            double random = new Random().nextDouble() * 10;
            DistanceConfiguration.putDistance(mobileDevice.getId(), mobileDevice.getAccessEdgeServerId(), random);
        }
    }

    private static void initializeRequestRouteOfMobile(int mobileDeviceNum) {
        ArrayList<Integer> route = new ArrayList<>(3);
        int target;
        /**
         * random request sequence
         */
        for (int i = 0; i < mobileDeviceNum; i++) {
            for (int j = 0; j < 3; j++) {
                target = new Random().nextInt(11);
                route.add(target);
            }
            MappingConfiguration.putRouteOfMobile(route);
        }

    }


    public static void buildByParaBuilder(ParaBuilder pb) {
        ConfigInfo configInfo = new ConfigInfo();

        // set entity info
        // 1 set host
        configInfo.setEntities(new ArrayList<>());
        for (int i = 0; i < pb.getHostNum(); i++) {
            EntityInfo entityInfo = new EntityInfo();
            entityInfo.setType(ENTITY_TYPE_EDGE_SERVER);
            if (i < 1) {
                entityInfo.setName("cloud-server");
            } else {
                entityInfo.setName("edge-server-" + i);
            }
            configInfo.getEntities().add(entityInfo);
        }
        List<List<Double>> distance = new ArrayList<>(pb.getEdgeNum());
        for (int i = 0; i < pb.getEdgeNum(); i++) {
            List<Double> dis = new ArrayList<>(pb.getEdgeNum());
            for (int j = 0; j <= i; j++) {
                if (i == j){
                    dis.add(j,0.0);
                }
                else{
                    /**
                     * random distance
                     */
                    dis.add(j, (new Random().nextDouble() * 10));
                }
            }
            distance.add(dis);
        }
        configInfo.setS2sDistance(distance);

        // set application info
        configInfo.setApplications(new ArrayList<>());
        for (int i = 0; i < pb.getServiceNum(); i++) {
            AppInfo appInfo = new AppInfo();
            appInfo.setName("app_" + i);
            AppInfo.Node node = new AppInfo.Node();
            node.setId(i);
            node.setInputSize(pb.getInputSize().get(i));
            node.setOutputSize(pb.getOutputSize().get(i));
            node.setName("node");
            appInfo.setNodes(new AppInfo.Node[]{node});
            configInfo.getApplications().add(appInfo);
        }

        // 2 set mobile device
        for (int i = 0; i < pb.getMobileDeviceNum(); i++) {
            EntityInfo entityInfo = new EntityInfo();
            entityInfo.setType(ENTITY_TYPE_MOBILE_DEVICE);
            entityInfo.setName("mobile-device-" + i);
            int random = new Random().nextInt(10) + 1;
            entityInfo.setAccessEdgeServerName("edge-server-" + random);
            configInfo.getEntities().add(entityInfo);
        }

        // set host to host bandwidth
        configInfo.setM2mBandwidth(pb.getHostBandwidth());

        // set device to host bandwidth
        configInfo.setM2dBandwidth(new ArrayList<>());
        for (int i = 0; i < pb.getEdgeNum(); i++) {
            BandwidthInfo bandwidthInfo = new BandwidthInfo();
            bandwidthInfo.setTo("edge-server-" + (i+1));
            bandwidthInfo.setBandwidth(pb.getWirelessTransRate().get(i));
            configInfo.getM2dBandwidth().add(bandwidthInfo);
        }

        // build processing info
        List<List<Double>> processingInfo = new ArrayList<>(pb.getServiceNum());
        for (int i = 0; i < pb.getServiceNum(); i++) {
            processingInfo.add(new ArrayList<>(pb.getHostNum()));
            for (int j = 0; j < pb.getHostNum(); j++) {
                processingInfo.get(i).add(0.0);
            }
        }
        configInfo.setProcessingInfo(processingInfo);

        for (int j = 0; j < pb.getHostNum(); j++) {
            for (int i = 0; i < pb.getServiceNum(); i++) {
                processingInfo.get(i).set(j, pb.getMu().get(j).get(i));

            }
        }

        configInfo.setDeployInfo(new ArrayList<>(pb.getServiceNum()));
        for (int i = 0; i < pb.getServiceNum(); i++) {
            configInfo.getDeployInfo().add(new ArrayList<>(pb.getHostNum()));
            for (int j = 0; j < pb.getHostNum(); j++) {
                configInfo.getDeployInfo().get(i).add(0);
            }
        }

        for (int i = 0; i < pb.getServiceNum(); i++) {
            for (int j = 0; j < pb.getHostNum(); j++) {
                if (processingInfo.get(i).get(j) > 0) {
                    configInfo.getDeployInfo().get(i).set(j, 1);
                }
            }
        }


        configInfo.setTransferProbability(pb.getP());
        System.out.println(JSONObject.toJSONString(configInfo));
        parseJsonConfig(configInfo, pb.getHostNum());
    }
}

