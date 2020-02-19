package org.edgesim.tool.platform.entity;

import lombok.Getter;
import lombok.Setter;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;
import org.edgesim.tool.platform.application.ApplicationServiceNode;
import org.edgesim.tool.platform.config.BandwidthConfiguration;
import org.edgesim.tool.platform.config.MappingConfiguration;
import org.edgesim.tool.platform.log.Logger;
import org.edgesim.tool.platform.util.PlatformUtils;
import org.edgesim.tool.platform.util.EventsConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动设备的表示类。
 *
 * @author jfqiao
 * @since 2019/10/20
 */
@Getter
@Setter
public class MobileDevice extends SimEntity {
    /**
     * 记录设备所有的发出去的请求，可以不用记录，视具体情况看是否需要该参数
     */
    private List<Request> requests;
    // 通过id查询每个request
    private Map<Integer, Request> requestMap;
    // 每个request的长度
    private long reqSize;
    // 设备发出请求的间隔时间
    private ContinuousDistribution distribution;
    // 设备所请求的应用id
    private int appId;
    // app第一个app service名称
    private String appFirstServiceName;
    // 平均请求时延
    private double avgLantency;
    // 停止发送请求
    private boolean stopSendReq;
    // 接入边缘服务器id
    private int accessEdgeServerId;
    // 接收到回复的请求数量
    private int reqCnt;

    /**
     * Creates a new entity.
     *
     * @param name the name to be associated with this entity
     */
    public MobileDevice(String name) {
        super(name);
        requests = new ArrayList<>();
//        setId(PlatformUtils.generateEntityId());
        this.requestMap = new HashMap<>();
        this.stopSendReq = false;
    }

    @Override
    public void startEntity() {
        send(getId(), EventsConst.IMMEDIATE_EVENT, EventsConst.DEVICE_JOIN);
    }

    @Override
    public void processEvent(SimEvent ev) {
        switch (ev.getTag()) {
            // mobile device加入后，开始发送请求。
            case EventsConst.DEVICE_JOIN:
                sendReq();
                break;
            // 发送请求事件
            case EventsConst.MOBILE_DEVICE_GENERATE_REQ:
                sendReq();
                break;
            // 请求收到回复
            case EventsConst.REQ_HAS_RCV_RESP:
                Request req = (Request) ev.getData();
                req.setRcvTime(ev.eventTime());
                Logger.log(String.format("Time %.4f", ev.eventTime()) + " Mobile Device: " + getName() + " receive request "
                        + String.format("%04d", req.getReqId()));
                break;
            default:
                break;
        }
    }

    @Override
    public void shutdownEntity() {
        stopSendReq = true;
    }

    private void sendReq() {
        if (requests.size() >= reqCnt) {
            return;
        }
        Request req = new Request(PlatformUtils.generateRequestId(), appId, accessEdgeServerId);
        req.setSourceDeviceId(getId());
        req.setEmitTime(CloudSim.clock());
        req.setCurServiceName(appFirstServiceName);
        req.setAppId(appId);
        req.addLink(getName());
        ApplicationServiceNode firstAppService = MappingConfiguration.getAppById(appId).getAppServiceNode(appFirstServiceName);
        req.setCurInputSize(firstAppService.getInputSize());
        double latency = firstAppService.getInputSize() / BandwidthConfiguration.getTransmissionRate(accessEdgeServerId);
        requests.add(req);
        Logger.log(String.format("Time %.4f", CloudSim.clock()) + " Mobile Device: " + getName() + " send request " + String.format("%04d", req.getReqId()) +
                " to " + MappingConfiguration.getEntityById(accessEdgeServerId).getName());
        req.getDelays().add(latency);
        requestMap.put(req.getReqId(), req);
        send(accessEdgeServerId, latency, EventsConst.MOBILE_DEVICE_SEND_REQ, req);
        // 发送完请求后，像自己发送一个event，触发继续发送请求。delay就是发送请求的间隔。
        if (!stopSendReq) {
            send(getId(), distribution.sample(), EventsConst.MOBILE_DEVICE_GENERATE_REQ);
        }
    }

    public List<Request> getRequests() {
        return requests;
    }

    public Map<Integer, Request> getRequestMap() {
        return requestMap;
    }

    public void setAppId(int appId) {
        this.appId = appId;
        this.appFirstServiceName = MappingConfiguration.getAppById(appId).getAppGraph().getFirst().getAppServiceName();
    }

    public double calLatency() {
        double sum = 0.0;
        Request req;
        for (int i = 0; i < requests.size(); i++) {
            req = requests.get(i);
            double l = req.getRcvTime() - req.getEmitTime();
//            for (int j = 0; j < req.getDelays().size(); j++) {
//                if (req.getDelays().get(j) > 10)
//                    System.out.println(j + req.getRequestLinks().get(2));
//            }
            sum += l;
        }
//        calLink();
        return sum;
    }

    public void printPath() {

        for (Request req : requests) {
            StringBuilder s = new StringBuilder();
            for (String t : req.getRequestLinks()) {
                s.append("->").append(t);
            }
            System.out.println(s.substring(2));
        }
    }

    public void calLink() {
        Map<String, Integer> map;
        for (int i = 0; i < 5; i++) {
            map = new HashMap<>();
            for (Request req : requests) {
                map.putIfAbsent(req.getRequestLinks().get(i), 0);
                map.computeIfPresent(req.getRequestLinks().get(i), (k, v) -> v + 1);
            }
            System.out.println(i);
            map.forEach((k, v) -> System.out.println(k + ": " + v));
        }
    }
}

