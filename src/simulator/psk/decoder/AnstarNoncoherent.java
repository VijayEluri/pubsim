/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.psk.decoder;

import lattices.Anstar;
import lattices.AnstarBucketVaughan;
import simulator.Complex;

/**
 * Noncoherent receiver using the linear time nearest point algorihtm
 * for An*.
 * @author Robby McKilliam
 */
public class AnstarNoncoherent implements PSKReceiver{
    
    double[] argy;
    Anstar anstar;
    int T, M;
    
    public AnstarNoncoherent(){
        anstar = new AnstarBucketVaughan(); //this is the O(nlogn) An* algorithm
    }

    public void setM(int M) {
        this.M = M;
    }

    public void setT(int T) {
        this.T = T;
        argy = new double[T];
        anstar.setDimension(T-1);
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
        //System.out.println();
        //System.out.println(VectorFunctions.print(y));
        //System.out.println("argy = " + VectorFunctions.print(argy));
        
        anstar.nearestPoint(argy);
        
        return anstar.getIndex();
        
    }

    public int bitErrors(double[] x) {
        return Util.differentialEncodedBitErrors(anstar.getIndex(), x, M);
    }

    /** This is a noncoherent reciever so setting the channel does nothing*/
    public void setChannel(Complex h) {  }
    
    public int bitsPerCodeword() {
        //return (int)Math.round((T-1)*Math.log(M)/Math.log(2));
        return (int)Math.round(T*Math.log(M)/Math.log(2));
    }

    public int symbolErrors(double[] x) {
        return Util.differentialEncodedSymbolErrors(anstar.getIndex(), x, M);
    }

    public boolean codewordError(double[] x) {
        return !Util.differentialEncodedEqual(anstar.getIndex(), x, M);
    }

}
