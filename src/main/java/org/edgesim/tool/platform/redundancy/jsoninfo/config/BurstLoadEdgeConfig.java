package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.gui.button.EdgeIconButton;

@Getter
@Setter
public class BurstLoadEdgeConfig extends EntityConfig {

    private int edgeCpuCycle;

    public BurstLoadEdgeConfig() {
        super(EdgeIconButton.EDGE_SERVER_BUTTON);
    }

    @Override
    public String toString() {
        return "BurstLoadEdgeConfig{" +
                "edgeCpuCycle=" + edgeCpuCycle +
                '}';
    }
}
