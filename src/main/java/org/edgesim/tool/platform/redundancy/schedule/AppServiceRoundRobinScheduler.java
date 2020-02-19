package org.edgesim.tool.platform.redundancy.schedule;


import org.edgesim.tool.platform.redundancy.entity.ApplicationService;

import java.util.HashMap;
import java.util.Map;

/**
 * 采用Round-Robin算法调度。
 * @author jfqiao
 * @since 2019/10/21
 */
public class AppServiceRoundRobinScheduler implements ApplicationScheduler {

    private Map<ApplicationService, Integer> appServiceMapping;


    public AppServiceRoundRobinScheduler() {
        appServiceMapping = new HashMap<>();

    }

    @Override
    public int schedule(Object o) {
        ApplicationService applicationService = (ApplicationService)o;
        Integer index = appServiceMapping.get(applicationService);
        if (index == null) {
            index = 0;
        }
//        Application app = MappingConfiguration.getAppById(applicationService.getAppId());
//        ApplicationServiceNode node = app.getAppServiceNode(applicationService.getAppServiceName());
//        List<Integer> appServiceNameList = MappingConfiguration.getAppServicesByName(node.getNextAppServiceNode().getAppServiceName());
//        appServiceMapping.put(applicationService, (index + 1) % appServiceNameList.size());
//        return appServiceNameList.get(index);
        return 0;
    }
}
