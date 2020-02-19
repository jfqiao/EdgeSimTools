package org.edgesim.tool.platform.redundancy.test;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.edgesim.tool.platform.redundancy.config.MappingConfiguration;
import org.edgesim.tool.platform.redundancy.entity.EdgeServer;
import org.edgesim.tool.platform.redundancy.entity.MobileDevice;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Collection;

public class TestApp {
    public static void main(String[] args) throws FileNotFoundException {
        CloudSim.init(6, Calendar.getInstance(), false);
        String configFilePath = "/Users/zijie/Documents/GitHub/zjuEdgeSim/src/main/java/zju/edu/cn/platform/test/sim_config.json";
//        InfoBuilder.buildByConfigFile(configFilePath);
        double total = 0;
        for (SimEntity simEntity : MappingConfiguration.simEntities){
            if (simEntity instanceof EdgeServer) {
                EdgeServer edgeServer = (EdgeServer) simEntity;
//                edgeServer.setTransferScheduler(new StringBasedRoundRobinScheduler());
            } else if (simEntity instanceof MobileDevice) {
                MobileDevice md = (MobileDevice) simEntity;
//                md.setScheduler(new StringBasedRoundRobinScheduler());
            }
        }
        MappingConfiguration.applicationServices.forEach(appService -> {
//            appService.setScheduler(new AppServiceRoundRobinScheduler());
        });
//        Controller controller = new Controller("controller");
        Log.disable();
        CloudSim.startSimulation();
        CloudSim.stopSimulation();
        int cnt = 0;
        double sum = 0;
        Collection<SimEntity> entityList = MappingConfiguration.entityNameMapping.values();
//        File file = new File("/Users/jfqiao/Documents/project/zjuEdgeSim/src/main/java/zju/edu/cn/platform/test/result7.txt");
//        PrintWriter pw = new PrintWriter(new FileOutputStream(file));
        for (SimEntity entity : entityList) {
            if (entity instanceof MobileDevice) {
                sum += ((MobileDevice)entity).calLatency();
//                cnt += ((MobileDevice)entity).getRequests().size();
//                MobileDevice md = (MobileDevice)entity;
//                for (int i = 0; i < md.getRequests().size(); i++) {
//                    Request req = md.getRequests().get(i);
//                    double latency = req.getRcvTime() - req.getEmitTime();
//                    pw.println(String.format("Average latency is %.4fs", latency));
//                }
//                pw.flush();
            }
        }
//        pw.close();
        System.out.println(String.format("Average latency is %.4fs", sum / cnt));
    }

    public static void startSimulation(String configPath) {

    }
}
