package logic.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import model.Model;
import model.Paper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class JsonFileParser extends FileParser {
    public JsonFileParser(Model model) {
        super(model);
    }

    protected void parse(File file) {
        try {
            JSONParser parser = new JSONParser();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                // handle line breaks in between json data
                while (line.charAt(line.length()-1) != '}') {
                    line = line + reader.readLine();
                }
                //System.out.println(line);
                JSONObject object = (JSONObject) parser.parse(line);
                Paper paper = parsePaper(object);
                model.addPaper(paper);
                System.out.println(paper);

                parseInCitation(object, paper);
                parseOutCitation(object, paper);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Paper parsePaper(JSONObject object) {
        String id = object.get("id").toString();
        String title = object.get("title").toString();
        int date = 0;
        try {
            date = Integer.parseInt(object.get("year").toString());
        } catch (Exception e) {
            System.out.println("Date in invalid format: " + e.getMessage());
        }
        String venue = object.get("venue").toString();
        JSONArray authors = (JSONArray)object.get("authors");
        String[] authorNames = new String[authors.size()];
        for (int i = 0; i < authors.size(); i++) {
            JSONObject author = (JSONObject) authors.get(i);
            authorNames[i] = author.get("name").toString();
        }
        return new Paper(id, title, date, authorNames, venue);
    }

    private void parseInCitation(JSONObject object, Paper paper) {
        JSONArray inCitations = (JSONArray) object.get("inCitations");
        for (int i = 0; i < inCitations.size(); i++) {
            Paper citation = new Paper(inCitations.get(i).toString());
            model.addPaper(citation);
            model.addCitation(citation, paper); //citation cites paper
        }
    }

    private void parseOutCitation(JSONObject object, Paper paper) {
        JSONArray outCitations = (JSONArray) object.get("outCitations");
        for (int i = 0; i < outCitations.size(); i++) {
            Paper citation = new Paper(outCitations.get(i).toString());
            model.addPaper(citation);
            model.addCitation(paper, citation); //paper cites citation
        }
    }
}
