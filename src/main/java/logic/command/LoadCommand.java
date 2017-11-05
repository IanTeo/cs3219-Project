package logic.command;

import logic.exception.ParseException;
import logic.parser.FileParserManager;
import model.Author;
import model.Model;
import model.Paper;

import java.util.*;
import java.util.stream.Collectors;

public class LoadCommand implements Command{
    public static final String COMMAND_WORD = "load";
    public static final String BASE_URL = "Data/%s";
    public static final String HELP = "Error: %s\nUsage: load [folder...]\n" +
            "This command loads all datasets in the specified folder(s)";
    private Model model;
    private String[] directoryNames;

    public String execute() {
        model.clear();
        FileParserManager fileParserManager = new FileParserManager(model);
        StringBuilder builder = new StringBuilder();
        builder.append("Successfully loaded ");
        try {
            for (String directoryName : directoryNames) {
                fileParserManager.parseFilesInDirectory(String.format(BASE_URL, directoryName));
                builder.append(directoryName + " ");
            }
        } catch (Exception e) {
            model.clear();
            e.printStackTrace();
            return String.format(HELP, "Error parsing file");
        }
        return builder.toString();
    }

    public void setParameters(Model model, Map<String, String> paramMap) throws ParseException {
        if (!containExpectedArguments(paramMap)) {
            throw new ParseException(String.format(HELP, "Error parsing parameters"));
        }

        this.model = model;
        this.directoryNames = paramMap.get("directory").split("\\s+");
    }

    private boolean containExpectedArguments(Map<String, String> paramMap) {
        return paramMap.containsKey("directory");
    }
}
