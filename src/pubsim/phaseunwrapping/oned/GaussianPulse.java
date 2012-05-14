/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.phaseunwrapping.oned;

import pubsim.distributions.GaussianNoise;
import pubsim.distributions.RealRandomVariable;
import pubsim.VectorFunctions;

/**
 * Construct a wrapped, noisy gaussian pulse.
 * By default the noise variace is 0.
 * @author Robby McKilliam
 */
public class GaussianPulse implements WrappedData{

    RealRandomVariable noise = new GaussianNoise(0.0,0.0);
    int N;
    double[] y, yw, u;
    double a, b;

    public GaussianPulse(){
        setParameters(1.0, 1.0);
        setSize(10);
    }

    public GaussianPulse(int N, double amplitude, double stretch){
        setParameters(amplitude, stretch);
        setSize(N);
    }

    public double[] getWrappedData() {
        return yw;
    }

    public double[] getTrueData() {
        return y;
    }

    public void generateData(){
        double off = N/2.0;
        for(int i = 0; i < N; i ++){
            y[i] = a * Math.exp(-b*(i - off)*(i - off)) + noise.getNoise();
            u[i] = -Math.round(y[i]);
            yw[i] = y[i] + u[i];
        }
    }

    public void setNoiseGenerator(RealRandomVariable noise) {
        this.noise = noise;
    }

    public void setParameters(double amplitude, double stretch){
        a = amplitude;
        b = stretch;
    }

    public void setSize(int N) {
        this.N = N;
        y = new double[N];
        yw = new double[N];
        u = new double[N];
    }

    public int getSize() {
        return N;
    }

    public double[] getWrappedIntegers() {
        return u;
    }


}
