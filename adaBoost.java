import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class adaBoost {
    int boost;
    decisionStump[] h;
    double[] z;

    public adaBoost(featureVector[] train , ArrayList<Set<String>> attr , ArrayList<Set<Double>> attr2 , Double[] min, Double[] max, int boost){
        this.boost = boost;
        int numberOftraining = train.length;
        Double[] w = new Double[numberOftraining];
        Arrays.fill(w,1/Double.valueOf(numberOftraining));

        h = new decisionStump[boost];
        z = new double[boost];
        int k = 0;
        int r = 0;
        while( k < boost) {
            decisionStump ds = new decisionStump(train, w, attr, attr2, min, max);
            r++;
            System.out.print(r +" ->");
            Double ratio = Double.valueOf(ds.error) / Double.valueOf(numberOftraining);
            System.out.println(ds.attr);
            System.out.println(ratio);
            if (ratio < .5) {
                h[k] = ds;
                double error = 0.0;
                for (int i = 0; i < numberOftraining; i++) {
                    if (!ds.result[i].equals(train[i].f[20].string)) {
                        error += w[i];
                    }
                }

                for (int i = 0; i < numberOftraining; i++) {
                    if (ds.result[i].equals(train[i].f[20].string)) {
                        w[i] = w[i] * (error / (1 - error));
                    }
                }

                double tw = 0;
                for (int i = 0; i < numberOftraining; i++) {
                    tw += w[i];
                }

                // System.out.println(tw);

                for (int i = 0; i < numberOftraining; i++) {
                    w[i] = w[i] / tw;
                }

                //if(error == 0){ z[k] = Double.valueOf(1);}
                double b = (1 - error) / error;
                double a = log2(b);
                z[k]=a;
                //System.out.println(z[k]);
                k++;
            }
        }
    }

    double decision(featureVector fv){
        double r = 1.0;
        for (int i = 0; i < boost; i++) {
            double c = h[i].decision(fv);
            r *= z[i]*c;
        }
        return r;
    }

    double log2(double num)
    {
        double b = (Math.log(num)/Math.log(2));
        return b;
    }
}


/*
 for(Set<String> a : attributes){
            System.out.println(a);
        }
        for(int i = 0; i<10;i++){
            System.out.print(min[i]+",");
            System.out.println(max[i]);
        }
 */