package org.edgesim.tool.platform.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 对移动设备发起的请求的抽象，移动设备请求经过一个处理链路
 * m -> s1 -> s2 -> s3 -> s2 -> s1 -> m,
 * 前面一段为请求链路，后面一段为响应链路，通过direction属性指明请求方向
 * 在edge server中响应SimEvent时，需要根据Request的方向选择发送消息的SimEntity。
 * @author jfqiao
 * @since 2019/10/20
 */
@Getter
@Setter
public class Request implements Serializable {
    // 请求链路方向
    public static final int REQUEST_DIRECTION = 1;
    // 响应链路方向，响应链路直接从requestLinks取出实例返回即可。
    public static final int RESPONSE_DIRECTION = 2;

    // 请求id
    private int reqId;
    private int sourceDeviceId;
    private int appId;
    //    private String reqType;
    // 发送请求的时间
    private double emitTime;
    // 得到响应的时间
    private double rcvTime;
    //    private int direction;
    // request需要被处理的service的名称
    private String curServiceName;
    // 当前request的size
    private double curInputSize;
    //
    private List<Double> delays;

    private double putInQueueTime;

    // 在需要使用的时候设置与读取
    private int curServiceId;
    private int curEdgeServerId;

    // 一个请求走过的所有机器名称
    private LinkedList<String> requestLinks;


    public Request(int reqId, int appId, int sourceDeviceId) {
        setReqId(reqId);
        setAppId(appId);
        setSourceDeviceId(sourceDeviceId);
        requestLinks = new LinkedList<>();
        delays = new ArrayList<>();
    }

    public Request() {

    }

    public void addLink(String name) {
        requestLinks.add(name);
    }
}

