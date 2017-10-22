package logic.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.Author;
import model.Model;
import model.Paper;
import util.StringUtil;

public class TopCommand implements Command{
    public static final String COMMAND_WORD = "top";
    public static final String HELP = "Error: %s\nUsage: top [#] [author/paper] [venue]\n" +
            "This command returns a JSON file representing the top # of authors/papers for the specified venue";
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
                return String.format(HELP, "Invalid type");
        }
    }

    public void setParameters(Model model, String arguments) throws Exception {
        try {
            this.model = model;
            String[] args = arguments.split(" ");
            this.count = Integer.parseInt(args[0]);
            this.type = args[1];
            this.venue = args[2];
        } catch (Exception e) {
            throw new Exception(String.format(HELP, "Error parsing parameters"));
        }
    }

    private String getTopAuthorIds() {
        if (count == 0) return "";
        List<Author> authors = new ArrayList<>(model.getAuthors());
        authors.sort((Author a1, Author a2) -> {
            int countA1 = getPaperCount(a1);
            int countA2 = getPaperCount(a2);
            return countA1 == countA2 ? a1.getName().compareTo(a2.getName()) : countA2 - countA1;
        });

        JSONArray array = new JSONArray();
        for (int i = 0; i < count; i++) {
            Author author = authors.get(i);
            JSONObject object = author.toJson();
            object.put("paperCount", getPaperCount(author));
            array.add(object);
        }
        return array.toString();
    }

    private int getPaperCount(Author author) {
        Collection<Paper> papers = author.getPapers();
        if (venue.isEmpty()) return papers.size();

        return (int) papers.stream().filter(paper -> StringUtil.containsIgnoreCase(paper.getVenue(), venue)).count();
    }

    private String getTopPaperIds() {
        if (count == 0) return "";


        List<Paper> filteredPapers = new ArrayList<>();
        for (Paper p : model.getPapers()) {
            if (StringUtil.containsIgnoreCase(p.getVenue(), venue)) {
                filteredPapers.add(p);
            }
        }
        filteredPapers.sort(Comparator.comparingInt(Paper::getInCitationCount).reversed());

        JSONArray array = new JSONArray();
        for (int i = 0; i < count; i++) {
            Paper paper = filteredPapers.get(i);
            array.add(paper.toJson());
        }
        return array.toString();
    }
}
