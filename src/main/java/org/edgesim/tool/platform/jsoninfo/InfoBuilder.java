package org.edgesim.tool.platform.jsoninfo;

import com.alibaba.fastjson.JSONObject;
import org.cloudbus.cloudsim.core.SimEntity;
import org.edgesim.tool.platform.config.SimConfiguration;
import org.edgesim.tool.platform.schedule.ProbabilityScheduler;
import org.edgesim.tool.platform.util.PlatformUtils;
import org.edgesim.tool.platform.application.Application;
import org.edgesim.tool.platform.entity.ApplicationService;
import org.edgesim.tool.platform.application.ApplicationServiceNode;
import org.edgesim.tool.platform.config.BandwidthConfiguration;
import org.edgesim.tool.platform.config.MappingConfiguration;
import org.edgesim.tool.platform.distribution.PoissonDistribution;
import org.edgesim.tool.platform.entity.EdgeServer;
import org.edgesim.tool.platform.entity.MobileDevice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将Info相关类构建为模拟需要的实体类
 *
 * @author jfqiao
 * @since 2019/10/22
 */
public class InfoBuilder {

    private static final String ENTITY_TYPE_MOBILE_DEVICE = "mobile-device";
    private static final String ENTITY_TYPE_EDGE_SERVER = "edge-server";

    private static Application buildAppByInfo(AppInfo appInfo) {
        Application app = new Application(appInfo.getName(), PlatformUtils.generateApplicationId());
        for (AppInfo.Node node : appInfo.getNodes()) {
            ApplicationServiceNode serviceNode = new ApplicationServiceNode(node.getName());
            serviceNode.setInputSize(node.getInputSize());
            serviceNode.setOutputSize(node.getOutputSize());
            serviceNode.setAppName(app.getAppName());
            app.putServiceNode(serviceNode);
            MappingConfiguration.putApplicationServiceNode(serviceNode);
        }
        if (appInfo.getEdges() != null) {
            for (AppInfo.Edge edge : appInfo.getEdges()) {
                ApplicationServiceNode fromNode = app.getAppServiceNode(edge.getFrom());
                ApplicationServiceNode toNode = app.getAppServiceNode(edge.getTo());
                fromNode.setNextAppServiceNode(toNode);
                toNode.setPreAppServiceNode(fromNode);
            }
        }
        ApplicationServiceNode firstNode = null;
        for (ApplicationServiceNode node : app.getServiceNodeMap().values()) {
            if (node.getPreAppServiceNode() == null) {
                firstNode = node;
                break;
            }
        }
        app.addLinkedNode(firstNode);
        return app;
    }

    private static void buildM2mBandwidth(List<List<Double>> bandwidth) {
        for (int i = 0; i < bandwidth.size(); i++) {
            for (int j = 0; j < bandwidth.get(i).size(); j++) {
                BandwidthConfiguration.putBandwidth(MappingConfiguration.simEntities.get(i).getId(),
                        MappingConfiguration.simEntities.get(j).getId(), bandwidth.get(i).get(j));
            }
        }
    }

    private static void buildM2dBandwidth(List<BandwidthInfo> bandwidthInfos) {
        bandwidthInfos.forEach(bandwidthInfo -> {
            SimEntity to = MappingConfiguration.getEntityByName(bandwidthInfo.getTo());
            BandwidthConfiguration.putTransmissionRate(to.getId(), bandwidthInfo.getBandwidth());
        });
    }

    private static void buildProcessingInfo(List<List<Double>> processingInfo) {
        for (int i = 0; i < processingInfo.size(); i++) {
            for (int j = 0; j < processingInfo.get(i).size(); j++) {
                EdgeServer edgeServer = (EdgeServer)MappingConfiguration.simEntities.get(j);
                Application application = MappingConfiguration.applications.get(i);
                MappingConfiguration.putProcessingInfo(edgeServer.getName(), application.getAppName(),
                        processingInfo.get(i).get(j));
            }
        }
    }

