/*
 * SparseNoisyPeriodicSignal.java
 *
 * Created on 13 April 2007, 14:55
 */

package simulator.pes;

import java.util.Random;
import simulator.*;

/**
 * Generates a set of recieved times that are sparse and
 * noisy versions of the recieved signal.
 * <p>
 * The specific sparse pulses that will be transmitted can
 * be specefied by @func setSparseSignal.
 * 
 * @author Robby McKilliam
 */
public class SparseNoisyPeriodicSignal implements SignalGenerator {
    
    private double[] transmittedSignal;
    private double[] recievedSignal;
    private NoiseGenerator noise;
    private Random rand;
    private double T;
    private int n;
    
    public SparseNoisyPeriodicSignal(){
            rand = new Random();
            transmittedSignal = new double[0];
            recievedSignal = new double[0];
    }
    
    public void setSparseSignal(double[] transmitted){
        transmittedSignal = transmitted;
    }
    
    public void setPeriod(double T){  this.T = T; }
    public double getPeriod(){ return T; }
    
    public void setLength(int n){
        this.n = n;
        transmittedSignal = new double[n];
        recievedSignal = new double[n];
    }
    
    public double[] generateSparseSignal(){
        double count = 0.0;
        int added = 0;
        while(added < n){
            if(rand.nextBoolean()){
                transmittedSignal[added] = count;
                added++;
            }
            count++;
        }  
        return transmittedSignal;
    }
    
    /**
     * Generate a binomial sequence typical of a transmitted
     * sparse signal.
     */
    public double[] generateSparseSignal(int length){
        if( n != length )
            setLength(n);
        return generateSparseSignal();
    }
    
     /**
     * Generate a binomial sequence typical of a transmitted
     * sparse signal.  Seed the random generator so that well always get
     * the same answer.
     */
    public double[] generateSparseSignal(int length, long seed){
        rand.setSeed(seed);      
        return generateSparseSignal(length);
    }
    
    /**
     * Generate a binomial sequence typical of a transmitted
     * sparse signal.
     */
    public double[] generateReceivedSignal() {
          if(transmittedSignal == null )
              throw new java.lang.NullPointerException
                      ("transmitted signal has not been allocated\n" +
                      "call generateSparseSignal(length) first ");
          
          for(int i = 0; i< transmittedSignal.length; i++){
              recievedSignal[i] = T * transmittedSignal[i] + noise.getNoise();
          }
          
          return recievedSignal;
    }
    
    /** Set the noise type for the signal */
    public void setNoiseGenerator(NoiseGenerator noise){
        this.noise = noise;
    }
    public NoiseGenerator getNoiseGenerator(){ return noise; }
    
}
