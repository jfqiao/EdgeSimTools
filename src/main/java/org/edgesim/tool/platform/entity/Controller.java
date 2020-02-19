package org.edgesim.tool.platform.entity;

import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.edgesim.tool.platform.config.MappingConfiguration;
import org.edgesim.tool.platform.config.SimConfiguration;
import org.edgesim.tool.platform.util.EventsConst;

import java.util.Collection;

/**
 * 控制实体，只是负责处理一些控制事件，比如
 * @author jfqiao
 * @since 2019/10/22
 */
public class Controller extends SimEntity {

    public Controller(String name) {
        super(name);
    }

    @Override
    public void startEntity() {
        send(getId(), SimConfiguration.MAX_SIMULATION_TIME, EventsConst.STOP_SIMULATION);
    }

    @Override
    public void processEvent(SimEvent ev) {
        // controller可以加入更多事件，例如资源重新调度，重新部署等。
        switch (ev.getTag()) {
            case EventsConst.STOP_SIMULATION:
                // 计算统计信息。
                calculateLatency();
                System.exit(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void shutdownEntity() {

    }

    private void calculateLatency() {
        Collection<SimEntity> entities = MappingConfiguration.entityNameMapping.values();
        entities.forEach(entity -> {
            if (entity instanceof MobileDevice) {
                System.out.println(((MobileDevice)entity).calLatency());
            }
        });
    }
}
