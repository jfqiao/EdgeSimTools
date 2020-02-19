package org.edgesim.tool.platform.jsoninfo.generator;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.distribution.TruncatedNormalDistribution;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.EdgeConfig;
import org.edgesim.tool.platform.jsoninfo.config.resalloc.LinkConfig;

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
    private double cloudBandwidthRate = 0.3;
    private TruncatedNormalDistribution bandwidthDist;

    public LinkGenerator(List<EdgeConfig> edgeConfigs, double bandwidthMean, double bandwidthVar) {
        this.edgeConfigs = edgeConfigs;
        this.bandwidthMean = bandwidthMean;
        this.bandwidthVar = bandwidthVar;
        this.bandwidthDist = new TruncatedNormalDistribution(bandwidthMean, bandwidthVar,
                bandwidthMean - bandwidthVar, bandwidthMean + bandwidthVar);
    }

    public List<LinkConfig> generateLink() {
        List<LinkConfig> linkConfigs = new ArrayList<>();
        // 先添加边缘服务器与边缘服务器的连接配置。
        for (int i = 0; i < this.edgeConfigs.size() - 1; i++) {
            for (int j = i; j < this.edgeConfigs.size() - 1; j++) {
                LinkConfig linkConfig = new LinkConfig();
                double bandwidth = bandwidthDist.sample();
                if (i != j) {
//                    linkConfig.setDownLink(bandwidthDist.sample());
//                    linkConfig.setUpLink(bandwidthDist.sample());
                    linkConfig.setDownLink(bandwidth);
                    linkConfig.setUpLink(bandwidth);
                } else {
                    linkConfig.setDownLink(SELF_BANDWIDTH);
                    linkConfig.setUpLink(SELF_BANDWIDTH);
                }
                linkConfig.setStartEdgeName(this.edgeConfigs.get(i).getName());
                linkConfig.setEndEdgeName(this.edgeConfigs.get(j).getName());
                linkConfigs.add(linkConfig);
            }
        }
        // 添加云服务器的配置
        for (int i = 0; i < this.edgeConfigs.size() - 1; i++) {
            LinkConfig linkConfig = new LinkConfig();
            linkConfig.setUpLink(bandwidthDist.sample() * this.cloudBandwidthRate);
            linkConfig.setDownLink(bandwidthDist.sample() * this.cloudBandwidthRate);
            linkConfig.setStartEdgeName(this.edgeConfigs.get(this.edgeConfigs.size() - 1).getName());
            linkConfig.setEndEdgeName(this.edgeConfigs.get(i).getName());
            linkConfigs.add(linkConfig);
        }
        LinkConfig cloudSelfConfig = new LinkConfig();
        cloudSelfConfig.setStartEdgeName(this.edgeConfigs.get(this.edgeConfigs.size() - 1).getName());
        cloudSelfConfig.setEndEdgeName(cloudSelfConfig.getStartEdgeName());
        cloudSelfConfig.setDownLink(SELF_BANDWIDTH);
        cloudSelfConfig.setUpLink(SELF_BANDWIDTH);
        linkConfigs.add(cloudSelfConfig);
        return linkConfigs;
    }
}
