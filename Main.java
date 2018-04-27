import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import com.opencsv.CSVReader;

public class Main {
    public static void main(String[] args) {
        CSVReader reader = null;
        featureVector fv[] = new featureVector[41188];
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
            //for (int k = 0; k < 21; k++) System.out.print(line[k] + " ");
            //System.out.println();

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

        //for(Set<Double> a : attributes2)System.out.println(a);


        //System.out.println(job);//debug
        System.out.println("Total Data : "+i);//debug
        featureVector[] dataset = new featureVector[9280];
        featureVector[] yesSet = new featureVector[4640];
        featureVector[] noSet = new featureVector[4640];

        int yes = 0;
        int no = 0;
        String q = new String("yes");
        String w = new String("no");
        for(int t=0;t<i;t++){ //adding all yes class
            if(fv[t].f[20].string.equals(q)) {
                yesSet[yes] = fv[t];
                yes++;
            }
        }
        for(int t=0;t<i;t++){ //adding all no class
            if(fv[t].f[20].string.equals(w)) {
                noSet[no] = fv[t];
                no++;
                if(no == 4640){break;}
            }
        }
/*        List<Integer> array = new ArrayList< >();
        Random rn = new Random();
        while(no<yes){ //adding same number of no class randomly
            int  n = rn.nextInt(i);
            if(!array.contains(n)) {
                array.add(n);
                if (fv[n].f[20].string.equals(w)) { //no
                    noSet[no] = fv[n];
                    no++;
                }
            }
        }*/

        for(int t=0;t<4640;t++){ //adding all class
            dataset[2*t] = yesSet[t];
            dataset[2*t+1] = noSet[t];
        }
       Collections.shuffle(Arrays.asList(dataset));

        System.out.println("Number of yes : "+yes);//debug
        //System.out.println(dataset[9278].f[20].string);//debug y

/*        List<Integer> array = new ArrayList< >();
        Random rn = new Random();
        while(no<yes){ //adding same number of no class randomly
            int  n = rn.nextInt(i);
            if(!array.contains(n)) {
                array.add(n);
                if (fv[n].f[20].string.equals(w)) { //no
                    dataset[2 * no + 1] = fv[n];
                    no++;
                }
            }
        }*/

        System.out.println("Number of no : "+no);//debug

        /*for(int t=0;t<yes*2;t++){
            System.out.print(t+1+"-> ");
            for(int p = 0; p<10 ; p++)System.out.print(dataset[t].f[p].dbl+" ");
            for(int p = 10; p<21 ; p++)System.out.print(dataset[t].f[p].string+" ");
            System.out.println();
        }*/

        //calling kfoldcrossvalidation
        int folds = 5;
        int boost = 5;
        System.out.println("Number Of Folds : "+folds);
        try{
        Writer output;
        String fileName = "E:\\L-4 T-2\\ML Sessional\\Offline1\\src\\result.txt";
        output = new BufferedWriter(new FileWriter(fileName,true));
        output.append("\n");
        output.close();}catch(Exception e){}


        new kfoldCrossValidation(dataset,attributes,attributes2,min,max,5,1);
        new kfoldCrossValidation(dataset,attributes,attributes2,min,max,5,5);
        new kfoldCrossValidation(dataset,attributes,attributes2,min,max,5,10);
        new kfoldCrossValidation(dataset,attributes,attributes2,min,max,5,20);
        new kfoldCrossValidation(dataset,attributes,attributes2,min,max,5,30);

        new kfoldCrossValidation(dataset,attributes,attributes2,min,max,10,5);
       new kfoldCrossValidation(dataset,attributes,attributes2,min,max,20,5);






    }

}
