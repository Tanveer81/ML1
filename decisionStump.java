import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class decisionStump {
    int error = 0;
    int attr;
    featureVector[] resampled;
    ArrayList<Set<String>> set;
    ArrayList<Set<Double>> set2;
    int attributes = 10;
    double yes[][] = new double [attributes][2];
    double no[][] = new double [attributes][2];
    double stringYes[][] = new double [attributes][100];
    double stringNo[][] = new double [attributes][100];
    String result[];
    Double[] min,max;

    int bins = 0;
    double[] splitPoint = new double[10];


    public decisionStump(featureVector[] train, Double[] w, ArrayList<Set<String>> set , ArrayList<Set<Double>> set2, Double[] min, Double[] max) {
        this.min = min;
        this.max = max;
        this.set = set;
        this.set2 = set2;
        result = new String[train.length];
        resample(train, w);
        attr = minEntropyAttribute();
        if(attr < 10){
            //numeric
            double avg =  splitPoint[attr];
            for (int i = 0; i < train.length; i++) {
                if(train[i].f[attr].dbl > avg){
                    if(yes[attr][0] >= no[attr][0])result[i] = "yes";
                    else result[i] = "no";
                }
                else{
                    if(yes[attr][1] > no[attr][1])result[i] = "yes";
                    else result[i] = "no";
                }
            }
        }

        else{
            //string
            int b= 0;
            for(String a : set.get(attr-10)){
                for (int i = 0; i < train.length; i++) {
                    if(train[i].f[attr].string.equals(a)){
                        if(stringYes[attr-10][b] > stringNo[attr-10][b]){
                            result[i] = "yes";
                        }
                        else result[i] = "no";
                    }
                }
                b++;
            }
        }
        error = 0;
        for (int i = 0; i < train.length; i++) {
            if(!train[i].f[20].string.equals(result[i]))error++;
        }
    }

    double decision (featureVector fv){
        double r = 1.0;
        if(attr < 10){
            //numeric
            double avg =  splitPoint[attr];
                if(fv.f[attr].dbl > avg) {
                    if (yes[attr][0] > no[attr][0]) r = 1;
                    else r = -1;
                }
                else{
                    if(yes[attr][1] > no[attr][1])r = 1;
                    else r = -1;
                }
            }


        else{
            //string
            int b= 0;
            for(String a : set.get(attr-10)){
                    if(fv.f[attr].string.equals(a)){
                        if(stringYes[attr-10][b] > stringNo[attr-10][b]){
                            r = 1;
                        }
                        else r = -1;
                    }
                b++;
            }
        }
        return r;
    }

    int minEntropyAttribute() {
        double minEntropy = Double.MAX_VALUE;
        int minEntAttr = 0;
        for (int i = 0; i < 10; i++) {
            double currentEntropy = entropyNumerical(i);
            if (minEntropy > currentEntropy) {
                minEntropy = currentEntropy;
                minEntAttr = i;
            }
        }

        for (int i = 10; i < 20; i++) {
            double currentEntropy = entropyString(i);
            if (minEntropy > currentEntropy) {
                minEntropy = currentEntropy;
                minEntAttr = i;
            }
        }
        return minEntAttr;
    }

    double entropyNumerical(int attr){
        double minEntropy = Double.MAX_VALUE;
        int b= 0;
        bins = set2.get(attr).size();
        int iu = 0;
        Double unique[] = new Double[bins];
        Double splits[] = new Double[bins-1];

        for(Double avg : set2.get(attr)){
            unique[iu] = avg;
            iu++;
        }

        for (int j = 0; j < bins - 1; j++) {
            splits[j] =  (unique[j] + unique[j+1])/2;
        }

        for (int j = 0; j < bins - 1; j++){
            double entropy = 0;

            for (int i = 0; i < resampled.length; i++) {
                if(resampled[i].f[attr].dbl > splits[j]) {
                    if (resampled[i].f[20].string.equals("yes")) {yes[attr][0]++;}
                    else {no[attr][0]++;}
                }
                else{
                    if (resampled[i].f[20].string.equals("yes")) {yes[attr][1]++;}
                    else {no[attr][1]++;}
                }
            }

            if((yes[attr][0] + no[attr][0] > 0)){
                double yesRatio = yes[attr][0] / (yes[attr][0] + no[attr][0]);
                double noRatio = no[attr][0] / (yes[attr][0] + no[attr][0]);
                if(yes[attr][0] > 0)entropy -= yesRatio * log2(yesRatio);
                if(no[attr][0] > 0)entropy -= noRatio * log2(noRatio);
            }
            if((yes[attr][1] + no[attr][1] > 0)){
                double yesRatio = yes[attr][1] / (yes[attr][1] + no[attr][1]);
                double noRatio = no[attr][1] / (yes[attr][1] + no[attr][1]);
                if(yes[attr][1] > 0)entropy -= yesRatio * log2(yesRatio);
                if(no[attr][1] > 0)entropy -= noRatio * log2(noRatio);
            }

            double q = yes[attr][0] + no[attr][0] + yes[attr][1] + no[attr][1];
            if(q>0){
                entropy *=q;
                entropy /= (double)resampled.length;
            }

            if(entropy < minEntropy){
                minEntropy = entropy;
                splitPoint[attr] = splits[j];
            }
        }
        return minEntropy;
    }



    double entropyString(int attr){
        double entropy = 0;
        int b= 0;
        for(String a : set.get(attr-10)){
            for (int i = 0; i < resampled.length; i++) {
                if(resampled[i].f[attr].string.equals(a)){
                    if(resampled[i].f[20].string.equals("yes")){
                        stringYes[attr-10][b]++;
                    }
                    else {
                        stringNo[attr-10][b]++;
                    }
                }
            }
            b++;
        }
        b=0;
        for(String a : set.get(attr-10)) {
            double temp = 0;
            double q = 0;
            if (stringYes[attr - 10][b] + stringNo[attr - 10][b] > 0){
                double yesRatio = stringYes[attr - 10][b] / (stringYes[attr - 10][b] + stringNo[attr - 10][b]);
                double noRatio = stringNo[attr - 10][b] / (stringYes[attr - 10][b] + stringNo[attr - 10][b]);
                if(stringYes[attr - 10][b] > 0)temp -= yesRatio * log2(yesRatio);
                if(stringNo[attr - 10][b] > 0)temp -= noRatio * log2(noRatio);
                q += stringYes[attr - 10][b] + stringNo[attr - 10][b];
            }
            b++;
            if(q>0) {
                temp *= q;
                temp /= (double) resampled.length;
            }
            entropy += temp;
        }
        return entropy;
    }

    void resample(featureVector[] train , Double[] w){
        int numberOfTraining = train.length;
        Random rn = new Random();
        double[] nw = new double[numberOfTraining];
        nw[0]=w[0];
        for (int i = 1; i < numberOfTraining; i++)nw[i] = w[i] + nw[i-1];
        resampled = new featureVector[numberOfTraining];
        int sampleNo = 0;
        for (int j = 0; j < numberOfTraining; j++) {
            Double sample = rn.nextDouble();
            for (int i = 0; i < numberOfTraining; i++) {
                if (i == 0 && sample >= 0 && sample < nw[0]) {
                    sampleNo = i;
                    break;
                }
                else if (i!=0 && sample >= nw[i-1] && sample < nw[i]) {
                    sampleNo = i;
                    break;
                }
            }
            resampled[j] = train[sampleNo];
        }
    }

    double log2(double num)
    {
        return (Math.log(num)/Math.log(2));
    }
}


