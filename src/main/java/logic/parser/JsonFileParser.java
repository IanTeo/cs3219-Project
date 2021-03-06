package logic.parser;

import java.io.*;
import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import model.Model;
import model.Author;
import model.Paper;
import util.StringUtil;

public class JsonFileParser extends FileParser {
    public JsonFileParser(Model model) {
        super(model);
    }

    protected void parse(File file) {
        try {
            JSONParser parser = new JSONParser();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder builder = new StringBuilder();
                builder.append(line);
                // handle line breaks in between json data
                while (builder.charAt(builder.length()-1) != '}') {
                    builder.append(reader.readLine());
                }

                JSONObject object = (JSONObject) parser.parse(builder.toString());
                Paper paper = parsePaper(object);
                parseInCitation(object, paper);
                parseOutCitation(object, paper);

                addAuthor(paper.getAuthors(), paper);
                model.addPaper(paper);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses {@code object} into a {@code Paper}.
     */
    private Paper parsePaper(JSONObject object) {
        String id = StringUtil.sanitise(object.get("id").toString());
        String title = StringUtil.sanitise(object.get("title").toString());
        int year;
        try {
            year = Integer.parseInt(object.get("year").toString());
        } catch (Exception e) {
            year = 0;
        }
        String venue = StringUtil.sanitise(object.get("venue").toString());
        Author[] authors = parseAuthors(object);
        processAuthors(authors);

        return new Paper(id, title, year, authors, venue);
    }

    /**
     * Parses {@code object} into {@code Author}s.
     */
    private Author[] parseAuthors(JSONObject object) {
        JSONArray authorsJSON = (JSONArray) object.get("authors");
        Author[] authors = new Author[authorsJSON.size()];
        for (int i = 0; i < authorsJSON.size(); i++) {
            JSONObject authorJSON = (JSONObject) authorsJSON.get(i);
            JSONArray ids = (JSONArray) authorJSON.get("ids");
            String name = StringUtil.sanitise(authorJSON.get("name").toString());
            authors[i] = (ids.size() == 0) ? new Author(name) : new Author(ids.get(0).toString(), name);
        }
        return authors;
    }

    /**
     * Replaces each {@code Author} in authors with the copy in the model, if it already exists in the model.
     */
    private void processAuthors(Author[] authors) {
        for (int i = 0; i < authors.length; i++) {
            String authorUID = authors[i].getId();
            if (model.hasAuthor(authorUID)) {
                authors[i] = model.getAuthor(authorUID);
            }
        }
    }

    /**
     * Appends {@code paper} to the list of papers written by each of the authors in {@code authorsArray}.
     * Then add each author into {@code model}, if the author doesn't exist in the list of authors stored in
     * {@code model}. Otherwise, merges the list of papers written by the author in {@code model} and the current
     * author.
     */
    private void addAuthor(Collection<Author> authorsArray, Paper paper) {
        for (Author author : authorsArray) {
            String uniqueIdentifier = author.getId();
            if (model.hasAuthor(uniqueIdentifier)) {
                Author toModify = model.getAuthor(uniqueIdentifier);
                toModify.addPaper(paper);
            } else {
                author.addPaper(paper);
                model.addAuthor(author);
            }
        }
    }

    private void parseInCitation(JSONObject object, Paper paper) {
        JSONArray inCitations = (JSONArray) object.get("inCitations");
        for (int i = 0; i < inCitations.size(); i++) {
            String citationId = inCitations.get(i).toString();
            if (model.hasPaper(citationId)) {
                Paper citation = model.getPaper(citationId);
                citation.addCitation(paper);
            }
        }
    }

    private void parseOutCitation(JSONObject object, Paper paper) {
        JSONArray outCitations = (JSONArray) object.get("outCitations");
        for (int i = 0; i < outCitations.size(); i++) {
            String citationId = outCitations.get(i).toString();
            if (model.hasPaper(citationId)) {
                Paper citation = model.getPaper(citationId);
                paper.addCitation(citation);
            }
        }
    }
}
