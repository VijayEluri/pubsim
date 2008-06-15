/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.psk.decoder;

import java.util.Arrays;
import lattices.Anm;
import simulator.Complex;
import simulator.IndexedDouble;
import simulator.VectorFunctions;

/**
 *
 * @author robertm
 */
public class CoxeterNoncoherentReciever implements PSKReceiver{
    
    double[] argy, u, v;
    IndexedDouble[] z;
    int T, M;
    
    public CoxeterNoncoherentReciever(){
    }

    public void setM(int M) {
        this.M = M;
        setT(T);
    }

    /** 
     * You must set T and M such
     * they are relatively prime.
     * @param T the bock length
     */
    public void setT(int T) {
        this.T = T;
        argy = new double[T];
        u = new double[T];
        v = new double[T];
        z = new IndexedDouble[T];
        for(int i = 0; i < T; i++)
            z[i] = new IndexedDouble();
    }

    /** Implements the Sweldens Noncoherent decoder using the O(nlogn)
     * nearest point algorithm for An*.
     * @param y the PSK symbols
     * @return the index of the nearest lattice point
     */
    public double[] decode(Complex[] y) {
        if(y.length != T) setT(y.length);
        
        //calculate the argument of of y and scale
        //so that the symbols are given by integers
        //in the range {0,1,...,M-1}
        for(int i = 0; i < T; i++){
            //System.out.print(y[i].phase());
            //double p = M/(2*Math.PI)*y[i].phase();
            argy[i] = M/(2*Math.PI)*y[i].phase();
        }
        
        //must project to ensure that ambiguities are not found
        //Anm.project(argy, argy);
        
        int sumM = 0;
        double a = 0, b = 0;
        for(int i = 0; i < T; i++){
            u[i] = Math.round(argy[i]);
            sumM += u[i];
            z[i].value = argy[i] - u[i];
            z[i].index = i;
            a += z[i].value;
            b += z[i].value * z[i].value;
        }
        
        Arrays.sort(z);
        
        double D = Double.POSITIVE_INFINITY;
        int m = 0;
        for(int i = 0; i < M*T; i++){
            double dist = b - a*a/T;
            if(dist < D && sumM%M == 0){
                D = dist;
                m = i;
            }
            sumM++;
            a -= 1;
            b += -2*z[T - 1 - i%T].value + 1.0;
            z[T - 1 - i%T].value -= 1.0;
            
            //System.out.println("numloops");
            //System.out.println("T = " + T);
            //System.out.println("t = " + (T - 1 - i%T));
            //System.out.println("a = " + a + ", b = " + b + ", dist = " + dist);
        }
        
        //System.out.println("m = " + m);
        
        for(int i = 0; i < m; i++)
            u[z[T - 1 - i%T].index] += 1;
        
        return u;
        
    }

    /** This is a noncoherent reciever so setting the channel does nothing*/
    public void setChannel(Complex h) {  }
    
    public int bitsPerCodeword() {
        //return (int)Math.round((T-1)*Math.log(M)/Math.log(2));
        return (int)Math.round((T)*Math.log(M)/Math.log(2));
    }

    /** 
     * We simply ignore the last symbol when calculating the bit errors.
     * It is only used to ensure that parity occurs.
     */
    public int bitErrors(double[] x) {
        if(x.length != T) 
             throw new Error("vectors must have length T");
         
         int errors = 0;
         for(int i = 0; i<T-1; i++){
            int xmod = Util.mod((int)Math.round(x[i]), M);
            int lmod = Util.mod((int)Math.round(u[i]), M);
            errors += Util.mod((int)Math.round(xmod-lmod), M/2+1);
         }
         return errors;
    }

    public int symbolErrors(double[] x) {
        return Util.SymbolErrors(u, x, M);
    }

    public boolean codewordError(double[] x) {
        return Util.codewordError(x, u, M);
    }

}
