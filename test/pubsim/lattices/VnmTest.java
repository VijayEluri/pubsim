/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.lattices;

import pubsim.lattices.Vnm;
import Jama.Matrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pubsim.Util;
import pubsim.VectorFunctions;
import static org.junit.Assert.*;

/**
 *
 * @author harprobey
 */
public class VnmTest {

    public VnmTest() {
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
     * Test of logVolume method, of class Vnm.
     */
    @Test
    public void testLogVolume() {
        System.out.println("logVolume");
        Vnm instance = new Vnm(5, 12);
        Matrix gen = instance.getGeneratorMatrix();
        Matrix gram = gen.transpose().times(gen);

        System.out.println(VectorFunctions.print(gram));

        assertEquals(Math.sqrt(gram.det()), instance.volume(), 0.0001);
    }

    /**
     * Test of logVolume method, of class Vnm.
     */
    @Test
    public void testPrintOutShortVects() {
        System.out.println("print out short vectors");
        Vnm instance = new Vnm(6, 40);

        System.out.println(instance.kissingNumber());

        double inrad = instance.inradius();
        System.out.println(inrad * inrad * 4);
    }

    /**
     * Test of logVolume method, of class Vnm.
     */
    @Test
    public void searchForSquares() {
        System.out.println("search for squares");
        int m = 1;
        for(int n = m+1; n < 1000; n++){
            double vol = (new Vnm(4,n)).volume();
            String marker = "";
            if(Math.abs(vol - Math.round(vol)) < 0.000001 ) marker = "******";
            System.out.println(n + "\t" + vol + "\t" + marker);
        }

    }


}