package org.edgesim.tool.platform.redundancy.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName: Request
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2019-12-16 20:54
 * @Version: 1.0
 */

@Data
public class Request {
    // 请求链路方向
    public static final int REQUEST_DIRECTION = 1;
    // 响应链路方向，响应链路直接从requestLinks取出实例返回即可。
    public static final int RESPONSE_DIRECTION = 2;

    // 请求id
    private int reqId;
    private int sourceDeviceId;
    private int appId;
    private int mdId;
    private int msId;
    private int step;

    // 发送请求的时间
    private double emitTime;
    // 得到响应的时间
    private double rcvTime;

    private double putInQueueTime;

    private List<Double> delays;

    // 一个请求走过的所有机器名称
    private LinkedList<String> requestLinks;


    public Request(int reqId, int appId, int sourceDeviceId) {
        setReqId(reqId);
        setAppId(appId);
        setSourceDeviceId(sourceDeviceId);
        setStep(0);
        requestLinks = new LinkedList<>();
        delays = new ArrayList<>();
    }

    public void addLink(String name) {
        requestLinks.add(name);
    }

}

