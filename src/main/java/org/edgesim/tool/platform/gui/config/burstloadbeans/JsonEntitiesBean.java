package org.edgesim.tool.platform.gui.config.burstloadbeans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class JsonEntitiesBean {

    private List<EdgeServerBean> edge_servers;
    private List<EdgeLinkBean> edge_links;
    private List<EdgeLocBean> edge_locs;
}
