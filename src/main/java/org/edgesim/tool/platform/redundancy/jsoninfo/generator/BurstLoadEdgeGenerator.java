package org.edgesim.tool.platform.redundancy.jsoninfo.generator;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.BurstLoadEdgeConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class BurstLoadEdgeGenerator {

    private int numEdges;
    private int[] edgeCycleRange;

    /**
     * @param numEdges       the number of edges that generated
     * @param edgeCycleRange the random range for generating the edge's cpu cycle, array, size 2
     */
    public BurstLoadEdgeGenerator(int numEdges, int[] edgeCycleRange) {
        assert edgeCycleRange.length == 2;

        this.numEdges = numEdges;
        this.edgeCycleRange = edgeCycleRange;
    }

    public List<BurstLoadEdgeConfig> generateBurstLoadEdgeConfig() {
        List<BurstLoadEdgeConfig> edgeConfigList = new ArrayList<>(numEdges);
        Random random = new Random();
        for (int i = 0; i < numEdges; ++i) {
            BurstLoadEdgeConfig edgeConfig = new BurstLoadEdgeConfig();
            edgeConfig.setEdgeCpuCycle(random.nextInt(edgeCycleRange[1] - edgeCycleRange[0]) + edgeCycleRange[0]);
            edgeConfigList.add(edgeConfig);
        }
        return edgeConfigList;
    }

//    public static void main(String[] args) {
//        BurstLoadEdgeGenerator edgeGenerator=new BurstLoadEdgeGenerator(10,new int[]{100,500});
//        List<BurstLoadEdgeConfig> edgeConfigList=edgeGenerator.generateBurstLoadEdgeConfig();
//        for(BurstLoadEdgeConfig edgeConfig : edgeConfigList){
//            System.out.println(edgeConfig);
//        }
//    }
}
