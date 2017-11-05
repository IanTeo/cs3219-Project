package logic.command;

import logic.exception.ParseException;
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

    public void setParameters(Model model, Map<String, String> paramMap) throws ParseException {
        if (!containExpectedArguments(paramMap)) {
            throw new ParseException(String.format(HELP, "Error parsing parameters"));
        }

        this.model = model;
        this.venue = paramMap.get("venue");
    }

    private boolean containExpectedArguments(Map<String, String> paramMap) {
        return paramMap.containsKey("venue");
    }
}
