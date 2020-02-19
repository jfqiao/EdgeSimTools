package org.edgesim.tool.platform.redundancy.jsoninfo.generator;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.BurstLoadLinkConfig;

import java.util.*;

@Getter
@Setter
public class BurstLoadLinkGenerator {
    private int[] parallelWidthRange;
    private double[] transRates = {0.1, 0.125, 0.2, 0.25, 0.5, 1.0};
    private int numEdges;
    private int[] edgeDegreeRange;

    /**
     * @param parallelWidthRange the range of generating link parallel width, size 2
     * @param numEdges           the num of edges, this value is used to assign the start end end for a link
     * @param edgeDegreeRange    the control value for the degree of link sparsity
     */
    public BurstLoadLinkGenerator(int[] parallelWidthRange, int numEdges, int[] edgeDegreeRange) {
        assert parallelWidthRange.length == 2;
        assert edgeDegreeRange.length == 2;
        assert edgeDegreeRange[1] < numEdges;

        this.parallelWidthRange = parallelWidthRange;
        this.numEdges = numEdges;
        this.edgeDegreeRange = edgeDegreeRange;
    }

    public List<BurstLoadLinkConfig> generateLinkConfigs() {
        List<BurstLoadLinkConfig> burstLoadLinkConfigList = new ArrayList<>();
        int[][] connectMatrix = new int[numEdges][];
        for (int i = 0; i < numEdges; ++i) {
            connectMatrix[i] = new int[numEdges];
            for (int j = 0; j < numEdges; ++j) {
                connectMatrix[i][j] = 0;
            }
        }
        Random random = new Random();
        for (int i = 0; i < numEdges && (i + edgeDegreeRange[1] + 1) < numEdges; ++i) {
            Set<Integer> flag = new HashSet<>();
            int numSelected = random.nextInt(edgeDegreeRange[1] - edgeDegreeRange[0]) + edgeDegreeRange[0];
            if (numSelected + i >= numEdges)
                continue;
            while (flag.size() < numSelected) {
                int idx = random.nextInt(numEdges - i - 1) + i + 1;
                connectMatrix[i][idx] = 1;
                connectMatrix[idx][i] = 1;
                flag.add(idx);
            }
        }
        for (int i = 0; i < numEdges; ++i) {
            for (int j = i + 1; j < numEdges; ++j) {
                if (connectMatrix[i][j] == 1) {
                    BurstLoadLinkConfig linkConfig = new BurstLoadLinkConfig();
                    linkConfig.setStartEdgeName(i);
                    linkConfig.setEndEdgeName(j);
                    linkConfig.setParallelWidth(random.nextInt(parallelWidthRange[1] - parallelWidthRange[0]) + parallelWidthRange[0]);
//                    linkConfig.setTransRate(random.nextInt(transRateRange[1]-transRateRange[0])+transRateRange[0]);
                    linkConfig.setTransRate(transRates[random.nextInt(transRates.length)]);
                    burstLoadLinkConfigList.add(linkConfig);
                }
            }
        }
        return burstLoadLinkConfigList;
    }

//    public static void main(String[] args) {
//        BurstLoadLinkGenerator burstLoadLinkGenerator=new BurstLoadLinkGenerator(new int[]{1,3},10,new int[]{2,3});
//        List<BurstLoadLinkConfig> linkConfigList=burstLoadLinkGenerator.generateLinkConfigs();
//        for(BurstLoadLinkConfig linkConfig : linkConfigList){
//            System.out.println(linkConfig);
//        }
//    }
}
