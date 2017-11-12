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

import model.Author;
import model.Model;
import model.Paper;
import util.StringUtil;

public class WordCommand implements Command{
    public static final String COMMAND_WORD = "word";
    public static final int MAX_WORDS = 100;

    private Model model;
    public final Category category;
    public final Set<String> stopWords = new HashSet<>(Arrays.asList(
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

    public WordCommand(Model model, Category category, List<String> additionalStopWords) {
        this.model = model;
        this.category = category;
        this.stopWords.addAll(additionalStopWords);
    }
    
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
