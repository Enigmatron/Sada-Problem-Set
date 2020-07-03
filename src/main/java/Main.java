import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
    public static class entryJSON{
        public int year;
        public int rank;
        public String company;
        public int revenue;
        public int profit;

        private static Dictionary<String, Integer> CompanyEntryCount;


        //ANSWER 5
        public static String[] top10Repeats(){
            String[] ret = new String[10];



            return ret;
        }

        //ANSWER 4
        public static int uniqueCount(){
            return CompanyEntryCount.size();
        }

        //ANSWER 6
        public static int onlyOneReportCompany(){
            int ret = 0;
            while(CompanyEntryCount.elements().hasMoreElements()){
                if(CompanyEntryCount.elements().nextElement() == 1){
                    ret++;
                }
            }
            return ret;
        }



        public entryJSON (int y, int ra, String co, int re, int pr){
            year = y;
            rank = ra;
            company = co;
            revenue = re;
            profit = pr;
            if(CompanyEntryCount.get(company) != null ){
                CompanyEntryCount.put(company, CompanyEntryCount.get(company)+1);
            }
            else{
                CompanyEntryCount.put(company, 1);
            }
        }


    }


    public static void main(String[] args) {

        String[][] dataset = readCSV("data.csv").stream().toArray(String[][]::new);
        System.out.println(dataset.length); //ANSWER 1


        dataset = removeInvalidProfits(dataset).stream().toArray(String[][]::new);;
        System.out.println(dataset.length);//ANSWER 2


        writeToJSON(dataset);
        dataset = printTop20(dataset).stream().toArray(String[][]::new);
        Arrays.stream(dataset).forEach(row -> System.out.println(   row[0]+ ", " +row[1]+ ", " +row[2]+ ", " +row[3]+ ", " +row[4]    ));//ANSWER 3



    }

    public static entryJSON[][] writeToJSON(String path) {
        JSONParser parser = new JSONParser();
        List<entryJSON[]> dataset = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            JSONObject json = (JSONObject) parser.parse(reader);

            while ( parser.hasNext()) {

                data = row.split(",");

                //removes the top row
                if(data[0] != "Year"){
                    dataset.add(data);
                }
                // do something with the data
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }



    }


    public static void writeToJSON(String[][] dataset){
        FileWriter file = null;
        try {
            file = new FileWriter("data2.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String[] i :dataset) {
            JSONObject json = new JSONObject();
            json.put("year", i[0]);
            json.put("rank", i[1]);
            json.put("company", i[2]);
            json.put("revenue", i[3]);
            json.put("profit", i[4]);
            try {
                //FileWriter file = new FileWriter("data2.json");
                file.write(json.toJSONString());
                file.write("\n");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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