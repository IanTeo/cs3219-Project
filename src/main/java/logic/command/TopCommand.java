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
            this.type = StringUtil.parseString(args[1]);
            this.venue = StringUtil.parseString(args[2]);
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
            return countA1 == countA2 ? a2.getName().compareTo(a1.getName()) : countA2 - countA1;
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

        return (int) papers.stream().filter(paper -> paper.getVenue().contains(venue)).count();
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
            Paper paper = filteredPapers.get(i);
            array.add(paper.toJson());
        }
        return array.toString();
    }
}
