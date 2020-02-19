package org.edgesim.tool.platform.redundancy.schedule;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public class ProbabilityScheduler implements ApplicationScheduler {

    public static class SchedulerParameter {
        @Setter @Getter
        private int serviceId;
        @Setter @Getter
        private int hostId;

        public SchedulerParameter(int serviceId, int hostId) {
            this.serviceId = serviceId;
            this.hostId = hostId;
        }
    }

    /*
     依据概率模型产生一个调度方案，对于概率为0的不会产生调度结果。
     */
    @Override
    public int schedule(Object o) {
        SchedulerParameter sp = (SchedulerParameter)o;
        Random random = new Random();
        double prod = random.nextDouble();
        double sum = 0;
//        for (int k = 0; k < MappingConfiguration.edgeServers.size(); k++) {
//            int kid = MappingConfiguration.edgeServers.get(k).getId();
//            sum += MappingConfiguration.probabilityMap.get(PlatformUtils.buildProbabilityKey(sp.serviceId,
//                    sp.hostId, kid));
//            if (sum >= prod) {
//                return kid;
//            }
//        }
        return 0;
    }
}
