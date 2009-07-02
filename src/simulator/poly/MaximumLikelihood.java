/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.poly;

import Jama.Matrix;
import lattices.util.PointInParallelepiped;
import optimisation.AutoDerivativeFunction;
import optimisation.FunctionAndDerivatives;
import optimisation.NewtonRaphson;
import simulator.VectorFunctions;

/**
 * Implements a (approximate) maximum likelihood estimator for
 * polynomial phase signals.  This samples the identifiable region
 * performing Newton's method a whole lot of times.
 * @author Robby McKilliam
 */
public class MaximumLikelihood implements PolynomialPhaseEstimator{

    int a;
    int N;
    int samples[];
    protected AmbiguityRemover ambiguityRemover;

    //Here for inheritance purposes.  You can't call this.
    protected MaximumLikelihood() {
    }

    /**
     * @param a : polynomail order
     * @param samples : number of samples used per parameter in ML search.
     * Deafult samples = 100
     */
    public MaximumLikelihood(int a, int samples){
        this.a = a;
        this.samples = new int[a];
        for(int i = 0; i < a; i++)
            this.samples[i] = samples;
        ambiguityRemover = new AmbiguityRemover(a);
    }

    /**
     * @param a : polynomail order
     * @param samples : number of samples used per parameter in ML search.
     * Deafult samples = 100
     */
    public MaximumLikelihood(int a, int[] samples){
        this.a = a;
        this.samples = samples;
        ambiguityRemover = new AmbiguityRemover(a);
    }
    
    /**
     * @param samples : number of samples used per parameter in ML search.
     */
    public void setSamples(int[] samples){
        this.samples = samples;
    }

    @Override
    public void setSize(int n) {
        N = n;
    }

    public int getOrder() {
        return a;
    }

    public double[] estimate(double[] real, double[] imag) {
        if (N != real.length) {
            setSize(real.length);
        }

        PolynomialPhaseLikelihood func
                = new PolynomialPhaseLikelihood(real, imag);
        NewtonRaphson newtonRaphson
                = new NewtonRaphson(func);

        //System.out.println(ambiguityRemover.getBasisMatrix()==null);

        PointInParallelepiped points
                = new PointInParallelepiped(ambiguityRemover.getBasisMatrix(),
                                            samples);

        Matrix p = null;
        double D = Double.NEGATIVE_INFINITY;
        while(points.hasMoreElements()){
            //Matrix pt = newtonRaphson.maximise(points.nextElement());
            //System.out.println("here");
            Matrix pt = points.nextElement();
            double dist = func.value(pt);
            if(dist > D){
                D = dist;
                p = pt.copy();
                //System.out.println(VectorFunctions.print(pt));
            }
        }
        //double[] parray = {0.1,0.1};
        //double dist = func.value(VectorFunctions.columnMatrix(parray));
        //p = VectorFunctions.columnMatrix(parray);
        try{
            p = newtonRaphson.maximise(p);
        }catch(Exception e){
            throw new ArithmeticException(e.getMessage());
        }
        //System.out.println(dist);
        //System.out.println(D);
        //System.out.println(VectorFunctions.print(p));
        return ambiguityRemover.disambiguate(VectorFunctions.unpackRowise(p));
    }

    public double[] error(double[] real, double[] imag, double[] truth) {
        double[] est = estimate(real, imag);
        double[] err = new double[est.length];

        for (int i = 0; i < err.length; i++) {
            err[i] = est[i] - truth[i];
        }
        err = ambiguityRemover.disambiguate(err);
        for (int i = 0; i < err.length; i++) {
            err[i] = err[i]*err[i];
        }
        return err;
    }

