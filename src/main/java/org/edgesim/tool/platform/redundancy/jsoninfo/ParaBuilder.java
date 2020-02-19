package org.edgesim.tool.platform.redundancy.jsoninfo;

import lombok.Getter;
import lombok.Setter;
import org.cloudbus.cloudsim.core.SimEntity;
import org.edgesim.tool.platform.redundancy.entity.MobileDevice;
import org.edgesim.tool.platform.redundancy.config.MappingConfiguration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class ParaBuilder {
    private int hostNum;
    private int edgeNum;
    private int serviceNum;
    private int mobileDeviceNum;
    private int requestNum;
    private List<Double> wirelessTransRate;
    // hostNum * hostNum
    private List<List<Double>> hostBandwidth;
    private List<Double> computingPower;
    private List<Double> inputSize;
    private List<Double> outputSize;
    private List<Double> workload;
    // edgeNum * serviceNum
    private List<List<Double>> arrivalRate;
    public static double totalArrivalRate;
    private List<Double> price;
    // hostNum * serviceNum
    private List<List<Double>> mu;
    // serviceNum * edgeNum * hostNum
    private List<List<List<Double>>> p;

    public double calculateDt() {
        double sum = 0;
        Collection<SimEntity> entityList = MappingConfiguration.entityNameMapping.values();
        for (SimEntity entity : entityList) {
            if (entity instanceof MobileDevice) {
                sum += ((MobileDevice)entity).calLatency();
            }
        }
        return sum;
    }

    public double calculateCt() {
        double ct = 0.0;
        for (int i = 0; i < serviceNum; i++) {
            for (int j = 0; j < edgeNum; j++) {
                ct += price.get(j) * mu.get(j).get(i);
            }
        }
        return ct;
    }

    public void readMuByFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        mu = new ArrayList<>(hostNum);
        for (int i = 0; i < hostNum; i++) {
            mu.add(new ArrayList<>(serviceNum));
        }
        String line;
        int cnt = 0;
        while ((line = br.readLine()) != null) {
//            line = line.replaceAll("\\s", "");
            if (line.length() == 0) {
                continue;
            }
            mu.get(cnt / serviceNum).add(Double.parseDouble(line));
            cnt++;
        }
        br.close();
        for (int j = 0; j < hostNum; j++) {
            for (int i = 0; i < serviceNum; i++) {
                mu.get(j).set(i, mu.get(j).get(i) / workload.get(i)); // 将computing power转换为每秒处理多少个请求。
            }
        }
    }

    public void readPByFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        p = new ArrayList<>(serviceNum);
        for (int i = 0; i < serviceNum; i++) {
            List<List<Double>> curMatrix = new ArrayList<>(edgeNum);
            p.add(curMatrix);
            for (int j = 0; j < edgeNum; j++) {
                curMatrix.add(new ArrayList<>(hostNum));
            }
        }
        String line;
        int cnt = 0;
        int innerMatrixNum = edgeNum * hostNum;
        int i, j, tmp;
        while ((line = br.readLine()) != null) {
            line = line.replaceAll("\\s", "");
            if (line.length() == 0) {
                continue;
            }
            i = cnt / innerMatrixNum;
            tmp = cnt % innerMatrixNum;
            j = tmp / hostNum;
            cnt += 1;
            p.get(i).get(j).add(Double.parseDouble(line));
        }
    }

    public void readPara(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        hostNum = readInt(br);
        edgeNum = readInt(br);
        serviceNum = readInt(br);
        wirelessTransRate = readDoubleArray(br);
        readBandwidth(br);
        computingPower = readDoubleArray(br);
        inputSize = readDoubleArray(br);
        outputSize = readDoubleArray(br);
        workload = readDoubleArray(br);
        price = readDoubleArray(br);
        br.close();
    }

    private int readInt(BufferedReader br) throws IOException {
        br.readLine(); // read comment line
        String line = br.readLine();
        return Integer.parseInt(line.substring(0, line.length() - 1));
    }

    private List<Double> readDoubleArray(BufferedReader br) throws IOException {
        List<Double> list = new ArrayList<>();
        br.readLine(); // read comment line;
        String line = br.readLine();
        String[] numsStr = line.substring(0, line.length() - 1).split(" ");
        for (String numStr : numsStr) {
            list.add(Double.parseDouble(numStr));
        }
        return list;
    }

    private void readBandwidth(BufferedReader br) throws IOException {
        br.readLine();
        String line = br.readLine();
        String[] strs = line.substring(0, line.length() - 1).split(" ");
        hostBandwidth = new ArrayList<>();
        for (int i = 0; i < hostNum; i++) {
            hostBandwidth.add(new ArrayList<>());
        }
        int j;
        for (int i = 0; i < strs.length; i++) {
            j = i / hostNum;
            hostBandwidth.get(j).add(Double.parseDouble(strs[i]));
        }
    }

    public void readArrivalRateByFile(String path) throws IOException {
        totalArrivalRate = 0;
        BufferedReader br = new BufferedReader(new FileReader(path));
        arrivalRate = new ArrayList<>();
        for (int i = 0; i < edgeNum; i++) {
            arrivalRate.add(new ArrayList<>(serviceNum));
        }
        String line = br.readLine();
        String[] strs = line.substring(0, line.length() - 1).split(" ");
        for (int i = 0; i < strs.length; i++) {
            int j = i / serviceNum;
            double rate = Double.parseDouble(strs[i]);
            totalArrivalRate += rate;
            arrivalRate.get(j).add(rate);
        }

    }

//    public void setTotalArrivalRate() {
//        for (int i = 0; i < arrivalRate.size(); i++) {
//            for (int j = 0; j < arrivalRate.get(i).size(); j++) {
//                totalArrivalRate += arrivalRate.get(i).get(j);
//            }
//        }
//    }
}