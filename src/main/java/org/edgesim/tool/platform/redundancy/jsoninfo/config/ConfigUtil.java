package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import org.edgesim.tool.platform.redundancy.gui.ConnectLineInfo;
import org.edgesim.tool.platform.redundancy.gui.IconLabel;
import org.edgesim.tool.platform.redundancy.gui.button.EdgeIconButton;
import org.edgesim.tool.platform.redundancy.jsoninfo.ParaBuilder;

import java.util.*;

/**
 * 将绘制在画板上的空间的相关信息转化为系统的配置，包括对服务器、服务器之间的连接、
 * 移动设备、移动设备与边缘服务器之间的无线连接。
 *
 * @author jfqiao
 * @since 2020/01/02
 */
public class ConfigUtil {

    public static List<DeviceConfig> convertToDeviceConfig(List<IconLabel> iconLabels) {
        List<DeviceConfig> deviceConfigs = new ArrayList<>();
        for (int i = 0; i < iconLabels.size(); i++) {
            IconLabel iconLabel = iconLabels.get(i);
            if (iconLabel.getIconType() == EdgeIconButton.DEVICE_BUTTON) {
                DeviceConfig deviceConfig = iconLabel.getDeviceConfig();
                setBounds(deviceConfig, iconLabel);
                deviceConfigs.add(deviceConfig);
            }
        }
        return deviceConfigs;
    }

    public static List<EdgeConfig> convertToEdgeConfig(List<IconLabel> iconLabels) {
        List<EdgeConfig> edgeConfigs = new ArrayList<>();
        for (int i = 0; i < iconLabels.size(); i++) {
            IconLabel iconLabel = iconLabels.get(i);
            if (iconLabel.getIconType() == EdgeIconButton.EDGE_SERVER_BUTTON
                    || iconLabel.getIconType() == EdgeIconButton.CLOUD_SERVER_BUTTON) {
                EdgeConfig edgeConfig = iconLabel.getEdgeConfig();
                setBounds(edgeConfig, iconLabel);
                edgeConfigs.add(edgeConfig);
            }
        }
        return edgeConfigs;
    }

    private static void setBounds(EntityConfig entityConfig, IconLabel iconLabel) {
        entityConfig.setX(iconLabel.getX());
        entityConfig.setY(iconLabel.getY());
        entityConfig.setWidth(iconLabel.getWidth());
        entityConfig.setHeight(iconLabel.getHeight());
    }

    public static List<LinkConfig> convertToLinkConfig(List<ConnectLineInfo> connectLineInfos) {
        List<LinkConfig> linkConfigs = new ArrayList<>();
        for (int i = 0; i < connectLineInfos.size(); i++) {
            ConnectLineInfo connectLineInfo = connectLineInfos.get(i);
            LinkConfig linkConfig = connectLineInfo.getLinkConfig();
            if (linkConfig != null) {
                setLineBounds(linkConfig, connectLineInfo);
                linkConfigs.add(linkConfig);
            }
        }
        return linkConfigs;
    }

    public static List<WirelessLinkConfig> convertToWirelessConfig(List<ConnectLineInfo> connectLineInfos) {
        List<WirelessLinkConfig> wirelessLinkConfigs = new ArrayList<>();
        for (int i = 0; i < connectLineInfos.size(); i++) {
            WirelessLinkConfig wirelessLinkConfig = connectLineInfos.get(i).getWirelessLinkConfig();
            if (wirelessLinkConfig != null) {
                setLineBounds(wirelessLinkConfig, connectLineInfos.get(i));
                wirelessLinkConfigs.add(wirelessLinkConfig);
            }
        }
        return wirelessLinkConfigs;
    }

    private static void setLineBounds(LineConfig lineConfig, ConnectLineInfo connectLineInfo) {
        IconLabel start = connectLineInfo.getStartJLabel();
        IconLabel end = connectLineInfo.getEndJLabel();
        lineConfig.setX1(start.getX() + start.getWidth() / 2);
        lineConfig.setY1(start.getY() + start.getHeight() / 2);
        lineConfig.setX2(end.getX() + end.getWidth() / 2);
        lineConfig.setY2(end.getY() + end.getHeight() / 2);
    }

