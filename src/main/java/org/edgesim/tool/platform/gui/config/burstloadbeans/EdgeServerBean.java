package org.edgesim.tool.platform.gui.config.burstloadbeans;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EdgeServerBean {

    private String name;
    private double freq;
    private double ram;
    private double storage;
}
