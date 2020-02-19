package org.edgesim.tool.platform.redundancy.jsoninfo.generator;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.distribution.TruncatedNormalDistribution;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.EdgeConfig;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.LinkConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jfqiao
 * @since 2019/12/30
 */
@Getter
@Setter
public class LinkGenerator {

    private static final double SELF_BANDWIDTH = 1e100;

    private List<EdgeConfig> edgeConfigs;
    private double bandwidthMean;
    private double bandwidthVar;
    private double distance;
    private double cloudBandwidthRate = 0.3;
    private TruncatedNormalDistribution bandwidthDist;

    public LinkGenerator(List<EdgeConfig> edgeConfigs, double bandwidthMean, double bandwidthVar) {
        this.edgeConfigs = edgeConfigs;
        this.bandwidthMean = bandwidthMean;
        this.bandwidthVar = bandwidthVar;
        this.bandwidthDist = new TruncatedNormalDistribution(bandwidthMean, bandwidthVar,
                bandwidthMean - bandwidthVar, bandwidthMean + bandwidthVar);
    }

    public LinkGenerator(List<EdgeConfig> edgeConfigs, double distance){
        this.edgeConfigs = edgeConfigs;
        this.distance = distance;
    }

    public List<LinkConfig> generateLink() {
        List<LinkConfig> linkConfigs = new ArrayList<>();
        // 先添加边缘服务器与边缘服务器的连接配置。
        for (int i = 0; i < this.edgeConfigs.size() - 1; i++) {
            for (int j = i; j < this.edgeConfigs.size() - 1; j++) {
                LinkConfig linkConfig = new LinkConfig();
                if (i != j) {
                    linkConfig.setDistance(this.distance);
                } else {
                    linkConfig.setDistance(0);
                }
                linkConfig.setStartEdgeName(this.edgeConfigs.get(i).getName());
                linkConfig.setEndEdgeName(this.edgeConfigs.get(j).getName());
                linkConfigs.add(linkConfig);
            }
        }
        // 添加云服务器的配置
        for (int i = 0; i < this.edgeConfigs.size() - 1; i++) {
            LinkConfig linkConfig = new LinkConfig();
            linkConfig.setDistance(0);
            linkConfig.setStartEdgeName(this.edgeConfigs.get(this.edgeConfigs.size() - 1).getName());
            linkConfig.setEndEdgeName(this.edgeConfigs.get(i).getName());
            linkConfigs.add(linkConfig);
        }
        LinkConfig cloudSelfConfig = new LinkConfig();
        cloudSelfConfig.setStartEdgeName(this.edgeConfigs.get(this.edgeConfigs.size() - 1).getName());
        cloudSelfConfig.setEndEdgeName(cloudSelfConfig.getStartEdgeName());
        cloudSelfConfig.setDistance(0);
        linkConfigs.add(cloudSelfConfig);
        return linkConfigs;
    }
}
