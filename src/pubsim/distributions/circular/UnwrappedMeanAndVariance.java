/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.distributions.circular;

import pubsim.distributions.RandomVariable;
import flanagan.integration.IntegralFunction;
import flanagan.integration.Integration;
import static pubsim.Util.fracpart;

/**
 * Computes the unwrapped mean and variance of a circular distribution
 * @author Robby McKilliam
 */
public class UnwrappedMeanAndVariance {

    protected final RandomVariable dist;
    protected double mean;
    protected double var;
    protected int numsamples = 1000;

    /** Input is a distribution */
    public UnwrappedMeanAndVariance( RandomVariable tdist ){
        this.dist = tdist;

        var = Double.POSITIVE_INFINITY;
        mean = 0;

        for(double t = -0.5; t < 0.5; t += 1.0/numsamples){
            double tvar = computeWrappedVarianceAbout(t, dist, 1e-8);
            if( tvar < var ){
                var = tvar;
                mean = t;
            }
        }

    }

    /** Compute the wrapped variance after applying a rotaton of phi */
    public static double computeWrappedVarianceAbout(final double phi, final RandomVariable dist, double accuracy){
        double tvar = (new Integration(new IntegralFunction() {
            public double function(double x) {
                double rot = fracpart(x-phi);
                return rot*rot*dist.pdf(x);
            }
        }, -0.5, 0.5)).trapezium(accuracy, 10000);
        return tvar;
    }

    public double getUnwrappedVariance(){
        return var;
    }

    /**
     * This is only accurate the the 3rd decimal place.
     * This could be improved by an optimisation routine but
     * I have not implemented it.
     */
    public double getUnwrappedMean(){
        return mean;
    }

    public void computeAndPrint(int numsamples){
        for(double t = -0.5; t < 0.5; t += 1.0/numsamples){
            final double ft = t;
            final int INTEGRAL_STEPS = 1000;
            double tvar = (new Integration(new IntegralFunction() {
                public double function(double x) {
                    double rot = fracpart(x-ft);
                    return rot*rot*dist.pdf(x);
                }
            }, -0.5, 0.5)).trapezium(INTEGRAL_STEPS);
            System.out.println(ft + "\t" + tvar);
        }
    }
    

}
