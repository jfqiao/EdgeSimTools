package org.edgesim.tool.platform.application;

import lombok.Getter;
import lombok.Setter;

/**
 * ApplicationService的节点表示类，用来表示一个抽象节点，不用来实际部署，
 * 在Application类中保存此类的
 * @author jfqiao
 * @since 2019/10/21
 */
@Getter
@Setter
public class ApplicationServiceNode {

    private String appName;
    private String appServiceName;
    private double inputSize;
    private double outputSize;
    private ApplicationServiceNode preAppServiceNode;
    private ApplicationServiceNode nextAppServiceNode;

    public ApplicationServiceNode(String appServiceName) {
        this.appServiceName = appServiceName;
    }
}
