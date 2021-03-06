import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Main {
    public static class entryJSON {
        public int year;
        public int rank;
        public String company;
        public String revenue;
        public float profit;
        private static int oneRecord = 0;
        private static HashMap<String, Integer> CompanyEntryCount = new HashMap<String, Integer>();

        //ANSWER 4
        public static int uniqueCount(){
            return CompanyEntryCount.size();
        }

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
        //variables
        entryJSON[] jsonDataset = {};
        String[][] dataset;

        //ANSWER 1
        System.out.println("ANSWER 1:");
        dataset = readCSV("data.csv").stream().toArray(String[][]::new);
        System.out.println(dataset.length); //ANSWER 1

        //ANSWER 2
        System.out.println("ANSWER 2:");
        dataset = removeInvalidProfits(dataset).stream().toArray(String[][]::new);
        System.out.println(dataset.length);//ANSWER 2

        //ANSWER 3
        writeToJSON(dataset);
        dataset = printTop20(dataset).stream().toArray(String[][]::new);
        System.out.println("ANSWER 3:");
        Arrays.stream(dataset).forEach(row -> System.out.println(   row[0]+ ", " +row[1]+ ", " +row[2]+ ", " +row[3]+ ", " +row[4]    ));//ANSWER 3

        //ANSWER 4
        jsonDataset = readFromJSON("data2.json").stream().toArray(entryJSON[]::new);
        System.out.println("ANSWER 4:\n" + entryJSON.uniqueCount() + " unique companies"); //ANSWER 4

        //ANSWER 5
        HashMap<String, Integer> top10 = entryJSON.top10Repeats();
        System.out.println("ANSWER 5:");
        top10.keySet().forEach(key -> System.out.println(key + " has "  +  top10.get(key) + " entries") ); //ANSWER 5

        //ANSWER 6
        System.out.println("ANSWER 6:\n "+entryJSON.onlyOneReportCompany() + " companies with one record"); //ANSWER 6
    }


    //all json values are strings(it was my first iteration idea) so i just needed to parse the values to added it to the object constructor
    //the parser takes a string and converts it to a dictionary, therefore i just converted those strings to their respective values.
    //revenue had some string entries so i kept it a string because it wasn't asked to trim non-numeric revenue
    public static List<entryJSON> readFromJSON(String path) {
        JSONParser parser = new JSONParser();
        List<entryJSON> dataset = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            JSONObject json;
            String row = "";
            while ( (row = reader.readLine()) != null) {
                json = (JSONObject) parser.parse(row);
                dataset.add(new entryJSON(
                        Integer.parseInt((String)json.get("year")),
                        Integer.parseInt((String)json.get("rank")),
                        (String)json.get("company"),
                        (String)json.get("revenue"),
                        Float.parseFloat((String)json.get("profit"))  ));
            }
        }
        catch(Exception e){ e.printStackTrace(); }

        return dataset;
    }

    // takes teh strings of the dataset and converts them to json objects to write onto file via the json parser
    public static void writeToJSON(String[][] dataset){
        FileWriter file = null;
        try {
            file = new FileWriter("data2.json");
            for (String[] i : dataset) {
                JSONObject json = new JSONObject();
                json.put("year", i[0]);
                json.put("rank", i[1]);
                json.put("company", i[2]);
                json.put("revenue", i[3]);
                json.put("profit", i[4]);
                file.write(json.toJSONString());
                file.write("\n");
            }
            file.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }



    //the algorithm loops thru the dataset, and then from that loop it implements a pseudo-in-place sorting algorithm.
    // the algorithm populates the psuedo-queue till 20 entries. then it compares the profits between the entries in the queue vs the dataset.
    public static List<String[]> printTop20 (String[][] dataset){
        List<String[]> queue = new ArrayList<String[]>();
        boolean added = false;

        for(String[] i : dataset){
            float profit = Float.parseFloat(i[4]);
            if(queue.size() > 19){
                for (String[] j : queue){
                    float contrast = Float.parseFloat(j[4]);
                    if(profit > contrast && !added){ added = true; queue.set (queue.indexOf(j), i); }
                }
                added = false;
            }
            else{ queue.add(i); }
        }

        return queue;
    }

    //answer 2: loops thru the dataset and only adds the rows back if the matcher finds an entry that is some floating number string
    public static List<String[]> removeInvalidProfits(String[][] dataset){
        String pattern = "^-?\\d*\\.{0,1}\\d+$";
        List<String[]> ret = new ArrayList<String[]>();
        
        for(String[] i : dataset){
            Pattern pat = Pattern.compile(pattern);
            Matcher match = pat.matcher(i[4]);
            if(match.find()){ ret.add(i); }
        }

        return ret;
    }


    //uses a buffered reader to read line by line of the data.csv file, it excludes the first entry by eliminating the "year" value of the first entry
    public static List<String[]> readCSV(String path) {
        BufferedReader csvReader;
        String[] data = {};
        List<String[]> dataset = new ArrayList<String[]>();
        try {
            csvReader = new BufferedReader(new FileReader(path));
            String row = "";
                while ((row = csvReader.readLine()) != null) {
                    data = row.split(",");
                    
                    if(data[0] != "Year"){
                    dataset.add(data);
                    }
                }
                csvReader.close();
            } catch (IOException e1) { e1.printStackTrace(); }
        return dataset;
    }

}