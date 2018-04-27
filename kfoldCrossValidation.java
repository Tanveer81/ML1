import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Set;

import static java.lang.Double.NaN;

public class kfoldCrossValidation {
    featureVector[] dataset;
    ArrayList<Set<String>> attributes;
    ArrayList<Set<Double>> attributes2;
    Double[] min,max;
    int folds;
    int boost;
    double avgF1=0;
    String fileName = "E:\\L-4 T-2\\ML Sessional\\Offline1\\src\\result.txt";
    String fileName2 = "E:\\L-4 T-2\\ML Sessional\\Offline1\\src\\avgResult.txt";


    public kfoldCrossValidation(featureVector[] ds, ArrayList<Set<String>> attr, ArrayList<Set<Double>> attr2, Double[] min, Double[] max,int folds,int boost) {
        this.dataset = ds;
        this.attributes = attr;
        this.attributes2 = attr2;
        this.folds = folds;
        this.min = min;
        this.max = max;
        this.boost = boost;
        cross(folds);
    }

    void cross(int folds) {
        System.out.println("Boost : "+boost);
        int totalData = dataset.length;
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
            }

        adaBoost ab = new adaBoost(train, attributes , attributes2 , min , max , boost);

        double[] r = new double[(int) (testRatio*totalData)];
        for (int it = 0; it < testRatio*totalData; it++) {
            double c = ab.decision(test[it]);
            r[it] = c;
        }

        double error = 0.0;
        double tp = 0, fp =0, fn = 0;

        for (int ii = 0; ii < testRatio*totalData; ii++) {
            if(r[ii] > 0 && test[ii].f[20].string.equals("yes")){
                tp++;
            }
            else if(r[ii] <= 0 && test[ii].f[20].string.equals("yes")){
                fp++;
                error++;
            }
            else if(r[ii] > 0 && test[ii].f[20].string.equals("no")){
                fn++;
                error++;
            }
        }
        double f1 = 0;
        double precision = tp/(tp+fp);
        double recall = tp/(tp+fn);
        if((tp+fp) == 0 && (tp+fn) == 0){f1 = 1;}
        else if((tp+fp) == 0){f1 = 2*recall;}
        else if((tp+fn) == 0){f1 = 2*precision;}
        else f1 = 2/((1/precision)+(1/recall));
        avgF1 += f1/folds;
            try {
                appendToFile2(fileName,folds+"        "+boost+"        "+tp+"        "+fp+"        " +
                        fn+"        "+precision+"        "+recall+"        "+f1+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

//            System.out.println("Error : "+errorRatio);
//            System.out.println("tp : "+tp);
//            System.out.println("fp : "+fp);
//            System.out.println("fn : "+fn);
//            System.out.println("precision : "+precision);
//            System.out.println("recall : "+recall);
//            System.out.println("f1 : "+f1);

        }
        try {
            appendToFile2(fileName,"\n");
            appendToFile2(fileName2,"\nFolds : "+folds+"  Boost : "+boost+"  Avg F1 Score : "+ avgF1+"\n");
        }catch (IOException e){}
    }



    public void appendToFile2(String FileName,String str) throws IOException {
        Writer output;
        output = new BufferedWriter(new FileWriter(FileName,true));
        output.append(str);
        output.close();
    }

}

//folds  boost       tp            fp          fn            precision           recall            f1
