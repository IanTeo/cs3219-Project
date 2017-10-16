package logic.parser;

import java.io.File;

import model.Model;

public class FileParserManager {
    private Model model;
    private FileParser jsonParser;

    public FileParserManager(Model model) {
        this.model = model;
        jsonParser = new JsonFileParser(model);
    }
    public void parseFilesInDirectory(String directoryName) {
        File folder = new File(directoryName);
        parseFilesInDirectory(folder);
    }

    private void parseFilesInDirectory(File folder) {
        int numFiles = 0;
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                parseFilesInDirectory(fileEntry);
            } else {
                if (!isHiddenFile(fileEntry)) {
                    numFiles++;
                    jsonParser.parse(fileEntry);
                }
            }
        }
        model.incNumDataSet(numFiles);
    }

    private boolean isHiddenFile(File file) {
        // Check if the first character is a '.', which signals that it is a hidden file
        return file.getName().charAt(0) == '.';
    }
}
