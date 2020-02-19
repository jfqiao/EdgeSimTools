package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.gui.button.EdgeIconButton;

/**
 * 配置文件中边缘服务器的配置项。
 * @author jfqiao
 * @since 2019/12/28
 */
@Getter
@Setter
public class EdgeConfig extends EntityConfig {
    private String name;
    private int appCapacity;
    private double price;
    private double computingPower;
    public EdgeConfig() {
        super(EdgeIconButton.EDGE_SERVER_BUTTON);
    }
}
