package Utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Petter Månsson 2017-11-08
 * Läser in text från fil för att skapa en sträng som går att söka på.
 */

public class FileReader {

    public static String readFile(String fnam) {
        StringBuilder string = new StringBuilder();
        int wordcount = 0;

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fnam)));

            while (true) {
                String word = r.readLine();
                if (word == null) {
                    break;
                }
                if (wordcount == 0) {
                    string.append(word);
                    wordcount++;
                } else {
                    string.append("\n" + word);
                }
            }
        }catch(Exception e){

        }



        return string.toString();
    }

    public ArrayList<String> readFiletoArrayList(String fnam) {
        ArrayList<String> strings = new ArrayList<>();

        try {
            List<String> productLines = Files.readAllLines(java.nio.file.Paths.get(fnam), StandardCharsets.UTF_8);

            for (String line : productLines) {
                String[] tokens = line.split(" ");
                for(String words : tokens) {
                    strings.add(words);
                }
            }


        }catch(Exception e){

        }
        return strings;
    }

    public StringBuilder readFileU5Text(String fnam){
        StringBuilder strings = new StringBuilder();

        try {
            List<String> productLines = Files.readAllLines(java.nio.file.Paths.get(fnam), StandardCharsets.UTF_8);
            for (String line : productLines) {
                strings.append(line);
            }


        }catch(Exception e){

        }
        return strings;
    }
}
