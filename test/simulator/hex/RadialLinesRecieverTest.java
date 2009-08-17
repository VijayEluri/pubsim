/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.hex;

import distributions.GaussianNoise;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import simulator.Point2;
import simulator.VectorFunctionsTest;
import static simulator.VectorFunctions.print;

/**
 *
 * @author Robby McKilliam
 */
public class RadialLinesRecieverTest {

    public RadialLinesRecieverTest() {
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
     * Test of nextHexangonalNearPoint method, of class RadialLinesReciever.
     */
    @Test
    public void testNextHexangonalNearPoint() {
        System.out.println("nextHexangonalNearPoint");
        double d1 = 1.0;
        double d2 = 0.0;
        double c1 = 0.0;
        double c2 = 0.0;
        double expr = 0.5;
        RadialLinesReciever.DoubleAndPoint2AndIndex dp
            = RadialLinesReciever.nextHexangonalNearPoint(d1, d2, c1, c2);       
        assertEquals(expr, dp.value, 0.0000001);

        //System.out.println(dp.value);
        //System.out.println(dp.point);

        d1 = 0.5;
        d2 = Math.sqrt(3)/2;
        expr = 0.5;
        dp = RadialLinesReciever.nextHexangonalNearPoint(d1, d2, c1, c2);
        assertEquals(expr, dp.value, 0.0000001);

        //System.out.println(dp.value);
        //System.out.println(dp.point);

    }

    /**
     * Test of nextHexangonalNearPoint method, of class RadialLinesReciever.
     */
    @Test
    public void correctlyReturnCodewordNearestOrigin() {
        System.out.println("correctlyReturnCodewordNearestOrigin");
        
        int N = 5;
        RadialLinesReciever rec = new RadialLinesReciever(N,
                new HexagonalCode(2));

        double[] yr = new double[N];
        double[] yi = new double[N];

        rec.decode(yr, yi);
        
        double[] ur = rec.getReal();
        double[] ui = rec.getImag();
        //System.out.println(print(rec.getReal()));
        //System.out.println(print(rec.getImag()));

        for(int n = 0; n < N; n++){
            assertEquals(0.0, ur[n], 0.0000001);
            assertEquals(0.0, ui[n], 0.0000001);
        }

    }

    /**
     * Test of nextHexangonalNearPoint method, of class RadialLinesReciever.
     */
    @Test
    public void decodeSymbolsCorrectly() {
        System.out.println("decodeSymbolsCorrectly");

        int N = 3;
        int M = 4;
        RadialLinesReciever rec = new RadialLinesReciever(N, M);

        FadingNoisyHex signal = new FadingNoisyHex(N, M);
        signal.setChannel(1.0, 0.0);
        signal.setNoiseGenerator(new GaussianNoise(0.0, 0.00000000));

        signal.generateCodeword();
        signal.generateReceivedSignal();
        double[] yr = signal.getReal();
        double[] yi = signal.getImag();

        rec.decode(yr, yi);

        double[] urt = signal.getTransmittedRealCodeword();
        double[] uit = signal.getTransmittedImagCodeword();
        double[] ur = rec.getReal();
        double[] ui = rec.getImag();

        System.out.println(print(urt));
        System.out.println(print(uit));
        System.out.println(print(ur));
        System.out.println(print(ui));

        VectorFunctionsTest.assertVectorsEqual(urt, ur);
        VectorFunctionsTest.assertVectorsEqual(uit, ui);

    }


}