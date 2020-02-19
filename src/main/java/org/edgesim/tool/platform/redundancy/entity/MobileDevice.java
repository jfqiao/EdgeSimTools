package org.edgesim.tool.platform.redundancy.entity;

import lombok.Data;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.edgesim.tool.platform.redundancy.config.MappingConfiguration;
import org.edgesim.tool.platform.redundancy.config.DistanceConfiguration;
import org.edgesim.tool.platform.redundancy.log.Logger;
import org.edgesim.tool.platform.redundancy.util.EventsConst;
import org.edgesim.tool.platform.redundancy.util.PlatformUtils;
import org.edgesim.tool.platform.redundancy.util.SimConfigConst;

import java.util.ArrayList;

/**
 * @ClassName: MobileDevice
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2019-12-16 21:25
 * @Version: 1.0
 */

@Data
public class MobileDevice extends SimEntity {

    private Request request;
    // 设备所请求的应用id
    private int appId;
    // app第一个app service名称
    private String appFirstServiceName;
    // 平均请求时延
    private double avgLantency;
    // 接入边缘服务器id
    private int accessEdgeServerId;

    private int mdId;

    public MobileDevice(String name) {
        super(name);
    }

    @Override
    public void startEntity() {
        send(getId(), EventsConst.IMMEDIAT_EVENT, EventsConst.DEVICE_JOIN);
    }

    @Override
    public void processEvent(SimEvent simEvent) {
        switch (simEvent.getTag()) {
            // mobile device加入后，开始发送请求。
            case EventsConst.DEVICE_JOIN:
                sendReq();
                break;
            // 发送请求事件
            // 请求收到回复
            case EventsConst.REQ_HAS_RCV_RESP:
                Request req = (Request)simEvent.getData();
                req.setRcvTime(simEvent.eventTime());
                System.out.println(String.format("Time %.4f", simEvent.eventTime()) + " Mobile Device: " + getName() + " receive request " + req.getReqId());
                break;
            default:
                break;
        }
    }

    @Override
    public void shutdownEntity() {

    }

    private void sendReq() {
        Request req = new Request(PlatformUtils.generateRequestId(), appId, accessEdgeServerId);
        req.setSourceDeviceId(getId());
        req.setEmitTime(CloudSim.clock());
        req.addLink(getName());
        req.setMdId(getMdId());
        ArrayList<Integer> route = (ArrayList<Integer>) MappingConfiguration.routeOfMobile.get(getMdId());
        req.setMsId(route.get(0));
        double latency = SimConfigConst.alpha * DistanceConfiguration.getDistance(getId(), accessEdgeServerId);
        request = req;
        System.out.println(String.format("Time %.4f", CloudSim.clock()) + " Mobile Device: " + getName() + " send request " + req.getReqId() +
                " to " + MappingConfiguration.getEntityById(accessEdgeServerId).getName());
        Logger.log(String.format("Time %.4f", CloudSim.clock()) + " Mobile Device: " + getName() + " send request " + String.format("%04d", req.getReqId()) +
                " to " + MappingConfiguration.getEntityById(accessEdgeServerId).getName());
        req.getDelays().add(latency);
        req.setStep(req.getStep() + 1);
        send(accessEdgeServerId, latency, EventsConst.MOBILE_DEVICE_SEND_REQ, req);
    }

    public double calLatency() {
        return request.getRcvTime() - request.getEmitTime();
    }
}