package logic.command;

import logic.parser.FileParserManager;
import model.Model;

public class LoadCommand implements Command{
    public static final String COMMAND_WORD = "load";
    public static final String BASE_URL = "Data/%s";
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
            return "Error parsing file";
        }
        return builder.toString();
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        this.directoryNames = arguments.split("\\s+");
    }
}
