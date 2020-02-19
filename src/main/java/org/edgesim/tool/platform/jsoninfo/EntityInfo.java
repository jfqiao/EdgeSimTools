package org.edgesim.tool.platform.jsoninfo;

import lombok.Getter;
import lombok.Setter;

/**
 * json配置信息中entity信息
 * @author jfqiao
 * @since 2019/10/22
 */
@Getter
@Setter
public class EntityInfo {
    // entity的名称
    private String name;
    // entity的类型
    private String type;

    private double rate;

    private String accessEdgeServerName;
}
