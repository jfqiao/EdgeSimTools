package org.edgesim.tool.platform.entity;

import lombok.Getter;
import lombok.Setter;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.edgesim.tool.platform.config.MappingConfiguration;
import org.edgesim.tool.platform.log.Logger;
import org.edgesim.tool.platform.schedule.ProbabilityScheduler;
import org.edgesim.tool.platform.config.BandwidthConfiguration;
import org.edgesim.tool.platform.schedule.ApplicationScheduler;
import org.edgesim.tool.platform.schedule.StringBasedRoundRobinScheduler;
import org.edgesim.tool.platform.util.EventsConst;

import java.util.*;

/**
 * 边缘服务器实体。此次实验中省略掉CPU、RAM、DISK、网络等分配的策略。
 * 在实际的边缘服务器中，应该给定此类资源，并且在app的服务部署到边缘服务器中时，
 * 需要响应消息分配一定的资源给相关服务：即创建虚拟机或docker容器。
 *
 * @author jfqiao
 * @since 2019/10/20
 */
@Getter
@Setter
public class EdgeServer extends SimEntity {

    private Map<String, List<ApplicationService>> deployServices;

    private ApplicationScheduler scheduler;

    private StringBasedRoundRobinScheduler transferScheduler;

    private Map<String, LinkedList<Request>> waitingQueueMap;

    private LinkedList<Request> uploadRequest;
    private LinkedList<Request> downloadRequest;


    /**
     * Creates a new entity.
     *
     * @param name the name to be associated with this entity
     */
    public EdgeServer(String name) {
        super(name);
        deployServices = new HashMap<>();
        waitingQueueMap = new HashMap<>();
    }

    @Override
    public void startEntity() {
        send(getId(), EventsConst.IMMEDIATE_EVENT, EventsConst.EDGE_SERVER_JOIN);
    }

    @Override
    public void processEvent(SimEvent ev) {
        switch (ev.getTag()) {
            case EventsConst.EDGE_SERVER_JOIN:
                // edge server加入没有任何操作
                break;

            case EventsConst.MOBILE_DEVICE_SEND_REQ:
                // 收到接入设备的转发服务的请求
                transferReq((Request) ev.getData(), ev.eventTime());
                break;

            case EventsConst.ACCESS_EDGE_SERVER_RCV_RESP:
                // 将请求结果转发到接入设备
                receiveRespToTransfer((Request) ev.getData());
                break;

            case EventsConst.EDGE_SERVER_SEND_REQ_TO_SERVICE:
                sendReqToService((Request) ev.getData());
                break;

            case EventsConst.EDGE_SERVER_RCV_REQ_FROM_SERVICE:
                rcvReqFromService((Request) ev.getData());
                break;

            case EventsConst.EDGE_SERVER_SEND_REQ_TO_HOST:
                sendReqToHost((Request) ev.getData());
                break;

            default:
                break;
        }
    }

    @Override
    public void shutdownEntity() {

    }

    public void deployServiceInServer(ApplicationService appService) {
        deployServices.computeIfAbsent(appService.getAppId() + appService.getAppServiceName(), k -> new ArrayList<>()).add(appService);
    }

    private ApplicationService getAppService(int appId, String appServiceName, int index) {
        return deployServices.get(appId + appServiceName).get(index);
    }

    private void transferReq(Request req, double time) {
        req.addLink(getName());
        int eventTag = EventsConst.EDGE_SERVER_SEND_REQ_TO_SERVICE;
        int targetEntityId = scheduler.schedule(new ProbabilityScheduler.SchedulerParameter(req.getAppId(), getId()));

//        int  targetEntityId = MappingConfiguration.getEntityIdByAppServiceId(targetServiceId);
        double lantency = req.getCurInputSize() / BandwidthConfiguration.getBandwidth(getId(), targetEntityId);
        req.getDelays().add(lantency);
        send(targetEntityId, lantency, eventTag, req);
        Logger.log(String.format("Time %.4f", time) + " Edge Server: " + getName() + " schedule request "
                + String.format("%04d", req.getReqId()) + " to " + MappingConfiguration.getEntityById(targetEntityId).getName());
    }

    private void receiveRespToTransfer(Request req) {
        double delay = req.getCurInputSize() / BandwidthConfiguration.getTransmissionRate(getId());
        req.getDelays().add(delay);
        send(req.getSourceDeviceId(), delay, EventsConst.REQ_HAS_RCV_RESP, req);
    }

    // host从其他设备接收请求，转发到部署在本机上的app service
    private void sendReqToService(Request req) {
        // 此处无延迟直接发送到相应的app service
        // 每个里面只部署一个实例
        req.getRequestLinks().add(getName());
        ApplicationService appService = getAppService(req.getAppId(), req.getCurServiceName(), 0);
        send(appService.getId(), EventsConst.IMMEDIATE_EVENT, EventsConst.APP_SERVICE_RCV_REQ_FROM_HOST, req);
    }

    // host从部署在其上的服务接收到req的处理
    private void rcvReqFromService(Request req) {
        // 此处无网络排队等待
        send(getId(), EventsConst.IMMEDIATE_EVENT, EventsConst.EDGE_SERVER_SEND_REQ_TO_HOST, req);
    }

    // host将请求转发到其他host
    private void sendReqToHost(Request req) {
        // 有网络传输延迟
        double latency = req.getCurInputSize() / BandwidthConfiguration.getBandwidth(getId(), req.getCurEdgeServerId());
        send(req.getCurEdgeServerId(), latency, EventsConst.ACCESS_EDGE_SERVER_RCV_RESP, req);
    }
}

