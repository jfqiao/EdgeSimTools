package org.edgesim.tool.platform.redundancy.jsoninfo;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * json配置文件用来转换的类。
 *
 * @author jfqiao
 * @since 2019/10/23
 */
@Getter
@Setter
public class ConfigInfo {

    private List<EntityInfo> entities;
    private List<List<Double>> m2mBandwidth;
    private List<BandwidthInfo> m2dBandwidth;
    private List<AppInfo> applications;
    private List<List<Double>> processingInfo;
    private List<List<Integer>> deployInfo;
    private List<List<Double>> s2sDistance;
    private List<MobileDeviceInfo> mobileDeviceInfos;
    private List<List<List<Double>>> transferProbability;

}

