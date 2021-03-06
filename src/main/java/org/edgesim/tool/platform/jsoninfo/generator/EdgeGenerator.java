package org.edgesim.tool.platform.jsoninfo.generator;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.distribution.TruncatedNormalDistribution;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.CloudConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.EdgeConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jfqiao
 * @since 2019/12/30
 */
@Getter
@Setter
public class EdgeGenerator {
    private int edgeNum;
    // 云服务器计算能力比例
    private double cloudComputingPowerRate = 10;
    private double computingPowerMean;
    private double computingPowerVar;
    // 云服务器单价比例
    private double cloudPriceRate = 1.2;
    private double priceMean;
    private double priceVar;

    private PositionGenerator positionGenerator;

    private TruncatedNormalDistribution computingPowerDist;
    private TruncatedNormalDistribution priceDist;

    public EdgeGenerator(int edgeNum, double computingPowerMean, double computingPowerVar,
                         double priceMean, double priceVar, PositionGenerator positionGenerator) {
        this.edgeNum = edgeNum;
        this.computingPowerMean = computingPowerMean;
        this.computingPowerVar = computingPowerVar;
        this.priceMean = priceMean;
        this.priceVar = priceVar;
        this.computingPowerDist = new TruncatedNormalDistribution(computingPowerMean, computingPowerVar,
                computingPowerMean - computingPowerVar, computingPowerMean + computingPowerVar);
        this.priceDist = new TruncatedNormalDistribution(priceMean, priceVar,
                priceMean - priceVar, priceMean + priceVar);
        this.positionGenerator = positionGenerator;
    }

    public List<EdgeConfig> generateEdge() {
        // 先调用位置生成器
        positionGenerator.generateCloudPos();
        positionGenerator.generateEdgePos(edgeNum);

        List<EdgeConfig> edgeConfigs = new ArrayList<>(edgeNum + 1);
        // 最后一个添加云服务器的配置
        String defaultEdgeName = "edge-%d";
        for (int i = 0; i < edgeNum; i++) {
            EdgeConfig edgeConfig = new EdgeConfig();
            edgeConfig.setName(String.format(defaultEdgeName, i));
            edgeConfig.setPrice(priceDist.sample());
            edgeConfig.setComputingPower(computingPowerDist.sample());
            edgeConfig.setX(positionGenerator.getEdgeConfigs().get(i).getX());
            edgeConfig.setY(positionGenerator.getEdgeConfigs().get(i).getY());
            edgeConfigs.add(edgeConfig);
        }
        CloudConfig cloudConfig = new CloudConfig();
        cloudConfig.setPrice(priceDist.sample() * this.cloudPriceRate);
        cloudConfig.setComputingPower(computingPowerDist.sample() * this.cloudComputingPowerRate);
        cloudConfig.setX(positionGenerator.getCloudConfig().getX());
        cloudConfig.setY(positionGenerator.getCloudConfig().getY());
        edgeConfigs.add(cloudConfig);
        return edgeConfigs;
    }
}
