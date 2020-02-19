package org.edgesim.tool.platform.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestData {
    private int reqId;
    private String deviceName;
    private String accessServerName;
    private String handleServerName;
    private double delay;

    public RequestData(int reqId, String deviceName, String accessServerName, String handleServerName, double delay) {
        this.reqId = reqId;
        this.deviceName = deviceName;
        this.accessServerName = accessServerName;
        this.handleServerName = handleServerName;
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "RequestData [reqId=" + reqId + ", deviceName=" + deviceName + ", accessServerName=" + accessServerName
                + ", handleServerName=" + handleServerName + ", delay=" + delay + "]";
    }

}
