/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.bearing;

/**
 * Interface for a Bearing estimator
 * @author Robby McKilliam
 */
public interface BearingEstimator {
    
    double estimateBearing(double[] y);
    
}