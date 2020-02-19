package org.edgesim.tool.platform.redundancy.entity;

import lombok.Data;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.edgesim.tool.platform.redundancy.config.DistanceConfiguration;
import org.edgesim.tool.platform.redundancy.config.MappingConfiguration;
import org.edgesim.tool.platform.redundancy.log.Logger;
import org.edgesim.tool.platform.redundancy.util.EventsConst;
import org.edgesim.tool.platform.redundancy.application.ApplicationServiceNode;
import org.edgesim.tool.platform.redundancy.util.SimConfigConst;

import java.util.ArrayList;

/**
 * @ClassName: ApplicationService
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2019-12-16 20:42
 * @Version: 1.0
 */
@Data
public class ApplicationService extends SimEntity {
    /**
     * 每种ApplicationService的service name是相同的。
     */
    private String appServiceName;

    private ApplicationServiceNode appServiceNode;

    public ApplicationService(String appServiceName, ApplicationServiceNode appServiceNode) {
        super(appServiceName);
        this.appServiceName = appServiceName;
        this.appServiceNode = appServiceNode;
    }

    @Override
    public void startEntity() {

    }

    @Override
    public void processEvent(SimEvent simEvent) {
        switch (simEvent.getTag()) {
            case EventsConst.APP_SERVICE_HANDLING_REQ:
                System.out.println(String.format("Time %.4f", simEvent.eventTime()) + " Service " + getAppServiceName() + " handling request " + ((Request) simEvent.getData()).getReqId() + " in server " + simEvent.getSource());
                handleReq((Request) simEvent.getData(), simEvent.getSource());
                break;
            default:
                break;
        }
    }

    @Override
    public void shutdownEntity() {

    }

    private void handleReq(Request req, int sourceServerId) {
        /**
         * Log
         */
        Logger.log(String.format("Time %.4f", CloudSim.clock()) + " Edge Server: " +
                MappingConfiguration.getEntityById(sourceServerId).getName() + " handle request "
                + String.format("%04d", req.getReqId()));
        ArrayList<Integer> targetEntityIds;
        int targetEntityId = 0;
        int tag;
        double minDistance = Double.MAX_VALUE;
        double delay;
        if (req.getStep() == 2) {
            // 如果当前服务为最后一个app最后一个服务，则发送到接入服务器
            targetEntityId = ((MobileDevice) MappingConfiguration.getEntityById(req.getSourceDeviceId())).getAccessEdgeServerId();
            tag = EventsConst.ACCESS_EDGE_SERVER_RCV_RESP;
            delay = SimConfigConst.beta * DistanceConfiguration.getDistance(sourceServerId, targetEntityId);
        } else {
            ArrayList<Integer> route = (ArrayList<Integer>) MappingConfiguration.routeOfMobile.get(req.getMdId());
            int nextServiceId = route.get(req.getStep());
            targetEntityIds = MappingConfiguration.getAppServiceIdEntityIdsMapping(nextServiceId);
            if(targetEntityIds == null){
                targetEntityId = ((MobileDevice) MappingConfiguration.getEntityById(req.getSourceDeviceId())).getAccessEdgeServerId();
                tag = EventsConst.ACCESS_EDGE_SERVER_RCV_RESP;
                delay = SimConfigConst.TIME_OF_BB;
            }
            else{
                for (Integer entityId : targetEntityIds) {
                    if (minDistance > DistanceConfiguration.getDistance(sourceServerId, entityId)) {
                        minDistance = DistanceConfiguration.getDistance(sourceServerId, entityId);
                        targetEntityId = entityId;
                    }
                }
                tag = EventsConst.EDGE_SERVER_RECEIVE_REQ;
                delay = SimConfigConst.beta * minDistance;
            }

        }
        req.setStep(req.getStep() + 1);
        // 处理完当前的服务后，继续向后请求
        req.getDelays().add(delay);
        send(targetEntityId, delay, tag, req);

    }

}


