/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package robbysim.fes;

import robbysim.distributions.RandomVariable;
import robbysim.distributions.circular.CircularRandomVariable;

/**
 * Like PolynommialPhaseSignal but the noise gets added to the phase.
 * @author Robby McKilliam
 */
public class CircularNoiseSingleFrequencySignal extends NoisyComplexSinusoid{

    public CircularNoiseSingleFrequencySignal(int N){
        super(N);
    }

    @Override
    public double[] generateReceivedSignal() {
        for(int t = 0; t < N; t++){
            double phi = t*f + p;
            double pnoise = noise.getNoise();
            real[t] = Math.cos(2*Math.PI*(phi + pnoise));
            imag[t] = Math.sin(2*Math.PI*(phi + pnoise));
        }
        return null;
    }

}