package logic.command;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import logic.jsonconverter.JsonConverter;
import logic.model.Category;

import logic.exception.ParseException;
import model.Author;
import model.Model;
import model.Paper;
import util.StringUtil;

public class WordCommand implements Command{
    public static final String COMMAND_WORD = "word";
    public static final String HELP = "Error: %s\n" + COMMAND_WORD + "\n" +
            "This command returns a JSON file representing the top # words from paper title/venue and their respective counts" +
            "Required fields: category\n" +
            "Optional fields: ignore\n" +
            "Example: category=paper&ignore=data,effects";
    public static final int MAX_WORDS = 100;
    private Set<String> stopWords = new HashSet<>(Arrays.asList(
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
            "journal", "research", "scientific", "using", "use", "system", "systems", "new", "based", "non", "via",
            "analysis", "study", "model"));

    private Model model;
    private Category category;

    public String execute() {
        HashMap<String, Integer> wordMap = new HashMap<>();

        for (Paper paper : model.getPapers()) {
            String wordByCategory = getWordByCategory(paper);
            if (wordByCategory.equals("<venue unspecified>")) continue;

            String[] words = wordByCategory.split("[\\s\\p{Punct}]+");
            for (String word : words) {
                word = StringUtil.parseString(word);
                if (word.length() < 2 || stopWords.contains(word)) continue;

                int currentCount = wordMap.containsKey(word) ? wordMap.get(word) + 1 : 1;
                wordMap.put(word, currentCount);
            }
        }

        List<Map.Entry<String, Integer>> entryList = wordMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toList());

        if (entryList.size() > MAX_WORDS) {
            entryList = entryList.subList(0, MAX_WORDS);
        }

        return JsonConverter.entryListToWordCloudJson(entryList).toString();
    }

    public void setParameters(Model model, Map<String, String> paramMap) throws ParseException {
        if (!containExpectedArguments(paramMap)) {
            throw new ParseException(String.format(HELP, "Error parsing parameters"));
        }

        this.model = model;
        try {
            this.category = Category.valueOf(paramMap.get("category").toUpperCase());
        } catch (Exception e) {
            throw new ParseException(String.format(HELP, "Category not found"));
        }

        if (paramMap.containsKey("ignore")) {
            String additionalStopWords = StringUtil.parseString(paramMap.get("ignore"));
            this.stopWords.addAll(Arrays.asList(additionalStopWords.split(",")));
        }
    }

    private boolean containExpectedArguments(Map<String, String> paramMap) {
        return paramMap.containsKey("category");
    }

    private String getWordByCategory(Paper paper) {
        switch (category) {
            case VENUE :
                return paper.getVenue();

            case AUTHOR :
                return paper.getAuthors().stream()
                        .map(Author::getName)
                        .collect(Collectors.joining(","));

            case PAPER:
            default :
                return paper.getTitle();
        }
    }
}