/*
double entropyNumerical(int attr){
        double minEntropy = Double.MAX_VALUE;
        int b= 0;
        bins = set2.size();

        for(Double avg : set2.get(attr)){
            double entropy = 0;
            //Arrays.fill(yes[attr],1);
            //Arrays.fill(no[attr],1);
            //double avg =  (max[attr]+min[attr])/2;
            int debug = 0;
            for (int i = 0; i < resampled.length; i++) {
                if(resampled[i].f[attr].dbl >= avg) {
                    if (resampled[i].f[20].string.equals("yes")) {yes[attr][0]++;debug++;}
                    else {no[attr][0]++;debug++;}
                }
                else{
                    if (resampled[i].f[20].string.equals("yes")) {yes[attr][1]++;debug++;}
                    else {no[attr][1]++;debug++;}
                }
            }

            if((yes[attr][0] + no[attr][0] == 0)){entropy = 0;}
            else {
                double yesRatio = yes[attr][0] / (yes[attr][0] + no[attr][0]);
                double noRatio = no[attr][0] / (yes[attr][0] + no[attr][0]);
                if(yes[attr][0] > 0)entropy -= yesRatio * log2(yesRatio);
                if(no[attr][0] > 0)entropy -= noRatio * log2(noRatio);
            }
            if((yes[attr][1] + no[attr][1] == 0)){entropy = Double.MAX_VALUE;}
            else {
                double yesRatio = yes[attr][1] / (yes[attr][1] + no[attr][1]);
                double noRatio = no[attr][1] / (yes[attr][1] + no[attr][1]);
                if(yes[attr][1] > 0)entropy -= yesRatio * log2(yesRatio);
                if(no[attr][1] > 0)entropy -= noRatio * log2(noRatio);
            }

            double q = yes[attr][0] + no[attr][0] + yes[attr][1] + no[attr][1];
            entropy *=q;
            entropy /= (double)resampled.length;

            //System.out.println("debug "+debug);
            if(entropy < minEntropy){ minEntropy = entropy;
            splitPoint[attr] = avg;}
        }

            //System.out.println("debug "+debug);
        return minEntropy;
    }
 */