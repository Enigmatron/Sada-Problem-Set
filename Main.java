import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

    }

    public String[] readCSV(String path) {
        BufferedReader csvReader;
        String[] data = {};
        try {
            csvReader = new BufferedReader(new FileReader(path));
            String row = "";
            try {
                while ((row = csvReader.readLine()) != null) {
                    data = row.split(",");
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
        return data;
        
    }

}