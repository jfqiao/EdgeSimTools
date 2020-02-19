package org.edgesim.tool.platform.redundancy.gui.button;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.gui.EdgeGUI;

@Getter
@Setter
public class ConfirmConfigButton extends EdgeIconButton {

    private EdgeGUI edgeGUI;

    public ConfirmConfigButton() {
        super(EdgeIconButton.CONFIRM_CONFIG_BUTTON, "Confirm Config", "confirm_config.png");
        this.getButton().setText("确认配置");
        // 确认配置
        this.getButton().addActionListener(e -> {
            edgeGUI.generateConfig();
        });
    }

}
