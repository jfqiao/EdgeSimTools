package org.edgesim.tool.platform.jsoninfo.generator;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.distribution.TruncatedNormalDistribution;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.AppConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.DeviceConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.EdgeConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.WirelessLinkConfig;

import java.util.ArrayList;
import java.util.List;

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

    public List<DeviceConfig> generateDeviceConfigs() {
        positionGenerator.generateMobileDevicePos((edgeConfigs.size() - 1) * appConfigs.size());
        // 每种服务，都在每个边缘服务器上产生一个移动设备。
        List<DeviceConfig> deviceConfigs = new ArrayList<>();
        // 排除云服务器
        String defaultDeviceName = "mobile-device-%d-%d";
        for (int i = 0; i < edgeConfigs.size() - 1; i++) {
            for (int j = 0; j < appConfigs.size(); j++) {
                DeviceConfig deviceConfig = new DeviceConfig();
                deviceConfig.setAppName(appConfigs.get(j).getAppName());
                deviceConfig.setName(String.format(defaultDeviceName, i, j));
                deviceConfig.setReqGeneratedRate(reqGeneratedRateDist.sample());
                deviceConfig.setAccessEdgeName(edgeConfigs.get(i).getName());
                int cnt = i * appConfigs.size() + j;
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
            double transmission = transmissionRateDist.sample();
            for (int j = 0; j < this.appConfigs.size(); j++) {
                WirelessLinkConfig wirelessLinkConfig = new WirelessLinkConfig();
                wirelessLinkConfig.setEdgeName(this.edgeConfigs.get(i).getName());
                wirelessLinkConfig.setDeviceName(deviceConfigs.get(i * this.appConfigs.size() + j).getName());
                // 对于同一个边缘服务器，其接入设备传输速度都相同
                wirelessLinkConfig.setTransmissionRate(transmission);
                wirelessLinkConfigs.add(wirelessLinkConfig);
            }
        }
        return wirelessLinkConfigs;
    }



}
