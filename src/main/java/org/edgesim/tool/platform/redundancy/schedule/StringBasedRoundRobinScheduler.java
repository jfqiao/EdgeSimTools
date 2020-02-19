package org.edgesim.tool.platform.redundancy.schedule;

import org.edgesim.tool.platform.redundancy.config.MappingConfiguration;
import org.edgesim.tool.platform.redundancy.config.SimConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于唯一字符串进行的round-robin调度算法。
 * 字符串需要标明需要调用的app service的名字，因此字符串采用 -app_service_name结尾。
 * @author jfqiao
 * @since 2019/10/22
 */
public class StringBasedRoundRobinScheduler implements ApplicationScheduler {

    private Map<String, Integer> uniqueStrMapping;

    public StringBasedRoundRobinScheduler() {
        uniqueStrMapping = new HashMap<>();
    }

    @Override
    public int schedule(Object o) {
        String uniqueStr = (String)o;
        int pos = uniqueStr.lastIndexOf(SimConfiguration.STRING_SEPARATOR);
        if (pos == -1) {
            throw new RuntimeException("Error with unique string scheduler");
        }
        String appServiceName = uniqueStr.substring(pos+1);
        List<Integer> appServiceNameList = MappingConfiguration.getAppServicesByName(appServiceName);
        Integer index = uniqueStrMapping.get(uniqueStr);
        if (index == null) {
            index = 0;
        }
        // 通过app service name进行轮转调度
        uniqueStrMapping.put(uniqueStr, (index + 1) % appServiceNameList.size());
        return appServiceNameList.get(index);
    }
}
