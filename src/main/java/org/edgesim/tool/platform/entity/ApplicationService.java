package org.edgesim.tool.platform.entity;

import lombok.Getter;
import lombok.Setter;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;
import org.edgesim.tool.platform.config.MappingConfiguration;
import org.edgesim.tool.platform.log.Logger;
import org.edgesim.tool.platform.application.ApplicationServiceNode;
import org.edgesim.tool.platform.schedule.ApplicationScheduler;
import org.edgesim.tool.platform.util.EventsConst;

import java.util.LinkedList;

/**
 * 应用的服务的表示类，应用的服务采用双向链表的方式保存，因此每个服务需要保存其下一个服务以及前一个服务。
 * 处理排队请求的方式：在其他Entity发送Request到保存本ApplicationService的Entity，根据时间先后顺序保存到一个链表中，
 * 然后发送一个SimEvent给自己，让自己从ApplicationService中取一个Request处理，同时再发同类SimEvent，其delay就是处理
 * 当前Request的时间。
 * 在仿真模拟中，ApplicationService部署在edge server中，相当于一个虚拟机或docker容器。
 * 此处省略的CPU、RAM、网络带宽等模拟，待后续添加。
 *
 * @author jfqiao
 * @since 2019/10/20
 */
public class ApplicationService extends SimEntity {
    @Getter @Setter
    private int appId;
    @Getter @Setter
    private int edgeServerId;
    /**
     * 每种ApplicationService的service name是相同的。
     */
    @Getter @Setter
    private String appServiceName;
    private ContinuousDistribution distribution;
    @Getter @Setter
    private ApplicationScheduler scheduler;
    /**
     * 保存所有待处理的请求。
     */
    private LinkedList<Request> rcvRequest;

    @Getter @Setter
    private boolean busy;

    private ApplicationServiceNode appServiceNode;

    public ApplicationService(String appServiceName, int appId,
                              ContinuousDistribution distribution, ApplicationServiceNode appServiceNode) {
        super(appServiceName);
        this.appId = appId;
        this.appServiceName = appServiceName;
        this.distribution = distribution;
        this.rcvRequest = new LinkedList<>();
        this.appServiceNode = appServiceNode;
    }

    public double getProcessingTime() {
        return distribution.sample();
    }

    @Override
    public void startEntity() {

    }

    @Override
    public void processEvent(SimEvent simEvent) {
        switch (simEvent.getTag()) {
            case EventsConst.APP_SERVICE_RCV_REQ_FROM_HOST:
                rcvReqFromHost((Request) simEvent.getData());
                break;

            case EventsConst.APP_SERVICE_FETCH_REQ_FROM_QUEUE:
                fetchReqFromQueue();
                break;

            case EventsConst.APP_SERVICE_SEND_REQ_TO_HOST:
                sendReqToHost((Request) simEvent.getData());
                break;
            default:
                break;
        }
    }

    @Override
    public void shutdownEntity() {

    }

    public Request removeFirst() {
        return rcvRequest.removeFirst();
    }

    public void addRequest(Request request) {
        rcvRequest.add(request);
    }

    public boolean isEmpty() {
        return rcvRequest.isEmpty();
    }

    private void rcvReqFromHost(Request req) {
        if (isEmpty()) {
            addRequest(req);
            send(getId(), EventsConst.IMMEDIATE_EVENT, EventsConst.APP_SERVICE_FETCH_REQ_FROM_QUEUE);
        } else {
            addRequest(req);
        }
    }

    private void fetchReqFromQueue() {
        // 本次应用中，服务只需要处理一次立刻返回到接入服务器
        Request req = removeFirst();
        Logger.log(String.format("Time %.4f", CloudSim.clock()) + " Edge Server: " +
                MappingConfiguration.getEntityById(edgeServerId).getName() + " handle request "
                + String.format("%04d", req.getReqId()));
        double processingTime = getProcessingTime();
        send(getId(), processingTime, EventsConst.APP_SERVICE_SEND_REQ_TO_HOST, req);
        // 如果队列不空，则继续去队列里面取
        if (!isEmpty()) {
            send(getId(), processingTime, EventsConst.APP_SERVICE_FETCH_REQ_FROM_QUEUE);
        }
    }


    private void sendReqToHost(Request req) {
        MobileDevice md = (MobileDevice) MappingConfiguration.getEntityById(req.getSourceDeviceId());
        req.setCurEdgeServerId(md.getAccessEdgeServerId());
        req.setCurInputSize(appServiceNode.getOutputSize());
        send(getEdgeServerId(), EventsConst.IMMEDIATE_EVENT, EventsConst.EDGE_SERVER_RCV_REQ_FROM_SERVICE, req);
    }
}


