package org.edgesim.tool.platform.util;

public class EventsConst {
    // 无间隔事件，立刻发出
    public static final double IMMEDIATE_EVENT = 0.0;

    private static final int BASE = 100;
    // 有mobile device加入
    public static final int DEVICE_JOIN = BASE + 1;
    // 有edge server加入
    public static final int EDGE_SERVER_JOIN = BASE + 2;
    // edge server收到Request实体对象
    public static final int EDGE_SERVER_RECEIVE_REQ = BASE + 3;
    // edge server收到从request队列中取请求处理
    public static final int EDGE_SERVER_FETCH_REQ = BASE + 4;
    // mobile device发送下一个请求的事件提示
    public static final int MOBILE_DEVICE_GENERATE_REQ = BASE + 5;
    // 请求收到回复（mobile device与edge server收到回复采用同一个标识）
    public static final int REQ_HAS_RCV_RESP = BASE + 6;

    public static final int MOBILE_DEVICE_WATING_RCV_END = BASE + 7;
    //停止模拟事件
    public static final int STOP_SIMULATION = BASE + 8;

    public static final int EDGE_SERVER_PROCECESS_TRANSMIT = BASE + 9;

    // mobile device向接入的边缘服务器发送请求
    public static final int MOBILE_DEVICE_SEND_REQ = BASE + 10;
    // edge server作为接入服务器收到服务
    public static final int ACCESS_EDGE_SERVER_RCV_RESP = BASE + 11;

    // app service空闲时收到处理请求的通知
    public static final int APP_SERVICE_HANDLING_REQ = BASE + 12;
    // app service从edge server的请求等待队列中取出请求出来处理
    public static final int APP_SERVICE_FETCHING_REQ = BASE + 13;
    // app service 更新其状态
    public static final int APP_SERVICE_UPDATE_STATUS = BASE + 14;

    // edge server收到请求后，采用mme模型，将请求转发到对应的app service
    public static final int EDGE_SERVER_SEND_REQ_TO_SERVICE = BASE + 15;
    // edge server从app service收到处理完的请求
    public static final int EDGE_SERVER_RCV_REQ_FROM_SERVICE = BASE + 16;
    // edge server将req转发到下一个host
    public static final int EDGE_SERVER_SEND_REQ_TO_HOST = BASE + 17;

    public static final int APP_SERVICE_RCV_REQ_FROM_HOST = BASE + 18;

    public static final int APP_SERVICE_FETCH_REQ_FROM_QUEUE = BASE + 19;

    public static final int APP_SERVICE_SEND_REQ_TO_HOST = BASE + 20;

    public static final int EDGE_SERVER_REV_REQ_FRO_HOST = BASE + 21;
}
