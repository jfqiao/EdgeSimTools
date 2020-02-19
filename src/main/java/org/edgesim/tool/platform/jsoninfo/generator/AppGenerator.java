package org.edgesim.tool.platform.jsoninfo.generator;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.AppConfig;
import org.edgesim.tool.platform.distribution.TruncatedNormalDistribution;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AppGenerator {
    private int appNum;
    private double inputSizeMean;
    private double inputSizeVar;
    private double outputSizeMean;
    private double outputSizeVar;
    private double workloadMean;
    private double workloadVar;

    private TruncatedNormalDistribution inputDist;
    private TruncatedNormalDistribution outputDist;
    private TruncatedNormalDistribution workloadDist;

    public AppGenerator(int appNum, double inputSizeMean, double inputSizeVar,
                        double outputSizeMean, double outputSizeVar,
                        double workloadMean, double workloadVar) {
        this.appNum = appNum;
        this.inputSizeMean = inputSizeMean;
        this.inputSizeVar = inputSizeVar;
        this.outputSizeMean = outputSizeMean;
        this.outputSizeVar = outputSizeVar;
        this.workloadMean = workloadVar;
        this.inputDist = new TruncatedNormalDistribution(inputSizeMean, inputSizeVar,
                inputSizeMean - inputSizeVar, inputSizeMean + inputSizeVar);
        this.outputDist = new TruncatedNormalDistribution(outputSizeMean, outputSizeVar,
                outputSizeMean - outputSizeVar, outputSizeMean + outputSizeVar);
        this.workloadDist = new TruncatedNormalDistribution(workloadMean, workloadVar,
                workloadMean - workloadVar, workloadMean + workloadVar);
    }

    public List<AppConfig> generateApp() {
        // 本实验中，所有的app只有一个节点，没有边。
        List<AppConfig> appConfigs = new ArrayList<>(appNum);
        String defaultAppName = "app-%d";
        String defaultAppNodeName = "_node-%d";
        for (int i = 0; i < appNum; i++) {
            AppConfig appConfig = new AppConfig();
            appConfig.setAppName(String.format(defaultAppName, i));
            List<AppConfig.AppNode> appNodes = new ArrayList<>(1);
            AppConfig.AppNode node = new AppConfig.AppNode();
            node.setNodeName(appConfig.getAppName() + String.format(defaultAppNodeName, 1));
            node.setInputSize(inputDist.sample());
            node.setOutputSize(outputDist.sample());
            node.setWorkload(workloadDist.sample());
            appNodes.add(node);
            appConfig.setNodes(appNodes);
//            appConfig.setEdges(null); 暂时没有edge
            appConfigs.add(appConfig);
        }
        return appConfigs;
    }
}
