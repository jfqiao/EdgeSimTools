package org.edgesim.tool.platform.gui.button;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.EdgeGUI;

@Getter
@Setter
public class ConfirmConfigButton extends EdgeIconButton {

    private EdgeGUI edgeGUI;

    public ConfirmConfigButton() {
        super(CONFIRM_CONFIG_BUTTON, "Confirm Config", "confirm_config.png");
        this.getButton().setText("确认配置");
        // 确认配置
        this.getButton().addActionListener(e -> {
            edgeGUI.generateConfig();
        });
    }

}
