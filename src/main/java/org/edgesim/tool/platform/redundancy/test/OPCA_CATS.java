package org.edgesim.tool.platform.redundancy.test;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.edgesim.tool.platform.redundancy.config.MappingConfiguration;
import org.edgesim.tool.platform.redundancy.entity.MobileDevice;
import org.edgesim.tool.platform.redundancy.entity.Request;
import org.edgesim.tool.platform.redundancy.jsoninfo.InfoBuilder;
import org.edgesim.tool.platform.redundancy.jsoninfo.ParaBuilder;
import org.edgesim.tool.platform.redundancy.util.PlatformUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class OPCA_CATS {
    public static void main(String[] args) throws IOException {

        String t = "1";
        String constParaFile = PlatformUtils.PATH + "sc_1.txt";
        String arrivalRateFile = String.format(PlatformUtils.PATH + "arrival_rate/lambda_%s.txt", t);
        String muFile = String.format(PlatformUtils.PATH + "result/mu/mu_%s.txt", t);
        String pFile = String.format(PlatformUtils.PATH + "result/p/p_%s.txt", t);
        ParaBuilder pb = new ParaBuilder();
        pb.readPara(constParaFile);
        pb.readArrivalRateByFile(arrivalRateFile);
        pb.readMuByFile(muFile);
        pb.readPByFile(pFile);
        startSim(pb, "1");
    }

    public static void startSim(ParaBuilder pb, String t) throws IOException {
        CloudSim.init(1, Calendar.getInstance(), false);
        InfoBuilder.buildByParaBuilder(pb);

//        MappingConfiguration.applicationServices.forEach(appService -> {
//            appService.setScheduler(new ProbabilityScheduler());
//        });
        Log.disable();
        CloudSim.startSimulation();
        CloudSim.stopSimulation();
        File dir = new File(PlatformUtils.PATH + "result/requests/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Collection<SimEntity> entityList = MappingConfiguration.entityNameMapping.values();
        String filePath = String.format(dir.getAbsolutePath() + File.separator + "requests_%s.txt", t);
        File file = new File(filePath);
        PrintWriter pw = new PrintWriter(new FileOutputStream(file));
        List<Request> allReq = new ArrayList<>();
        for (SimEntity entity : entityList) {
            if (entity instanceof MobileDevice) {
                MobileDevice md = (MobileDevice)entity;
//                for (int i = 0; i < md.getRequests().size(); i++) {
//                    Request req = md.getRequests().get(i);
//                    double latency = req.getRcvTime() - req.getEmitTime();
//                    pw.println(String.format("%04d,%s,%s,%s,%.4f", req.getReqId(),req.getRequestLinks().get(0),
//                            req.getRequestLinks().get(1), req.getRequestLinks().get(2), latency));
//                    allReq.add(req);
//                }
                pw.flush();
            }
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                String.format(dir.getAbsolutePath() + File.separator + "req_ser_%s", t)));
        oos.writeObject(allReq);
        oos.flush();
        oos.close();
        pw.close();
//        System.out.println(String.format("Average latency is %.4fs", sum / cnt));
    }

}

