package org.edgesim.tool.platform.jsoninfo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeployInfo {
    private String appName;
    private String nodeName;
    private String entityName;
    private int num;
    private List<List<Double>> disParas;
}
