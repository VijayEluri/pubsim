/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pubsim.distributions.processes;

import pubsim.distributions.NoiseGenerator;
import pubsim.distributions.RealRandomVariable;

/**
 * Interface for a stationary process
 * @author Robby McKilliam
 */
public interface StationaryProcess extends NoiseGenerator<Double> {
    
    /** Return the random variable with the marginal pdf/cdf of this process */
    public RealRandomVariable marginal();
    
    public double[] autocorrelation();
    
}
