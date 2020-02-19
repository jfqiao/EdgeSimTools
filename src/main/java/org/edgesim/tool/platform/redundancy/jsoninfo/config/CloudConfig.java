package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.gui.button.EdgeIconButton;
import org.edgesim.tool.platform.redundancy.gui.button.PrintIconButton;

@Getter
@Setter
public class CloudConfig extends EdgeConfig {
    private String name = "cloud";

    public CloudConfig() {
        setWidth(PrintIconButton.ICON_CONFIG.get(EdgeIconButton.CLOUD_SERVER_BUTTON).getIconWidth());
        setHeight(PrintIconButton.ICON_CONFIG.get(EdgeIconButton.CLOUD_SERVER_BUTTON).getIconHeight());
        setType(EdgeIconButton.CLOUD_SERVER_BUTTON);
    }
}
