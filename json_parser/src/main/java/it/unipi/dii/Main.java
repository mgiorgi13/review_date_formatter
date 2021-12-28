package it.unipi.dii;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("unchecked")
public class Main {

    public static int count = 0;
    public static int writeCount = 0;
    public static String target = "user";
    public static String newFileName = "New";

    public static void main(String[] args) {
        JSONArray revList = new JSONArray();

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        System.out.println("Aspetta che apro il file...");

        try {
            FileWriter file = new FileWriter(target+newFileName+".json",true);
            //We can write any JSONArray or JSONObject instance to the file
            file.write("[");
            file.close();

            FileReader reader = new FileReader(target+".json");
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray reviewList = (JSONArray) obj;

            //Iterate over employee array
            reviewList.forEach( rev -> parseReviewObject( (JSONObject) rev ) );

            file = new FileWriter(target+newFileName+".json",true);
            file.write("]");
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


     private static void parseReviewObject(JSONObject review)
    {
        count++;
        writeCount++;

//        String user_id = (String) review.get("user_id");
//        String book_id = (String) review.get("book_id");
//        Long rating = (Long) review.get("rating");
//        String review_text = (String) review.get("review_text");
//        Long n_votes = (Long) review.get("n_votes");
        String review_id = (String) review.get("review_id");
        String date_added = (String) review.get("date_added");
        String date_updated = (String) review.get("date_updated");



        JSONObject result = new JSONObject();

//        result.put("user_id",user_id);
//        result.put("book_id",book_id);
//        result.put("rating",rating);
//        result.put("review_text",review_text);
//        result.put("n_votes",n_votes);
        result.put("review_id",review_id);

        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                Locale.US);
        Date date_added_parsed = null;
        Date date_updated_parsed = null;
        try {
            date_added_parsed = sdf.parse(date_added);
            date_updated_parsed = sdf.parse(date_updated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat print = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        result.put("date_added",print.format(date_added_parsed));
        result.put("date_updated",print.format(date_updated_parsed));

        appendJson(result,target);

        if(writeCount >= 100000){
            System.out.println(count);
            writeCount = 0;
        }
    }

    private static void appendJson(JSONObject rev,String f){
        try (FileWriter file = new FileWriter(f+newFileName+".json",true)) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(rev.toJSONString());
            file.write(",");
            //todo inserisce la "," anche se è l'ultimo oggetto

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