    private static void buildDeploy(List<List<Integer>> deployInfo) {
        for (int i = 0; i < deployInfo.size(); i++) {
            for (int j = 0; j < deployInfo.get(i).size(); j++) {
                if (deployInfo.get(i).get(j) > 0) {
                    EdgeServer edgeServer = (EdgeServer) MappingConfiguration.simEntities.get(j);
                    ApplicationServiceNode serviceNode = MappingConfiguration.applicationServiceNodes.get(i);
                    int num = deployInfo.get(i).get(j);
                    for (int k = 0; k < num; k++) {
                        Application app = MappingConfiguration.getAppByName(serviceNode.getAppName());
                        double processingRate = MappingConfiguration.getProcessingRatePara(edgeServer.getName(), app.getAppName());
                        ApplicationService appService = new ApplicationService(serviceNode.getAppServiceName(),
                                app.getAppId(), new PoissonDistribution(processingRate), serviceNode);
                        edgeServer.deployServiceInServer(appService);
                        appService.setEdgeServerId(edgeServer.getId());
                        MappingConfiguration.putAppServiceIdAndEntityId(appService.getId(), edgeServer.getId());
                        MappingConfiguration.putAppService(appService);
                    }
                }
            }
        }
    }

    private static void buildEntities(List<EntityInfo> entityInfos) {
        for (int i = 0; i < entityInfos.size(); i++) {
            // mobile device 会按照service数量创建多个。
            if (ENTITY_TYPE_MOBILE_DEVICE.equals(entityInfos.get(i).getType())) {
//                int appNum = MappingConfiguration.applications.size();
                EntityInfo entityInfo = entityInfos.get(i);
                List<MobileDevice> devices = MappingConfiguration.devices;
                MobileDevice device = new MobileDevice(entityInfo.getName());
                device.setAccessEdgeServerId(MappingConfiguration.getEntityByName(entityInfo.getAccessEdgeServerName()).getId());
                MappingConfiguration.putEntity(device);
                devices.add(device);
//                for (int j = 0; j < appNum; j++) {
//                    MobileDevice mobileDevice = new MobileDevice(entityInfo.getName() + "-" + j);
//                    mobileDevice.setAccessEdgeServerId(MappingConfiguration.getEntityByName(entityInfo.getAccessEdgeServerName()).getId());
//                    MappingConfiguration.putEntity(mobileDevice);
//                    devices.add(mobileDevice);
//                }
            } else {
                EdgeServer simEntity = new EdgeServer(entityInfos.get(i).getName());
                simEntity.setScheduler(new ProbabilityScheduler());
                MappingConfiguration.putEntity(simEntity);
                MappingConfiguration.edgeServers.add(simEntity);
            }
        }
    }

    private static void buildApps(List<AppInfo> appInfos) {
        appInfos.forEach(appInfo -> {
            Application app = buildAppByInfo(appInfo);
            MappingConfiguration.putApp(app);
        });
    }

    private static void buildMobileDeviceInfo(List<List<Double>> mobileDeviceConfigs) {
        for (int j = 0; j < mobileDeviceConfigs.size(); j++) {
            for (int i = 0; i < mobileDeviceConfigs.get(j).size(); i++) {
                // i 表示 第 i 个 服务， j 表示 第 j 个服务器
                Application app = MappingConfiguration.applications.get(i);
                int k =  j * mobileDeviceConfigs.get(j).size() + i;
                MobileDevice md = MappingConfiguration.devices.get(k);
                md.setDistribution(new PoissonDistribution(mobileDeviceConfigs.get(j).get(i)));
                md.setAppId(app.getAppId());
                md.setAppFirstServiceName(app.getAppGraph().getFirst().getAppServiceName());
                // 置空，请求总数。
                md.setReqCnt((int)(mobileDeviceConfigs.get(j).get(i) / ParaBuilder.totalArrivalRate
                        * SimConfiguration.DEVICE_MAX_SEND_REQ_NUM )); // 每个设备产生相同数量请求。
            }
        }
//        mobileDeviceConfigs.forEach(mobileDeviceConfig -> {
//            MobileDevice mobileDevice = (MobileDevice) MappingConfiguration.getEntityByName(mobileDeviceConfig.getDeviceName());
//            mobileDevice.setScheduler(new StringBasedRoundRobinScheduler());
//            mobileDevice.setDistribution(new PoissonDistribution(mobileDeviceConfig.getDistPara()));
//            Application app = MappingConfiguration.getAppByName(mobileDeviceConfig.getAppName());
//            mobileDevice.setAppFirstServiceName(app.getAppGraph().getFirst().getAppServiceName());
//            mobileDevice.setAppId(app.getAppId());
//            mobileDevice.setReqCnt((int)(mobileDeviceConfig.getDistPara() / tmp * SimConfiguration.DEVICE_MAX_SEND_REQ_NUM));
//        });
    }

