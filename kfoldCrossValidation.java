import java.util.ArrayList;
import java.util.Set;

public class kfoldCrossValidation {
    featureVector[] dataset;
    ArrayList<Set<String>> attributes;
    ArrayList<Set<Double>> attributes2;
    Double[] min,max;
    int folds;

    public kfoldCrossValidation(featureVector[] ds, ArrayList<Set<String>> attr, ArrayList<Set<Double>> attr2, Double[] min, Double[] max,int folds) {
        this.dataset = ds;
        this.attributes = attr;
        this.attributes2 = attr2;
        this.folds = folds;
        this.min = min;
        this.max = max;
        cross();
    }

    void cross() {
        int totalData = dataset.length;
        System.out.println(totalData);
        Double trainRatio = (Double.valueOf(folds)-1)/Double.valueOf(folds);
        Double testRatio = 1/Double.valueOf(folds);

        for (int i = 0; i < folds; i++) {
            featureVector[] train = new featureVector[(int) (trainRatio*totalData)];
            featureVector[] test = new featureVector[(int) (testRatio*totalData)];
            int l = 0;
            int p = 0;
            for (int j = 0; j < folds; j++) {
                if (j != i){
                    for (int k = j*totalData/folds; k < (j+1)*totalData/folds; k++) {
                        train[l] = dataset[k];
                        l++;
                    }
                } else {
                    for (int k = j*totalData/folds; k < (j+1)*totalData/folds; k++) {
                        test[p] =dataset[k];
                        p++;
                    }
                }
                //adaBoost ab = new adaBoost(train, attributes , min , max , 10);
            }

        //test
        adaBoost ab = new adaBoost(train, attributes , attributes2 , min , max , 30);
        double[] r = new double[(int) (testRatio*totalData)];
        for (int it = 0; it < testRatio*totalData; it++) {
            double c = ab.decision(test[it]);
            r[it] = c;
        }

        double error = 0.0;

        for (int ii = 0; ii < testRatio*totalData; ii++) {
            //System.out.println(r[ii]);
            //System.out.println(test[ii].f[20].string);
            if(r[ii] < 0 && test[ii].f[20].string.equals("yes"))error++;
            else if(r[ii] > 0 && test[ii].f[20].string.equals("no"))error++;
        }

        double w = (testRatio*totalData);
        double ratio = error/w;
        System.out.println(error);
        System.out.println(w);
        System.out.println("Final Result "+ratio);
        }
    }

}
