package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 边缘服务器之间以及边缘服务器与云服务器之间的网络连接配置
 * @author jfqiao
 * @since 2019/12/28
 */
@Getter
@Setter
public class LinkConfig extends LineConfig{
    private String startEdgeName;
    private String endEdgeName;
    private double distance;
    private double downLink;
    private double upLink;
}
