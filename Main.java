import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import com.opencsv.CSVReader;

public class Main {
    public static void main(String[] args) {
        CSVReader reader = null;
        featureVector fv[] = new featureVector[50000];
        ArrayList<Set<String>> attributes = new ArrayList<Set<String>>();
        for(int i = 0; i<11; i++){
            Set<String> a = new HashSet<String>();
            attributes.add(a);
        }

        ArrayList<Set<Double>> attributes2 = new ArrayList<Set<Double>>();
        for(int i = 0; i<11; i++){
            Set<Double> a = new TreeSet<Double>();
            attributes2.add(a);
        }

        Double min[] = new Double[10];
        Arrays.fill(min,Double.MAX_VALUE);
        Double max[] = new Double[10];
        Arrays.fill(max,Double.MIN_VALUE);

        int i = 0;
        try {
            reader = new CSVReader(new FileReader("E:\\L-4 T-2\\ML Sessional\\Offline1\\src\\bank-additional-full.csv"), ';');//
            String[] line;
            line = reader.readNext();//reading first line description
            for (int k = 0; k < 21; k++) System.out.print(line[k] + " ");
            System.out.println();

            while ((line = reader.readNext()) != null) {
                featureVector f = new featureVector();

                f.f[0].dbl = Double.parseDouble(line[0]);
                f.f[1].dbl = Double.parseDouble(line[11]);
                f.f[2].dbl = Double.parseDouble(line[16]);
                f.f[3].dbl = Double.parseDouble(line[17]);
                f.f[4].dbl = Double.parseDouble(line[10]);
                f.f[5].dbl = Double.parseDouble(line[15]);
                f.f[6].dbl = Double.parseDouble(line[18]);
                f.f[7].dbl = Double.parseDouble(line[19]);
                f.f[8].dbl = Double.parseDouble(line[12]);
                f.f[9].dbl = Double.parseDouble(line[13]);

                f.f[10].string = line[7];
                f.f[11].string = line[9];
                f.f[12].string = line[4];
                f.f[13].string = line[3];
                f.f[14].string = line[5];
                f.f[15].string = line[1];
                f.f[16].string = line[6];
                f.f[17].string = line[2];
                f.f[18].string = line[8];
                f.f[19].string = line[14];
                f.f[20].string = line[20];

                fv[i] = f;

                /*System.out.print(i+1+ "->");
                for (int y = 0; y < 10; y++)System.out.print(f.f[y].dbl + " ");
                for (int y = 10; y < 21; y++)System.out.print(f.f[y].string + " ");
                System.out.println();*/

                //sets
                for(int y = 0; y < 10 ; y++){
                    if(min[y] > f.f[y].dbl)min[y]=f.f[y].dbl;
                    if(max[y] < f.f[y].dbl)max[y]=f.f[y].dbl;
                    attributes2.get(y).add(f.f[y].dbl);
                }
                for(int y = 10; y < 21 ; y++){
                    attributes.get(y-10).add(f.f[y].string);
                }



                i++;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        /*for(Set<Double> a : attributes2){
            System.out.println(a);
        }*/

        //System.out.println(job);//debug
        System.out.println(i);//debug
        featureVector[] dataset = new featureVector[9280];
        int yes = 0;
        int no = 0;
        String q = new String("yes");
        String w = new String("no");
        for(int t=0;t<i;t++){ //adding all yes class
            if(fv[t].f[20].string.equals(q)) {
                dataset[2 * yes] = fv[t];
                yes++;
            }
        }
        System.out.println(yes);//debug
        //System.out.println(dataset[9278].f[20].string);//debug y

        //List<Integer> array = new ArrayList< >();
        Random rn = new Random();
        while(no<yes){ //adding same number of no class randomly
            int  n = rn.nextInt(i);
            //if(!array.contains(n)) {
                //array.add(n);
                if (fv[n].f[20].string.equals(w)) { //y
                    dataset[2 * no + 1] = fv[n];
                    no++;
                //}
            }
        }

        /*for(int t=0;t<yes*2;t++){
            System.out.print(t+1+"-> ");
            for(int p = 0; p<10 ; p++)System.out.print(dataset[t].f[p].dbl+" ");
            for(int p = 10; p<21 ; p++)System.out.print(dataset[t].f[p].string+" ");
            System.out.println();
        }*/

        //calling kfoldcrossvalidation
        kfoldCrossValidation kf = new kfoldCrossValidation(dataset,attributes,attributes2,min,max,5);

    }
}




//numeric
                /*f.age  = Double.parseDouble(line[0]);
                f.campaign  = Double.parseDouble(line[11]);
                f.consPriceIndex  = Double.parseDouble(line[16]);
                f.consConIndex  = Double.parseDouble(line[17]);
                f.duration  = Double.parseDouble(line[10]);
                f.empVarRate  = Double.parseDouble(line[15]);
                f.euribor3m  = Double.parseDouble(line[18]);
                f.nrEmployees  = Double.parseDouble(line[19]);
                f.pdays  = Double.parseDouble(line[12]);
                f.previous  = Double.parseDouble(line[13]);*/


//strings
                /*f.contact = line[7];
                f.dayOfWeek = line[9];
                f.dfault = line[4];
                f.education = line[3];
                f.housing = line[5];
                f.job = line[1];
                f.loan = line[6];
                f.marital = line[2];
                f.month = line[8];
                f.pOutCome = line[14];
                f.y = line[20];*/






/*
"age";"job";"marital";"education";"default";"housing";"loan";"contact";"month";"day_of_week";"duration";"campaign";"pdays";
"previous";"poutcome";"emp.var.rate";"cons.price.idx";"cons.conf.idx";"euribor3m";"nr.employed";"y"
 */

/*
1.6;Pankaj Kumar;20;India
        2;David Dan;40.9;USA
        3;Lisa Ray;28;Germany



56;"housemaid";"married";"basic.4y";"no";"no";"no";"telephone";"may";"mon";261;1;999;0;"nonexistent";1.1;93.994;-36.4;4.857;5191;"no"
57;"services";"married";"high.school";"unknown";"no";"no";"telephone";"may";"mon";149;1;999;0;"nonexistent";1.1;93.994;-36.4;4.857;5191;"no"
        */