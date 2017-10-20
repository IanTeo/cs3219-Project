package logic.command;

import model.Author;
import model.Model;
import model.Paper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.StringUtil;

public class VenueCommand implements Command{
    public static final String COMMAND_WORD = "venue";
    private Model model;
    private String venue;

    public String execute() {
        JSONArray array = new JSONArray();
        for (Paper paper : model.getPapers()) {
            if (paper.getVenue().contains(venue)) {
                array.add(paper.toJson());
            }
        }

        return array.toString();
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        this.venue = StringUtil.parseString(arguments);
    }
}
