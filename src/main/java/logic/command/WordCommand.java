package logic.command;

import model.Model;
import model.Paper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.StringUtil;

import java.util.*;

public class WordCommand implements Command{
    public static final String COMMAND_WORD = "word";
    public static final String HELP = "Error: %s\nUsage: word [#] [title/venue]" +
            "This command returns a JSON file representing the top # words from paper title/venue and their respective counts";
    public static final String[] STOP_WORDS = {
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
            "journal", "research", "scientific", "using", "use", "system", "systems", "new", "based", "non", "via" };

    private Model model;
    private int count;
    private String type;

    public String execute() {
        HashMap<String, Integer> wordMap = new HashMap<>();
        HashSet<String> stopWords = new HashSet<>(Arrays.asList(STOP_WORDS));

        JSONArray array = new JSONArray();
        for (Paper paper : model.getPapers()) {
            String[] words = getWord(paper).split("[\\s\\p{Punct}]+");
            for (int i = 0; i < words.length; i++) {
                String word = words[i].toLowerCase();
                if (word.length() < 2) continue;
                if (stopWords.contains(word)) continue;

                int currentCount = 1;
                if (wordMap.containsKey(word)) {
                    currentCount = wordMap.get(word) + 1;
                }
                wordMap.put(word, currentCount);
            }
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordMap.entrySet());
        entryList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        int maxCount = entryList.get(0).getValue();
        for (int i = 0; i < entryList.size(); i++) {
            if (array.size() >= count) break;
            String key = entryList.get(i).getKey();

            int count = wordMap.get(key);
            if (count > 2) {
                JSONObject object = new JSONObject();
                int wordCount = wordMap.get(key);
                object.put("word", key);
                object.put("count", wordCount);
                object.put("size", getPercentageInt(wordCount, maxCount));
                array.add(object);
            }
        }

        return array.toString();
    }

    public void setParameters(Model model, Map<String, String> argumentMap) throws Exception {
        if (!containExpectedArguments(argumentMap)) {
            throw new Exception(String.format(HELP, "Invalid Arguments"));
        }

        this.model = model;
        this.count = Integer.parseInt(argumentMap.get("count"));
        this.type = StringUtil.parseString(argumentMap.get("type"));
    }

    private boolean containExpectedArguments(Map<String, String> argumentMap) {
        return argumentMap.containsKey("count")
                && argumentMap.get("count").matches("[0-9]+")
                && argumentMap.containsKey("type");
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

    private int getPercentageInt(int numerator, int demoninator) {
        return (int) ((numerator * 100.0) / demoninator);
    }
}
