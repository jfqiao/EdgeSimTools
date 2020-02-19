package org.edgesim.tool.platform.jsoninfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BandwidthInfo {
    private String from;
    private String to;
    private double bandwidth;
}
