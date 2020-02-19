package org.edgesim.tool.platform.redundancy.entity;


import lombok.Data;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.edgesim.tool.platform.redundancy.config.DistanceConfiguration;
import org.edgesim.tool.platform.redundancy.config.MappingConfiguration;
import org.edgesim.tool.platform.redundancy.util.EventsConst;
import org.edgesim.tool.platform.redundancy.log.Logger;
import org.edgesim.tool.platform.redundancy.util.SimConfigConst;

import java.util.*;

/**
 * @ClassName: EdgeServer
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2019-12-16 20:38
 * @Version: 1.0
 */
@Data
public class EdgeServer extends SimEntity {

    private Map<String, List<ApplicationService>> deployServices;

    private Map<Integer, LinkedList<Request>> waitingQueueMap;

    private int capacity;

    public EdgeServer(String name) {
        super(name);
        deployServices = new HashMap<>();
        waitingQueueMap = new HashMap<>();
    }

    @Override
    public void startEntity() {
        send(getId(), EventsConst.IMMEDIAT_EVENT, EventsConst.EDGE_SERVER_JOIN);
    }

    @Override
    public void processEvent(SimEvent simEvent) {
        switch (simEvent.getTag()) {
            case EventsConst.EDGE_SERVER_JOIN:
                // edge server加入没有任何操作
                break;
            case EventsConst.MOBILE_DEVICE_SEND_REQ:
                // 收到接入设备的转发服务的请求
                transferReq((Request) simEvent.getData(), simEvent.eventTime());
                break;
            case EventsConst.EDGE_SERVER_RECEIVE_REQ:
                System.out.println(String.format("Time %.4f", simEvent.eventTime()) + " Server " + getName() + " receive request " + ((Request) simEvent.getData()).getReqId());
                // 收到请求后无延迟将请求放到edge-server该服务中，并进行处理
                receiveRequest((Request) simEvent.getData());
                break;
            case EventsConst.ACCESS_EDGE_SERVER_RCV_RESP:
                receiveRespToTransfer((Request) simEvent.getData());
                break;
            default:
                break;
        }
    }

    @Override
    public void shutdownEntity() {

    }

    public void deployServiceInServer(ApplicationService appService) {
        deployServices.computeIfAbsent(appService.getAppServiceName(), k -> new ArrayList<>()).add(appService);
    }

    private void transferReq(Request req, double time) {
        ArrayList<Integer> targetEntityIds;
        req.addLink(getName());
        int targetEntityId = 0;
        int tag;
        double lantency;
        double minDistance = Double.MAX_VALUE;

        ArrayList<Integer> route = (ArrayList<Integer>) MappingConfiguration.routeOfMobile.get(req.getMdId());
        int targetServiceId = route.get(req.getStep());
        targetEntityIds = MappingConfiguration.getAppServiceIdEntityIdsMapping(targetServiceId);
        if(targetEntityIds.size() ==  0){
            targetEntityId = ((MobileDevice) MappingConfiguration.getEntityById(req.getSourceDeviceId())).getAccessEdgeServerId();
            tag = EventsConst.ACCESS_EDGE_SERVER_RCV_RESP;
            lantency = SimConfigConst.TIME_OF_BB;
        }
        else{
            for (Integer entityId : targetEntityIds) {
                if (minDistance > DistanceConfiguration.getDistance(getId(), entityId)) {
                    minDistance = DistanceConfiguration.getDistance(getId(), entityId);
                    targetEntityId = entityId;
                }
            }
            tag = EventsConst.EDGE_SERVER_RECEIVE_REQ;
            lantency = SimConfigConst.beta * minDistance;
        }
        /**
         * Log
         */
        Logger.log(String.format("Time %.4f", time) + " Edge Server: " + getName() + " schedule request "
                + String.format("%04d", req.getReqId()) + " to " + MappingConfiguration.getEntityById(targetEntityId).getName());

        req.getDelays().add(lantency);
        send(targetEntityId, lantency, tag, req);
    }

    private void receiveRequest(Request req) {
        send(req.getMsId()+SimConfigConst.sumOfME, EventsConst.IMMEDIAT_EVENT, EventsConst.APP_SERVICE_HANDLING_REQ, req);
    }

    private void receiveRespToTransfer(Request req) {
        send(req.getSourceDeviceId(), EventsConst.IMMEDIAT_EVENT, EventsConst.REQ_HAS_RCV_RESP, req);
    }
}
