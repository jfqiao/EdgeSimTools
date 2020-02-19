package org.edgesim.tool.platform.jsoninfo.config.burstload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.LineConfig;

@Getter
@Setter
@ToString
public class BurstLoadLinkConfig extends LineConfig {

    private String name;
    private String startEdgeName;
    private String endEdgeName;
    private int parallelWidth;
    private double transRate;

}