    public static ParaBuilder convertJsonConfigToParaBuilder(JsonConfig jsonConfig) {
        ParaBuilder paraBuilder = new ParaBuilder();
        paraBuilder.setHostNum(jsonConfig.getEdges().size());
        paraBuilder.setEdgeNum(jsonConfig.getEdges().size() - 1);
        paraBuilder.setServiceNum(jsonConfig.getApps().size());
        paraBuilder.setMobileDeviceNum(jsonConfig.getMobileDeviceNum());
        paraBuilder.setRequestNum(jsonConfig.getRequestNum());
        Map<String, Integer> edgeOrder = new HashMap<>();
        for (int i = 0; i < jsonConfig.getEdges().size(); i++) {
            EdgeConfig edgeConfig = jsonConfig.getEdges().get(i);
            if (edgeConfig.getType() == EdgeIconButton.CLOUD_SERVER_BUTTON) {
                continue;
            }
            edgeOrder.put(edgeConfig.getName(), i);
        }
        paraBuilder.setWirelessTransRate(convertWirelessConfig(jsonConfig.getWirelessLinkConfigs(), edgeOrder));
        paraBuilder.setHostBandwidth(convertLinks(jsonConfig.getLinks(), edgeOrder));
        List<Double> computingPower = nullList(jsonConfig.getEdges().size());
        List<Double> price = nullList(jsonConfig.getEdges().size());
        jsonConfig.getEdges().forEach(edgeConfig -> {
            Integer index = edgeOrder.getOrDefault(edgeConfig.getName(), edgeOrder.size());
            computingPower.set(index, edgeConfig.getComputingPower());
            price.set(index, edgeConfig.getPrice());
        });
        paraBuilder.setComputingPower(computingPower);
        paraBuilder.setPrice(price);
        Map<String, Integer> appOrder = new HashMap<>();
        for (int i = 0; i < jsonConfig.getApps().size(); i++) {
            appOrder.putIfAbsent(jsonConfig.getApps().get(i).getAppName(), i);
        }
        List<Double> inputSize = nullList(jsonConfig.getApps().size());
        List<Double> outputSize = nullList(jsonConfig.getApps().size());
        List<Double> workload = nullList(jsonConfig.getApps().size());
        jsonConfig.getApps().forEach(appConfig -> {
            int index = appOrder.get(appConfig.getAppName());
            inputSize.set(index, appConfig.getNodes().get(0).getInputSize());
            outputSize.set(index, appConfig.getNodes().get(0).getOutputSize());
            workload.set(index, appConfig.getNodes().get(0).getWorkload());

        });
        paraBuilder.setInputSize(inputSize);
        paraBuilder.setOutputSize(outputSize);
        paraBuilder.setWorkload(workload);
        paraBuilder.setMu(convertAllocationPolicy(jsonConfig.getResourceAllocationConfigs(), edgeOrder, appOrder));
        paraBuilder.setP(convertSchedulingPolicy(jsonConfig.getSchedulingConfigs(), edgeOrder, appOrder));
//        paraBuilder.setTotalArrivalRate();
        return paraBuilder;
    }

    // 将接入服务器的无线传输速度转化为数组
    private static List<Double> convertWirelessConfig(List<WirelessLinkConfig> wirelessLinkConfigs,
                                                      Map<String, Integer> edgeOrder) {
        Map<String, Double> wirelessRateMap = new HashMap<>();
        wirelessLinkConfigs.forEach(wirelessLinkConfig ->
                wirelessRateMap.putIfAbsent(wirelessLinkConfig.getEdgeName(),
                        wirelessLinkConfig.getTransmissionRate()));
        List<Double> transRate = nullList(edgeOrder.size());
        Set<String> keys = wirelessRateMap.keySet();
        keys.forEach(s -> {
            int pos = edgeOrder.get(s);
            transRate.set(pos, wirelessRateMap.get(s));
        });
        return transRate;
    }

