/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.lattices;

import Jama.Matrix;
import pubsim.lattices.decoder.SphereDecoderSchnorrEuchner;

/**
 * General lattice with a nearest point algorithm included with it.
 * By default the sphere decoder is used but you can use other
 * algorithms by using the appropriate constructor
 * @author Robby McKilliam
 */
public class GeneralLatticeAndNearestPointAlgorithm extends GeneralLattice implements LatticeAndNearestPointAlgorithm {

    private NearestPointAlgorithm decoder;

    public GeneralLatticeAndNearestPointAlgorithm(Matrix B){
        this.B = B;
        decoder = new SphereDecoderSchnorrEuchner(this);
    }

    public GeneralLatticeAndNearestPointAlgorithm(double[][] B){
        this.B = new Matrix(B);
        decoder = new SphereDecoderSchnorrEuchner(this);
    }

     public GeneralLatticeAndNearestPointAlgorithm(Matrix B, NearestPointAlgorithm np){
        this.B = B;
        decoder = np;
    }

    public void nearestPoint(double[] y) {
        decoder.nearestPoint(y);
    }
    
    public void nearestPoint(Double[] y) {
        decoder.nearestPoint(y);
    }

    public double[] getLatticePoint() {
        return decoder.getLatticePoint();
    }

    public double[] getIndex() {
        return decoder.getIndex();
    }

    public double distance() {
        return decoder.distance();
    }

}
