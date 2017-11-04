package logic.command;

import logic.parser.FileParserManager;
import model.Model;

import java.util.Map;

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

    public void setParameters(Model model, Map<String, String> argumentMap) throws Exception {
        if (!containExpectedArguments(argumentMap)) {
            throw new Exception(String.format(HELP, "Invalid Arguments"));
        }

        this.model = model;
        this.directoryNames = argumentMap.get("directory").split("\\s+");
    }

    private boolean containExpectedArguments(Map<String, String> argumentMap) {
        return argumentMap.containsKey("directory");
    }
}
