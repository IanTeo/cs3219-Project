package logic.parser;

import java.util.Map;

import logic.command.WebCommand;
import logic.exception.ParseException;
import model.Model;
import model.Paper;

public class WebCommandParser {
    public static final String ERROR_MISSING_PARAMETERS = "Missing parameters:%nRequired parameters: level, paper";
    
    public WebCommand parse(Model model, Map<String, String> arguments) throws ParseException {
        if (!containExpectedArguments(arguments)) {
            throw new ParseException(ERROR_MISSING_PARAMETERS);
        }
        
        int level = getLevel(arguments.get("level"));
        Paper paper = getPaper(model, arguments.get("paper"));
        return new WebCommand(level, paper);
    }
    
    private boolean containExpectedArguments(Map<String, String> paramMap) {
        return paramMap.containsKey("level")
                && paramMap.containsKey("paper");
    }

    private int getLevel(String level) throws ParseException {
        try {
            return Integer.parseInt(level);
        } catch (Exception e) {
            throw new ParseException("Invalid Level");
        }
    }
    
    private Paper getPaper(Model model, String paperId) throws ParseException {
        Paper paper = model.getPaper(paperId);
        if (paper == null) {
            throw new ParseException("Paper not found");
        }
        return paper;
    }
}