    // 将边缘服务器与云服务器之间的连接转换为矩阵
    private static List<List<Double>> convertLinks(List<LinkConfig> linkConfigs,
                                                   Map<String, Integer> edgeOrder) {
        List<List<Double>> bandwidth = new ArrayList<>(edgeOrder.size() + 1);
        for (int i = 0; i < edgeOrder.size() + 1; i++) {
            bandwidth.add(nullList(edgeOrder.size() + 1));
        }
        linkConfigs.forEach(linkConfig -> {
            int fromIndex = edgeOrder.getOrDefault(linkConfig.getStartEdgeName(), edgeOrder.size());
            int toIndex = edgeOrder.getOrDefault(linkConfig.getEndEdgeName(), edgeOrder.size());
            bandwidth.get(fromIndex).set(toIndex, linkConfig.getUpLink());
            bandwidth.get(toIndex).set(fromIndex, linkConfig.getUpLink());
        });
        return bandwidth;
    }

    private static List<List<Double>> convertAllocationPolicy(List<ResourceAllocationConfig> allocationConfigs,
                                                              Map<String, Integer> edgeOrder,
                                                              Map<String, Integer> appOrder) {
        List<List<Double>> resourceAlloc = new ArrayList<>();
        for (int i = 0; i < edgeOrder.size() + 1; i++) {
            resourceAlloc.add(nullList(appOrder.size()));
        }
        allocationConfigs.forEach(allocConfig -> {
            int hostIndex = edgeOrder.getOrDefault(allocConfig.getEdgeName(), edgeOrder.size());
            int appIndex = appOrder.get(allocConfig.getAppName());
            resourceAlloc.get(hostIndex).set(appIndex, allocConfig.getAllocComputingPower());
        });
        return resourceAlloc;
    }

    private static List<List<List<Double>>> convertSchedulingPolicy(List<SchedulingConfig> schedulingConfigs,
                                                                    Map<String, Integer> edgeOrder,
                                                                    Map<String, Integer> appOder) {
        List<List<List<Double>>> schedulingPolicy = new ArrayList<>(appOder.size());

        for (int i = 0; i < appOder.size(); i++) {
            schedulingPolicy.add(new ArrayList<>(edgeOrder.size()));
        }
        for (int i = 0; i < appOder.size(); i++) {
            for (int j = 0; j < edgeOrder.size(); j++) {
                schedulingPolicy.get(i).add(nullList(edgeOrder.size() + 1));
            }
        }
        schedulingConfigs.forEach(schedulingConfig -> {
            int appIndex = appOder.get(schedulingConfig.getAppName());
            int accessEdgeIndex = edgeOrder.get(schedulingConfig.getAccessEdgeName());
            int targetEdgeIndex = edgeOrder.getOrDefault(schedulingConfig.getTargetEdgeName(), edgeOrder.size());
            schedulingPolicy.get(appIndex).get(accessEdgeIndex).set(targetEdgeIndex, schedulingConfig.getProbability());
        });
        return schedulingPolicy;
    }

    private static List<List<Double>> convertArrivalRate(List<DeviceConfig> deviceConfigs,
                                                  Map<String, Integer> edgeOrder,
                                                  Map<String, Integer> appOrder) {
        List<List<Double>> arrivalRate = new ArrayList<>(edgeOrder.size());
        for (int i = 0; i < edgeOrder.size(); i++) {
            arrivalRate.add(nullList(appOrder.size()));
        }
        deviceConfigs.forEach(deviceConfig -> {
            int edgeIndex = edgeOrder.get(deviceConfig.getAccessEdgeName());
            int appIndex = appOrder.get(deviceConfig.getAppName());
            arrivalRate.get(edgeIndex).set(appIndex, deviceConfig.getReqGeneratedRate());
        });
        return arrivalRate;
    }

    private static List<Double> nullList(int size) {
        List<Double> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(0.0);
        }
        return list;
    }
}
