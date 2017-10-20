package logic.command;

import model.Model;
import model.Paper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.StringUtil;

import java.util.LinkedList;
import java.util.Queue;

public class WebCommand implements Command{
    public static final String COMMAND_WORD = "web";
    public static final String HELP = "Error: %s\nUsage: web [#] [paper]\n" +
            "This command returns a JSON file representing a in-citation web with # levels, starting from the paper specified";
    private Model model;
    private int level;
    private String paperId;

    public String execute() {

        Paper paper = model.getPaper(paperId);
        if (paper == null) {
            return String.format(HELP, "Paper not found");
        }

        return createCitationWeb(paper, level).toString();
    }

    public void setParameters(Model model, String arguments) throws Exception {
        try {
            this.model = model;
            int levelEndIndex = arguments.indexOf(" ");
            level = Integer.parseInt(arguments.substring(0, levelEndIndex).trim());
            paperId = StringUtil.parseString(arguments.substring(levelEndIndex));
        } catch (Exception e) {
            throw new Exception(String.format(HELP, "Error parsing parameters"));
        }
    }

    private JSONObject createCitationWeb(Paper basePaper, int level) {
        JSONArray paperArray = new JSONArray();
        JSONArray edgeList = new JSONArray();

        int currentLevel = 2;
        Queue<Paper> paperQueue = new LinkedList<>();
        paperQueue.offer(basePaper);
        JSONObject basePaperObj = basePaper.toJson();
        basePaperObj.put("group", 1);
        paperArray.add(basePaperObj);
        paperQueue.offer(null);

        while (!paperQueue.isEmpty() && currentLevel <= level) {
            Paper next = paperQueue.poll();
            if (next == null) {
                paperQueue.offer(null);
                currentLevel++;
                if (paperQueue.peek() == null) break; // 2 nulls means terminate
                else continue;
            }
            for (Paper citation : next.getInCitation()) {
                paperQueue.offer(citation);
                JSONObject citationObj = citation.toJson();
                citationObj.put("group", currentLevel);
                paperArray.add(citationObj);

                JSONObject edge = new JSONObject();
                edge.put("source", next.getId());
                edge.put("target", citation.getId());
                edgeList.add(edge);
            }
        }
        JSONObject citationWeb = new JSONObject();
        citationWeb.put("nodes", paperArray);
        citationWeb.put("links", edgeList);
        return citationWeb;
    }
}
