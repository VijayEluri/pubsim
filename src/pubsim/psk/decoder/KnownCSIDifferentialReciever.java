/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.psk.decoder;

/**
 *
 * @author Robby McKilliam
 */
public class KnownCSIDifferentialReciever extends KnownCSIReciever{

    public KnownCSIDifferentialReciever(int M){
        super(M);
    }

    @Override
    public int bitErrors(double[] x) {
        return Util.differentialEncodedBitErrors(this.x, x, M);
    }

    @Override
    public int bitsPerCodeword() {
        return (int)Math.round((T-1)*Math.log(M)/Math.log(2));
        //return (int)Math.round(T*Math.log(M)/Math.log(2));
    }
    
    @Override
    public int symbolErrors(double[] x) {
        return Util.differentialEncodedSymbolErrors(this.x, x, M);
    }

    @Override
    public boolean codewordError(double[] x) {
        return !Util.differentialEncodedEqual(this.x, x, M);
    }
    
}
