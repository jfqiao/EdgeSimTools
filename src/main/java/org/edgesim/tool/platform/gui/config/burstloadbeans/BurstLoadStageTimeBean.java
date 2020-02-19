package org.edgesim.tool.platform.gui.config.burstloadbeans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 统计BurstLoad每个任务处于各个阶段时长的bean
 */
@Getter
@Setter
@ToString
public class BurstLoadStageTimeBean {

    private float waitAtSrcf;
    private float migratef;
    private float waitExecf;
    private float execf;
}
