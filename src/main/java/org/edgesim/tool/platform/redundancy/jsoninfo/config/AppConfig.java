package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 针对单个服务的配置项。
 * @author jfqiao
 * @since 2019/12/28
 */
@Getter
@Setter
public class AppConfig {

    private String appName;
    private List<AppNode> nodes;
    private List<AppEdge> edges;

    @Getter
    @Setter
    public static class AppNode {
        private String nodeName;
        private double inputSize;
        private double outputSize;
        private double workload;
    }

    @Getter
    @Setter
    public static class AppEdge {
        private AppNode startNode;
        private AppNode endNode;
    }
}
