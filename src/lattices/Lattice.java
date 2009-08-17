/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lattices;

import Jama.Matrix;

/**
 * Interface for a Lattice
 * @author Robby McKilliam
 */
public interface Lattice {
    
    /** 
     * Return the volume of the fundamental region
     * of the lattice.  This is the square root of 
     * the determinat of the gram matrix
     */
    double volume();

    /**
     * Return the in radius for this lattice
     */
    double inradius();

    /**
     * Return the convering radius for this lattice
     */
    double coveringRadius();
    
    /*
     * Return the center density:
     * inradius^n / volume;
     */
    double centerDensity();

    /**
     * Set/reset the dimension of this lattice.
     * There are reasonable arguments for this method being deprecated
     * (i.e. it's mutable).
     */
    void setDimension(int n);
    
    int getDimension();
    
    /**
     * Return the generator matrix for this latttice
     * @return double[][] containing the generator matrix
     */
    Matrix getGeneratorMatrix();

}
