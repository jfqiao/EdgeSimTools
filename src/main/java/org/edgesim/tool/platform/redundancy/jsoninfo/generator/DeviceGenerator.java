package org.edgesim.tool.platform.redundancy.jsoninfo.generator;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.AppConfig;
import org.edgesim.tool.platform.redundancy.distribution.TruncatedNormalDistribution;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.DeviceConfig;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.EdgeConfig;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.WirelessLinkConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class DeviceGenerator {
    // 边缘服务器数量与服务种类数量联合确定移动设备数量：edgeNum * appNum
    private List<EdgeConfig> edgeConfigs;
    private List<AppConfig> appConfigs;
    // 同一个接入服务器的所有移动设备无线传输速率相同
    private double transmissionRateMean;
    private double transmissionRateVar;
    // 每中服务在每个边缘服务器上的请求产生速率
    private double reqGeneratedRateMean;
    private double reqGeneratedRateVar;

    private int mobileDeviceNum;
    private int requestNum;

    private PositionGenerator positionGenerator;

    private TruncatedNormalDistribution transmissionRateDist;
    private TruncatedNormalDistribution reqGeneratedRateDist;

    public DeviceGenerator(List<EdgeConfig> edgeConfigs, List<AppConfig> appConfigs,
                           double transmissionRateMean, double transmissionRateVar,
                           double reqGeneratedRateMean, double reqGeneratedRateVar,
                           PositionGenerator positionGenerator) {
        this.edgeConfigs = edgeConfigs;
        this.appConfigs = appConfigs;
        this.transmissionRateMean = transmissionRateMean;
        this.transmissionRateVar = transmissionRateVar;
        this.reqGeneratedRateMean = reqGeneratedRateMean;
        this.reqGeneratedRateVar = reqGeneratedRateVar;
        this.transmissionRateDist = new TruncatedNormalDistribution(transmissionRateMean, transmissionRateVar,
                transmissionRateMean - transmissionRateVar, transmissionRateMean + transmissionRateVar);
        this.reqGeneratedRateDist = new TruncatedNormalDistribution(reqGeneratedRateMean, reqGeneratedRateVar,
                reqGeneratedRateMean - reqGeneratedRateVar, reqGeneratedRateMean + reqGeneratedRateVar);
        this.positionGenerator = positionGenerator;
    }

    public DeviceGenerator(List<EdgeConfig> edgeConfigs, List<AppConfig> appConfigs,
                           int mobileDeviceNum, int requestNum,
                           PositionGenerator positionGenerator){
        this.edgeConfigs = edgeConfigs;
        this.appConfigs = appConfigs;
        this.mobileDeviceNum = mobileDeviceNum;
        this.requestNum = requestNum;
        this.positionGenerator = positionGenerator;
    }

    public List<DeviceConfig> generateDeviceConfigs() {
        positionGenerator.generateMobileDevicePos((edgeConfigs.size() - 1));
        // 每种服务，都在每个边缘服务器上产生一个移动设备。
        List<DeviceConfig> deviceConfigs = new ArrayList<>();
        // 排除云服务器
        String defaultDeviceName = "mobile-device-%d-%d";
        for (int i = 0; i < edgeConfigs.size() - 1; i++) {
            int size = new Random().nextInt(3);
            for (int j = 0; j < 1; j++) {
                DeviceConfig deviceConfig = new DeviceConfig();
                deviceConfig.setAppName(appConfigs.get(j).getAppName());
                deviceConfig.setMobileDeviceNum(mobileDeviceNum);
                deviceConfig.setRequestNum(requestNum);
                deviceConfig.setName(String.format(defaultDeviceName, i, j));
                deviceConfig.setAccessEdgeName(edgeConfigs.get(i).getName());
                int cnt = i;
                deviceConfig.setX(positionGenerator.getDeviceConfigs().get(cnt).getX());
                deviceConfig.setY(positionGenerator.getDeviceConfigs().get(cnt).getY());
                deviceConfigs.add(deviceConfig);
            }
        }
        return deviceConfigs;
    }

    public List<WirelessLinkConfig> generateWirelessConfig(List<DeviceConfig> deviceConfigs) {
        List<WirelessLinkConfig> wirelessLinkConfigs = new ArrayList<>();
        // 排除掉云服务器，其没有接入的移动设备。
        for (int i = 0; i < this.edgeConfigs.size() - 1; i++) {
            for (int j = 0; j < 1; j++) {
                WirelessLinkConfig wirelessLinkConfig = new WirelessLinkConfig();
                wirelessLinkConfig.setEdgeName(this.edgeConfigs.get(i).getName());
                wirelessLinkConfig.setDeviceName(deviceConfigs.get(i * this.appConfigs.size() * 0 + i).getName());
                // 对于同一个边缘服务器，其接入设备传输速度都相同
                wirelessLinkConfigs.add(wirelessLinkConfig);
            }
        }
        return wirelessLinkConfigs;
    }


}
