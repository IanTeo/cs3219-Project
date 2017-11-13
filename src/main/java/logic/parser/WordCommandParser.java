package logic.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import logic.command.WordCommand;
import logic.exception.ParseException;
import logic.model.Category;
import model.Model;
import util.StringUtil;

public class WordCommandParser {
    public static final String ERROR_MISSING_PARAMETERS = "Missing parameters, Required parameters: category";
    
    public WordCommand parse(Model model, Map<String, String> arguments) throws ParseException {
        if (!containExpectedArguments(arguments)) {
            throw new ParseException(ERROR_MISSING_PARAMETERS);
        }
        
        Category category = getCategory(arguments.get("category"));
        List<String> stopWords = getStopWords(arguments.get("ignore"));
        return new WordCommand(model, category, stopWords);
    }
    
    private boolean containExpectedArguments(Map<String, String> paramMap) {
        return paramMap.containsKey("category");
    }

    private Category getCategory(String category) throws ParseException {
        try {
            return Category.valueOf(category.toUpperCase());
        } catch (Exception e) {
            throw new ParseException("Invalid Category");
        }
    }
    
    private List<String> getStopWords(String ignore) throws ParseException {
        if (ignore == null) {
            return new ArrayList<>();
        }

        List<String> stopWords = Arrays.asList(ignore.split(",")).stream()
                .map(StringUtil::parseString)
                .collect(Collectors.toList());
        return stopWords;
    }
}
