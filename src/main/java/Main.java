import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;

public class Main {
    public static class entryJSON {
        public int year;
        public int rank;
        public String company;
        public String revenue;
        public float profit;
        private static int oneRecord = 0;


        private static HashMap<String, Integer> CompanyEntryCount = new HashMap<String, Integer>();

        //ANSWER 5
        public static HashMap<String, Integer> top10Repeats(){
            HashMap<String, Integer> top10 = new HashMap<String, Integer>();
            boolean added = false;

            for (Object key : CompanyEntryCount.keySet()){
                if(top10.size() > 9){
                    for (Object j : top10.keySet()){
                        int contrast = top10.get(j);

                        if(CompanyEntryCount.get(key) > contrast && !added){
                            added = true;
                            top10.remove (key);
                            top10.put((String)j, CompanyEntryCount.get(key));
                        }
                    }
                    added = false;
                }
                else{
                    top10.put((String)key, CompanyEntryCount.get(key) );
                }
            }



            return top10;
        }

        //ANSWER 4
        public static int uniqueCount(){
            return CompanyEntryCount.size();
        }


        //ANSWER 6
        public static int onlyOneReportCompany(){
            CompanyEntryCount.keySet().forEach(key ->  ifEQOne(key));

            return oneRecord;
        }

        public static void ifEQOne(Object key){
                if(CompanyEntryCount.get(key) == 1){
                    oneRecord+=1;
                }
        }



        public entryJSON (int y, int ra, String co, String re, float pr){
            year = y;
            rank = ra;
            company = co;
            revenue = re;
            profit = pr;
            CompanyEntryCount.merge(company, 1, Integer::sum);
        }

    }


    public static void main(String[] args) {
        entryJSON[] jsonDataset = {};

        System.out.println("ANSWER 1:");
        String[][] dataset = readCSV("data.csv").stream().toArray(String[][]::new);
        System.out.println(dataset.length); //ANSWER 1

        System.out.println("ANSWER 2:");
        dataset = removeInvalidProfits(dataset).stream().toArray(String[][]::new);
        System.out.println(dataset.length);//ANSWER 2


        writeToJSON(dataset);
        dataset = printTop20(dataset).stream().toArray(String[][]::new);
        System.out.println("ANSWER 3:");
        Arrays.stream(dataset).forEach(row -> System.out.println(   row[0]+ ", " +row[1]+ ", " +row[2]+ ", " +row[3]+ ", " +row[4]    ));//ANSWER 3


        jsonDataset = readFromJSON("data2.json").stream().toArray(entryJSON[]::new);

        System.out.println("ANSWER 4:\n" + entryJSON.uniqueCount() + " unique companies"); //ANSWER 4

        HashMap<String, Integer> top10 = entryJSON.top10Repeats();
        System.out.println("ANSWER 5:");
        top10.keySet().forEach(key -> System.out.println((String)key + " has "  +  top10.get(key) + " entries") ); //ANSWER 5

        System.out.println("ANSWER 6:\n "+entryJSON.onlyOneReportCompany() + " companies with one record"); //ANSWER 6
    }

    public static List<entryJSON> readFromJSON(String path) {
        JSONParser parser = new JSONParser();
        List<entryJSON> dataset = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            JSONObject json;
            String row = "";
            while ( (row = reader.readLine()) != null) {

                json = (JSONObject) parser.parse(row);
                dataset.add(new entryJSON( Integer.parseInt((String)json.get("year")),
                        Integer.parseInt((String)json.get("rank")), (String)json.get("company"),
                        (String)json.get("revenue"),
                        Float.parseFloat((String)json.get("profit"))  ));


            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


        return dataset;
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
                    
                    if(data[0] != "Year"){
                    dataset.add(data);
                    }
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