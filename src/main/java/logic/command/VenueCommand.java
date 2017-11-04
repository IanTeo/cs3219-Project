package logic.command;

import org.json.simple.JSONArray;

import model.Model;
import model.Paper;
import util.StringUtil;

import java.util.Map;

public class VenueCommand implements Command{
    public static final String COMMAND_WORD = "venue";
    public static final String HELP = "Error: %s\nUsage: venue [venue]\n" +
            "This command returns a JSON file representing the papers for the specified venue";
    private Model model;
    private String venue;

    public String execute() {
        JSONArray array = new JSONArray();
        for (Paper paper : model.getPapers()) {
            if (StringUtil.containsIgnoreCase(paper.getVenue(), venue)) {
                array.add(paper.toJson());
            }
        }

        return array.toString();
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        this.venue = arguments;
    }

    public void setParameters(Model model, Map<String, String> argumentMap) throws Exception {
        if (!containExpectedArguments(argumentMap)) {
            throw new Exception(String.format(HELP, "Invalid Arguments"));
        }

        this.model = model;
        this.venue = StringUtil.parseString(argumentMap.get("venue"));
    }

    private boolean containExpectedArguments(Map<String, String> argumentMap) {
        return argumentMap.containsKey("venue");
    }
}
