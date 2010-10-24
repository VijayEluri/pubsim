/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lattices.reduction;

import Jama.Matrix;
import simulator.VectorFunctions;
import static simulator.VectorFunctions.addMultipleOfColiToColj;

/**
 * The is Sloanes reduction for producing an approximation of the dual
 * of a lattice that is given by the projection of the integer lattice.
 *
 * Obviously to get an approximation to the lattice itself you need to
 * compute the generator of the dual (i.e. the inverse or psuedoinverse).
 *
 * @author Robby McKilliam
 */
public class SloanesReduction {

    protected final double[] v;

    /**
     * Assumes L is a square, upper triangle basis matrix for
     * the lattice. The integer w is the approximation parameter.
     * Large w gives more accurate approximation but also larger
     * projection vectors (which typically means more complexity
     * for decoders etc.).
     */
    public SloanesReduction(Matrix L, int w){
        Matrix Lt = specialColumnReduce(computeSlonesLw(L, w));
        v = VectorFunctions.getRow(0, Lt);
        //invert the elements in v
        for(int i = 0; i < v.length; i++) v[i] = -v[i];
    }

    public double[] getProjectionVector() { return v; };

    /**
     * Returns an upper triangular basis for the matrix by computing
     * the Gram matrix and taking its cholesky factorisation.
     */
    public static Matrix upperTriangularBasis(Matrix B){
        Matrix A = B.transpose().times(B);
        return A.chol().getL().transpose();
    }

    /**
     * Compute the Lw matrix in Sloanes paper. Note I acutally
     * use the transpose of Sloanes Lw. Assumes that L is square
     * and upper triangular.
     */
    public static Matrix computeSlonesLw(Matrix L, int w){
        int n = L.getColumnDimension();
        Matrix Lw = new Matrix(n+1,n);

        //set Lw to L appropriately
        for(int i = 0; i < n; i++)
            for(int j = i; j < n; j++) Lw.set(i,j,-Math.floor(w*L.get(i, j)));

        //set special diagonal ones
        for(int j = 0; j < n; j++) Lw.set(j+1,j,1.0);

        return Lw;
    }

    /** 
     * Does the special column reduction leaving the first
     * row filled. Requires a n+1 by n matrix and assumes
     * the first few elements the second diagonal is all ones.
     * i.e. expects a matrix of type output by computeSloanesLw
     */
    public static Matrix specialColumnReduce(Matrix L){
        Matrix Lt = Matrix.constructWithCopy(L.getArrayCopy());
        int n = Lt.getColumnDimension();
        for(int i = 1; i < n; i++)
            for(int j = i; j < n; j++)
               addMultipleOfColiToColj(Lt, -Lt.get(i, j), i-1, j);
        return Lt;
    }

}
