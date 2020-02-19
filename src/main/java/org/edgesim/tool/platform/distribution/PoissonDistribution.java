package org.edgesim.tool.platform.distribution;

import org.cloudbus.cloudsim.distributions.ContinuousDistribution;

/**
 * 泊松分布随机数产生器.
 *
 * @author jfqiao
 * @since 2019/10/20
 */
public class PoissonDistribution implements ContinuousDistribution {

    private double lambda;

    public PoissonDistribution(double lambda) {
        this.lambda = lambda;
    }

    @Override
    public double sample() {
       double z = Math.random();
       return -(1 / lambda) * Math.log(z);
    }
}
