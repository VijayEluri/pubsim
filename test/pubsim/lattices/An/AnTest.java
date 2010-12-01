/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package robbysim.lattices.An;

import pubsim.lattices.An.An;
import pubsim.lattices.An.AnSorted;
import Jama.Matrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pubsim.QRDecomposition;
import pubsim.VectorFunctions;
import static org.junit.Assert.*;

/**
 *
 * @author Robby
 */
public class AnTest {

    public AnTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getGeneratorMatrix method, of class An.
     */
    @Test
    public void testGetGeneratorMatrix() {
        System.out.println("getGeneratorMatrix");
        int n = 8;
        An instance = new AnSorted(n);
        //instance.setDimension(4);
        Matrix result = instance.getGeneratorMatrix();
        System.out.println(VectorFunctions.print(result));
        System.out.println(VectorFunctions.print(instance.getGeneratorMatrixBig()));

        Matrix M = new Matrix(n+1, n + 1);
        for(int i = 0; i < n+1; i++){
            for(int j = 0; j < n; j++){
                M.set(i, j, result.get(i, j));
            }
        }
        for(int i = 0; i < n+1; i++){
            M.set(i, n, 1.0);
        }

        System.out.println(VectorFunctions.print(M));
        pubsim.QRDecomposition QR = new QRDecomposition(M);
        System.out.println(VectorFunctions.print(QR.getR()));

    }

}