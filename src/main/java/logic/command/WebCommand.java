package logic.command;

import java.util.LinkedList;
import java.util.Queue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import model.Model;
import model.Paper;
import util.StringUtil;


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
            paperId = StringUtil.sanitise(arguments.substring(levelEndIndex));
        } catch (Exception e) {
            throw new Exception(String.format(HELP, "Error parsing parameters"));
        }
    }

    private JSONObject createCitationWeb(Paper basePaper, int level) {
        JSONArray paperArray = new JSONArray();
        JSONArray edgeList = new JSONArray();

        int currentLevel = 1;
        HashSet<String> visitedSet = new HashSet<>();

        Queue<Paper> paperQueue = new LinkedList<>();
        paperQueue.offer(basePaper);

        // Add null to the end of every "round" to show a new level
        paperQueue.offer(null);

        while (!paperQueue.isEmpty() && currentLevel <= level) {
            Paper next = paperQueue.poll();
            if (next == null) {
                // Encounter null means time to increment level
                paperQueue.offer(null);
                currentLevel++;
                if (paperQueue.peek() == null) break; // 2 nulls means terminate
                else continue;
            }

            if (visitedSet.contains(next.getId())) continue;
            visitedSet.add(next.getId());

            JSONObject paperObj = next.toJson();
            paperObj.put("group", currentLevel);
            paperArray.add(paperObj);

            // If we are at the last level, we do not want to add the new edges
            if (currentLevel == level) continue;

            for (Paper citation : next.getInCitation()) {
                paperQueue.offer(citation);

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
