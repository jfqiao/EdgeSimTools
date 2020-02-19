package org.edgesim.tool.platform.distribution;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;

/**
 * 截断正态分布的实现，利用math3中的正态分布实现。
 *
 * @author jfqiao
 * @since 2019/12/29
 */
public class TruncatedNormalDistribution implements ContinuousDistribution {

    private NormalDistribution normalDistribution;
    private double left;
    private double right;

    public TruncatedNormalDistribution(double mean, double var, double left, double right) {
        super();
        this.normalDistribution = new NormalDistribution(mean, var);
        this.left = left;
        this.right = right;
    }

    @Override
    public double sample() {
        double data = normalDistribution.sample();
        if (data < left) {
            return left;
        }
        if (data > right) {
            return right;
        }
        return Double.parseDouble(String.format("%.4f", data));
    }
}
