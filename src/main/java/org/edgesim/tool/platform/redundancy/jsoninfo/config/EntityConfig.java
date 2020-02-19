package org.edgesim.tool.platform.redundancy.jsoninfo.config;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.gui.button.PrintIconButton;

/**
 * 在画板上绘制的实体类共有属性抽象。
 * @author jfqiao
 * @since 2019/12/30
 */
@Getter
@Setter
public class EntityConfig {
    // 绘制实体的横纵坐标, 默认都是30， 30
    private int x = 30;
    private int y = 30;
    // 绘制实体的宽、高
    private int width;
    private int height;
    private int type;
    public EntityConfig(int type) {
        this.type = type;
        this.width = PrintIconButton.ICON_CONFIG.get(type).getIconWidth();
        this.height = PrintIconButton.ICON_CONFIG.get(type).getIconHeight();
    }
}
