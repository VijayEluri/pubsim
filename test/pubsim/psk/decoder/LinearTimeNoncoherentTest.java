/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.psk.decoder;

import pubsim.psk.decoder.PSKSignal;
import pubsim.psk.decoder.Util;
import pubsim.psk.decoder.LinearTimeNoncoherent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pubsim.distributions.RealRandomVariable;
import pubsim.VectorFunctions;
import static org.junit.Assert.*;

/**
 *
 * @author robertm
 */
public class LinearTimeNoncoherentTest {

    public LinearTimeNoncoherentTest() {
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
     * Test of decode method, of class LinearTimeNoncoherent.
     */
    @Test
    public void decode() {
        System.out.println("decode");
        
        int iters = 100;
        int M = 4;
        int T = 10;
        
        PSKSignal signal = new PSKSignal();
        signal.setM(M);
        signal.setLength(T);
        signal.setChannel(1.0/Math.sqrt(2.0), 1.0/Math.sqrt(2.0));
        //signal.generateChannel();
        
        RealRandomVariable noise = new pubsim.distributions.UniformNoise(0.0, 0.001);
        signal.setNoiseGenerator(noise);  
               
        signal.generateReceivedSignal();
        
        //System.out.println(" recsig = " + VectorFunctions.print(signal.getReceivedSignal()));
        
        LinearTimeNoncoherent instance = new LinearTimeNoncoherent(T,M);

        for(int i = 0; i < iters; i++){
            //signal.generateChannel();
            signal.generatePSKSignal();
            signal.generateReceivedSignal();
            
            double[] result = instance.decode(signal.getReceivedSignal());
            
            System.out.println(" result = " + VectorFunctions.print(result));
            System.out.println(" expect = " + VectorFunctions.print(signal.getPSKSignal()));
            double[] s = VectorFunctions.subtract(signal.getPSKSignal(), result);
            System.out.println("s = " + VectorFunctions.print(s));
        
            assertTrue(Util.differentialEncodedEqual(signal.getPSKSignal(), result, M));
            
        }
    }

}