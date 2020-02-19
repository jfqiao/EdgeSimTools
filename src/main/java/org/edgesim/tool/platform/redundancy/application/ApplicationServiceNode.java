package org.edgesim.tool.platform.redundancy.application;

import lombok.Data;

/**
 * @ClassName: ApplicationServiceNode
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2019-12-16 21:01
 * @Version: 1.0
 */
@Data
public class ApplicationServiceNode {

    private String appName;
    private String appServiceName;
    private int msId;

    public ApplicationServiceNode(String appServiceName) {
        this.appServiceName = appServiceName;
    }

}
