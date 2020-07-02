import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 

public class Main {
    public static class entryCSV{
        public int year;
        public int rank;
        public String company;
        public int profit;

    }


    public static void main(String[] args) {
        String[][] dataset = readCSV("data.csv").stream().toArray(String[][]::new);
        System.out.println(dataset.length); //ANSWER 1
        dataset = removeInvalidProfits(dataset).stream().toArray(String[][]::new);;
        System.out.println(dataset.length);
        dataset = printTop20(dataset).stream().toArray(String[][]::new);
        Arrays.stream(dataset).forEach(row -> System.out.println(   row[0]+ ", " +row[1]+ ", " +row[2]+ ", " +row[3]+ ", " +row[4]    ));

    }


    public static List<String[]> printTop20 (String[][] dataset){
        //List<String[]> ret = new ArrayList<String[]>();
        List<String[]> queue = new ArrayList<String[]>();
        boolean added = false;

        for(String[] i : dataset){
            float profit = Float.parseFloat(i[4]);
            if(queue.size() > 19){
                for (String[] j : queue){
                    float contrast = Float.parseFloat(j[4]);

                    if(profit > contrast && !added){
                        added = true;
                        queue.set (queue.indexOf(j), i);
                    }
                }
                added = false;
            }
            else{
                queue.add(i);
            }
        }


        return queue;
    }

    //answer 2: loops thru the dataset and only adds the rows back if the matcher finds an entry that is some floating number
    public static List<String[]> removeInvalidProfits(String[][] dataset){
        String pattern = "^-?\\d*\\.{0,1}\\d+$";
        List<String[]> ret = new ArrayList<String[]>();
        
        for(String[] i : dataset){
            Pattern pat = Pattern.compile(pattern);
            Matcher match = pat.matcher(i[4]);
            if(match.find()){
                ret.add(i);
            }
        }

        return ret;
    }

    public static List<String[]> readCSV(String path) {
        BufferedReader csvReader;
        String[] data = {};
        List<String[]> dataset = new ArrayList<String[]>();
        try {
            csvReader = new BufferedReader(new FileReader(path));
            String row = "";
            try {
                while ((row = csvReader.readLine()) != null) {
                    
                    data = row.split(",");
                    
                    //removes the top row
                    if(data[0] != "Year"){
                    dataset.add(data);
                    }
                    // do something with the data
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                csvReader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return dataset;
        
    }

}