    private static void parseJsonConfig(ConfigInfo configInfo) {

        // 必须先构建app
        buildApps(configInfo.getApplications());

        // 必须先构建Entity
        buildEntities(configInfo.getEntities());

        buildM2mBandwidth(configInfo.getM2mBandwidth());
        buildM2dBandwidth(configInfo.getM2dBandwidth());

        // 服务器j对服务i的处理能力：即每秒多少个。
        buildProcessingInfo(configInfo.getProcessingInfo());

        buildDeploy(configInfo.getDeployInfo());

        // 构建mobile device信息
        buildMobileDeviceInfo(configInfo.getMobileDeviceConfig());

        // 构建转发概率模型
        buildProbability(configInfo.getTransferProbability());
    }

    private static void buildProbability(List<List<List<Double>>> probabilities) {
        for (int i = 0; i < probabilities.size(); i++) {
            for (int j = 0; j < probabilities.get(i).size(); j++) {
                for (int k = 0; k < probabilities.get(i).get(j).size(); k++) {
                    // p(i, j, k) 表示服务si由服务器hj转发到服务器hk上的概率
                    Application app = MappingConfiguration.applications.get(i);
                    EdgeServer edgeServer1 = MappingConfiguration.edgeServers.get(j);
                    EdgeServer edgeServer2 = MappingConfiguration.edgeServers.get(k);
                    // 此处将配置文件中的id转换为系统所用id。
                    MappingConfiguration.probabilityMap.put(PlatformUtils.buildProbabilityKey(app.getAppId(),
                            edgeServer1.getId(), edgeServer2.getId()),
                            probabilities.get(i).get(j).get(k));
                }
            }
        }
    }

    public static void buildByConfigFile(String configPath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(configPath));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            ConfigInfo configInfo = JSONObject.parseObject(sb.toString(), ConfigInfo.class);
            parseJsonConfig(configInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void buildByParaBuilder(ParaBuilder pb) {
        ConfigInfo configInfo = new ConfigInfo();

        // set application info
        configInfo.setApplications(new ArrayList<>());
        for (int i = 0; i < pb.getServiceNum(); i++) {
            AppInfo appInfo = new AppInfo();
            appInfo.setName("app_" + i);
            AppInfo.Node node = new AppInfo.Node();
            node.setInputSize(pb.getInputSize().get(i));
            node.setOutputSize(pb.getOutputSize().get(i));
            node.setName("node");
            appInfo.setNodes(new AppInfo.Node[]{node});
            configInfo.getApplications().add(appInfo);
        }

        // set entity info
        // 1 set host
        configInfo.setEntities(new ArrayList<>());
        for (int i = 0; i < pb.getHostNum(); i++) {
            EntityInfo entityInfo = new EntityInfo();
            entityInfo.setType("ENTITY_TYPE_EDGE_SERVER");
            if (i < pb.getHostNum() - 1) {
                entityInfo.setName("edge-server-" + (i+1));
            } else {
                entityInfo.setName("cloud-server");
            }
            configInfo.getEntities().add(entityInfo);
        }
        // 2 set mobile device
        for (int i = 0; i < pb.getEdgeNum(); i++) {
            for (int j = 0; j < pb.getServiceNum(); j++) {
                EntityInfo entityInfo = new EntityInfo();
                entityInfo.setType(ENTITY_TYPE_MOBILE_DEVICE);
                entityInfo.setName("mobile-device-" + (i+1) + "-" + (j+1));
                entityInfo.setAccessEdgeServerName("edge-server-" + (i+1));
                configInfo.getEntities().add(entityInfo);
            }
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

        // build mobile device info
        configInfo.setMobileDeviceConfig(pb.getArrivalRate());

        configInfo.setTransferProbability(pb.getP());
        System.out.println(JSONObject.toJSONString(configInfo));
        parseJsonConfig(configInfo);
    }
}

