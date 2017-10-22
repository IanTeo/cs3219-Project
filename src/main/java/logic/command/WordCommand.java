package logic.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.Model;
import model.Paper;

public class WordCommand implements Command{
    public static final String COMMAND_WORD = "word";
    public static final String HELP = "Error: %s\nUsage: word [#] [title/venue]" +
            "This command returns a JSON file representing the top # words from paper title/venue and their respective counts";

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            // Common stop words with more than 1 character
            "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at",
            "be", "because", "been", "before", "being", "below", "between", "both", "but", "by",
            "cannot", "could", "did", "do", "does", "down", "during", "each", "few", "for", "from", "further",
            "had", "has", "have", "having", "he", "her", "here", "hers", "herself", "him", "himself", "his", "how",
            "if", "in", "into", "is", "it", "its", "itself", "me", "more", "most", "my", "myself", "no", "nor", "not",
            "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own",
            "same", "she", "should", "so", "some", "such","than", "that", "the", "their", "theirs", "them", "themselves",
            "then", "there", "these", "they", "this", "those", "through", "to", "too", "under", "until", "up", "very",
            "was", "we", "were", "what", "when", "where", "which", "while", "who", "whom", "why", "with", "would",
            "you", "your", "yours", "yourself", "yourselves",
            // Special stop words specific to problem domain
            "journal", "research", "scientific", "using", "use", "system", "systems", "new", "based", "non", "via"));

    private Model model;
    private int count;
    private String type;

    public String execute() {
        HashMap<String, Integer> wordMap = new HashMap<>();

        for (Paper paper : model.getPapers()) {
            String[] words = getWord(paper).split("[\\s\\p{Punct}]+");
            for (String word : words) {
                word = word.toLowerCase();
                if (word.length() < 2 || STOP_WORDS.contains(word)) continue;

                int currentCount = wordMap.containsKey(word) ? wordMap.get(word) + 1 : 1;
                wordMap.put(word, currentCount);
            }
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordMap.entrySet());
        entryList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        JSONArray array = new JSONArray();
        int maxCount = entryList.get(0).getValue();
        for (Map.Entry<String, Integer> entry : entryList) {
            if (array.size() >= count) break;
            String key = entry.getKey();
            int count = entry.getValue();
            if (count <= 2) break;

            JSONObject object = new JSONObject();
            object.put("word", key);
            object.put("count", count);
            object.put("size", getPercentageInt(count, maxCount));
            array.add(object);
        }

        return array.toString();
    }

    public void setParameters(Model model, String arguments) throws Exception {
        try {
            this.model = model;
            String[] args = arguments.split(" ");
            this.count = Integer.parseInt(args[0].trim());
            this.type = args[1].trim();
        } catch (Exception e) {
            throw new Exception(String.format(HELP, "Error parsing parameters"));
        }
    }

    private String getWord(Paper paper) {
        switch (type) {
            case "venue" :
                return paper.getVenue();

            case "title" :
            default :
                return paper.getTitle();
        }
    }

    private int getPercentageInt(int numerator, int denominator) {
        return (int) ((numerator * 100.0) / denominator);
    }
}
