package org.edgesim.tool.platform.jsoninfo.config.burstload;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.EntityConfig;
import org.edgesim.tool.platform.gui.button.EdgeIconButton;

@Getter
@Setter
public class BurstLoadEdgeConfig extends EntityConfig {

    private String name;
    private double edgeCpuCycle;

    public BurstLoadEdgeConfig() {
        super(EdgeIconButton.EDGE_SERVER_BUTTON);
    }

    @Override
    public String toString() {
        return "BurstLoadEdgeConfig{" +
                "name='" + name + '\'' +
                ", edgeCpuCycle=" + edgeCpuCycle +
                '}';
    }
}
