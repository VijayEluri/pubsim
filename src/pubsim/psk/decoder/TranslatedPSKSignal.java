/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.psk.decoder;

import pubsim.Complex;

/**
 * Generates aPSK signal with each symbol translated by
 * a pilot symbol.
 * @author Robby McKilliam
 */
public class TranslatedPSKSignal extends PSKSignal 
        implements pubsim.qam.pat.PATSymbol{
    
    /** 
     * Generates the received QAM symbols with the pilot symbol
     * added to each symbol.
     */
    @Override
    public Double[] generateReceivedSignal() {
        for(int i=0; i < y.length; i++){
            Complex xc = new Complex(Math.cos(2*Math.PI*x[i]/M), 
                                     Math.sin(2*Math.PI*x[i]/M));
            Complex nos = new Complex(noise.getNoise(), noise.getNoise());
            y[i] = h.times(xc).plus(nos).plus(PAT);
        }
        return null;
    }
    
    
    /** The PAT symbol used */
    protected Complex PAT;

    @Override
    public void setPATSymbol(double real, double imag) {
        PAT = new Complex(real, imag);
    }

    @Override
    public void setPATSymbol(Complex c) {
        PAT = new Complex(c);
    }

    @Override
    public Complex getPATSymbol() {
        return PAT;
    }
    

}
