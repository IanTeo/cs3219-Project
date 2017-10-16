package logic.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import model.Author;
import model.Model;
import model.Paper;
import model.Paper.PaperBuilder;
import util.StringUtil;

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

    // TODO: This method will be refactored; this is tricky because of the bi-dependency between Paper & Author.
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
        JSONArray authorsJSON = (JSONArray)object.get("authors");
        Author[] authors = new Author[authorsJSON.size()];
        for (int i = 0; i < authorsJSON.size(); i++) {
            JSONObject author = (JSONObject) authorsJSON.get(i);
            String[] ids = (String[]) author.get("ids");
            String name = StringUtil.parseString(author.get("name").toString());
            authors[i] = (ids.length == 0) ? new Author(name) : new Author(ids[0], name);
            model.addAuthor(authors[i]);
        }

        Paper paper = new PaperBuilder().withId(id).withTitle(title).withYear(date).withAuthors(authors)
                .withVenue(venue).build();
        Arrays.stream(authors).forEach(author -> author.addPaper(paper));
        return paper;
    }

    private void parseInCitation(JSONObject object, Paper paper) {
        JSONArray inCitations = (JSONArray) object.get("inCitations");
        for (int i = 0; i < inCitations.size(); i++) {
            Paper citation = new PaperBuilder().withId(inCitations.get(i).toString()).build();
            model.addPaper(citation);
            model.addCitation(citation, paper); //citation cites paper
        }
    }

    private void parseOutCitation(JSONObject object, Paper paper) {
        JSONArray outCitations = (JSONArray) object.get("outCitations");
        for (int i = 0; i < outCitations.size(); i++) {
            Paper citation = new PaperBuilder().withId(outCitations.get(i).toString()).build();
            model.addPaper(citation);
            model.addCitation(paper, citation); //paper cites citation
        }
    }
}
