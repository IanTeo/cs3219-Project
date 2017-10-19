package logic.command;

import model.Author;
import model.Model;
import model.Paper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.StringUtil;

import java.util.*;

public class TopCommand implements Command{
    public static final String COMMAND_WORD = "top";
    private Model model;
    private int count;
    private String type;
    private String venue;

    public String execute() {
        switch (type) {
            case "author" :
                return getTopAuthorIds();

            case "paper" :
                return getTopPaperIds();

            default :
                return "Invalid type";
        }
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        String[] args = arguments.split(" ");
        this.count = Integer.parseInt(args[0]);
        this.type = StringUtil.parseString(args[1]);
        this.venue = StringUtil.parseString(args[2]);
    }

    private String getTopAuthorIds() {
        if (count == 0) return "";
        List<Author> authors = new ArrayList<>(model.getAuthors());
        authors.sort((Author a1, Author a2) -> {
            int countA1 = a1.getPaperCountByVenue(venue);
            int countA2 = a2.getPaperCountByVenue(venue);
            return countA1 == countA2 ? a2.getName().compareTo(a1.getName()) : countA2 - countA1;
        });

        JSONArray array = new JSONArray();
        for (int i = 0; i < count; i++) {
            JSONObject object = new JSONObject();
            Author author = authors.get(i);
            object.put("id", author.getId());
            object.put("name", author.getName());
            object.put("paperCount", author.getPaperCountByVenue(venue));
            // TODO: need to add actual array of papers
            array.add(object);
        }
        return array.toString();
    }

    private String getTopPaperIds() {
        if (count == 0) return "";


        List<Paper> filteredPapers = new ArrayList<>();
        for (Paper p : model.getPapers()) {
            if (p.getVenue().contains(venue)) {
                filteredPapers.add(p);
            }
        }
        filteredPapers.sort(Comparator.comparingInt(Paper::getInCitationCount).reversed());

        JSONArray array = new JSONArray();
        for (int i = 0; i < count; i++) {
            JSONObject object = new JSONObject();
            Paper paper = filteredPapers.get(i);
            object.put("id", paper.getId());
            object.put("title", paper.getTitle());
            object.put("year", paper.getYear());
            object.put("citationCount", paper.getInCitationCount());
            StringBuilder authorBuilder = new StringBuilder();
            for (Author author : paper.getAuthors()) {
                authorBuilder.append(author.getName()).append(", ");
            }
            authorBuilder.delete(authorBuilder.length() - 2, authorBuilder.length());
            object.put("authors", authorBuilder.toString());
            array.add(object);
        }
        return array.toString();
    }
}
