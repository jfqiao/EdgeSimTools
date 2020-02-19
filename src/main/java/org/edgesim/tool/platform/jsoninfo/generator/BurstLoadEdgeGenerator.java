package org.edgesim.tool.platform.jsoninfo.generator;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeLocBean;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeServerBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class BurstLoadEdgeGenerator {

    private int numEdges;
    private int[] edgeCycleRange;
    private int[] edgeRamRange;
    private int[] edgeStorageRange;

    /**
     * @param numEdges       the number of edges that generated
     * @param edgeCycleRange the random range for generating the edge's cpu cycle, array, size 2
     */
    public BurstLoadEdgeGenerator(int numEdges, int[] edgeCycleRange, int[] edgeRamRange, int[] edgeStorageRange) {
        assert edgeCycleRange.length == 2;

        this.numEdges = numEdges;
        this.edgeCycleRange = edgeCycleRange;
        this.edgeRamRange = edgeRamRange;
        this.edgeStorageRange = edgeStorageRange;
    }

    public List<EdgeServerBean> generateBurstLoadEdgeConfig() {
        List<EdgeServerBean> edgeServerBeans = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numEdges; ++i) {
            EdgeServerBean edgeServerBean = new EdgeServerBean();
            edgeServerBean.setName("s" + i);
            edgeServerBean.setFreq(random.nextInt(edgeCycleRange[1] - edgeCycleRange[0]) + edgeCycleRange[0]);
            edgeServerBean.setRam(random.nextInt(edgeRamRange[1] - edgeRamRange[0]) + edgeRamRange[0]);
            edgeServerBean.setStorage(random.nextInt(edgeStorageRange[1] - edgeStorageRange[0]) + edgeStorageRange[0]);
            edgeServerBeans.add(edgeServerBean);
        }
        return edgeServerBeans;
    }

    public List<EdgeLocBean> generateEdgeLocs(int[] xLimit, int[] yLimit) {
        int xLow = xLimit[0], xHigh = xLimit[1], yLow = yLimit[0], yHigh = yLimit[1];
        final int edgePerLine = 4, paddingLeft = 200, paddingTop = 100, hGap = 150, vGap = 100;
        List<EdgeLocBean> locs = new ArrayList<>();
        for (int i = 0; i < numEdges; ++i) {
            int x = paddingLeft + (i % edgePerLine) * hGap;
            int y = paddingTop + (i / edgePerLine) * vGap;
            EdgeLocBean locBean = new EdgeLocBean();
            locBean.setX(x);
            locBean.setY(y);
            locs.add(locBean);
        }
        return locs;
    }

//    public List<BurstLoadEdgeConfig> generateBurstLoadEdgeConfig() {
////        List<BurstLoadEdgeConfig> edgeConfigList = new ArrayList<>(numEdges);
////        Random random = new Random();
////        for (int i = 0; i < numEdges; ++i) {
////            BurstLoadEdgeConfig edgeConfig = new BurstLoadEdgeConfig();
////            edgeConfig.setEdgeCpuCycle(random.nextInt(edgeCycleRange[1] - edgeCycleRange[0]) + edgeCycleRange[0]);
////            edgeConfigList.add(edgeConfig);
////        }
////        return edgeConfigList;
////    }


//    public static void main(String[] args) {
//        BurstLoadEdgeGenerator edgeGenerator=new BurstLoadEdgeGenerator(10,new int[]{100,500});
//        List<BurstLoadEdgeConfig> edgeConfigList=edgeGenerator.generateBurstLoadEdgeConfig();
//        for(BurstLoadEdgeConfig edgeConfig : edgeConfigList){
//            System.out.println(edgeConfig);
//        }
//    }
}
