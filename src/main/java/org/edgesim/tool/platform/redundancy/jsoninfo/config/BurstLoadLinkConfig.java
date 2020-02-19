package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BurstLoadLinkConfig extends LineConfig {

    private int startEdgeName;
    private int endEdgeName;
    private int parallelWidth;
    private double transRate;

    @Override
    public String toString() {
        return "BurstLoadLinkConfig{" +
                "startEdgeName=" + startEdgeName +
                ", endEdgeName=" + endEdgeName +
                ", parallelWidth=" + parallelWidth +
                ", transRate=" + transRate +
                '}';
    }
}
