package org.edgesim.tool.platform.redundancy.schedule;

/**
 * 服务调度接口，即根据算法的需要，选择一个合适的服务。
 * @author jfqiao
 * @since 2019/10/20
 */
public interface ApplicationScheduler {
    /**
     * 服务调度，返回ApplicationService的id
     * @param o 调度参数，不同的调度算法可能需要不同的参数，由算法实现转换。
     * @return ApplicationService实例的id
     */
    int schedule(Object o);
}