    public static class PolynomialPhaseLikelihoodAutoDerivative
            extends AutoDerivativeFunction {

        double[] yr, yi;
        int N;

        protected PolynomialPhaseLikelihoodAutoDerivative(){}

         /**
         * @param yr real part of signal
          * @param yi complex part of signal
         */
        public PolynomialPhaseLikelihoodAutoDerivative(double[] yr, double[] yi){
            this.yr = yr;
            this.yi = yi;
            N = yr.length;
            interval = 1e-6;
        }

        /**
         * x is a column vector containing the polynomial phase
         * parameter.  x = [p0, p1, p2, ... ]
         * @return Value of the likelihood function for these parameters
         */
        public double value(Matrix x) {
            int M = x.getRowDimension();
            double val = 0.0;
            for(int n = 0; n < N; n++){
                double phase = 0.0;
                for(int m = 0; m < M; m++){
                    double p = x.get(m, 0);
                    phase += p * Math.pow(n+1, m);
                }
                double real = Math.cos(2*Math.PI*phase);
                double imag = Math.sin(2*Math.PI*phase);
                val += (yr[n] - real)*(yr[n] - real);
                val += (yi[n] - imag)*(yi[n] - imag);
            }
            return -val;
        }
    }

    public static class PolynomialPhaseLikelihood
            implements FunctionAndDerivatives{

        double[] yr, yi;
        double[] ymag, yphase;
        double[] spdiff, cpdiff;
        int N;

         /**
         * @param yr real part of signal
          * @param yi complex part of signal
         */
        public PolynomialPhaseLikelihood(double[] yr, double[] yi){
            N = yr.length;
            this.yr = yr;
            this.yi = yi;
            ymag = new double[N];
            yphase = new double[N];
            spdiff = new double[N];
            cpdiff = new double[N];
            for(int n = 0; n < N; n++){
                ymag[n] = Math.sqrt(yr[n]*yr[n] + yi[n]*yi[n]);
                yphase[n] = Math.atan2(yi[n], yr[n]);
            }
            N = yr.length;
        }

        public double value(Matrix x) {
            int M = x.getRowDimension();
            double val = 0.0;
            for(int n = 0; n < N; n++){
                double phase = 0.0;
                for(int m = 0; m < M; m++){
                    double p = x.get(m, 0);
                    phase += p * Math.pow(n+1, m);
                }
                double real = Math.cos(2*Math.PI*phase);
                double imag = Math.sin(2*Math.PI*phase);
                val += (yr[n] - real)*(yr[n] - real);
                val += (yi[n] - imag)*(yi[n] - imag);
            }
            return -val;
        }

        public Matrix hessian(Matrix x) {
            int M = x.getRowDimension();
            //precompute required sin values
            for(int n = 0; n < N; n++){
                double phase = 0.0;
                for(int m = 0; m < M; m++){
                    phase += x.get(m, 0)*Math.pow((n+1), m);
                }
                cpdiff[n] = Math.cos(2*Math.PI * phase - yphase[n]);
            }
            //compute hessian elements
            Matrix H = new Matrix(M, M);
            for(int m = 0; m < M; m++){
                for(int k = 0; k < M; k++){
                    double grad2 = 0.0;
                    for(int n = 0; n < N; n++){
                        grad2 += ymag[n] * Math.pow((n+1), m) *
                                Math.pow((n+1), k) * cpdiff[n];
                    }
                    grad2 *= -8*Math.PI*Math.PI;
                    H.set(m,k, grad2);
                }
            }
            return H;
        }

        public Matrix gradient(Matrix x) {
            int M = x.getRowDimension();
            //precompute required sin values
            for(int n = 0; n < N; n++){
                double phase = 0.0;
                for(int m = 0; m < M; m++){
                    phase += x.get(m, 0)*Math.pow((n+1), m);
                }
                spdiff[n] = Math.sin(2*Math.PI * phase - yphase[n]);
            }
            //compute gradients
            Matrix g = new Matrix(M, 1);
            for(int m = 0; m < M; m++){
                double grad = 0.0;
                for(int n = 0; n < N; n++){
                    grad += ymag[n] * Math.pow((n+1), m) * spdiff[n];
                }
                grad *= -4*Math.PI;
                g.set(m,0, grad);
            }
            return g;
        }
        
    }

}
