package logic.parser;

import java.io.File;

import model.Model;

public class FileParserManager {
    private FileParser jsonParser;

    public FileParserManager(Model model) {
        jsonParser = new JsonFileParser(model);
    }
    public void parseFilesInDirectory(String directoryName) {
        File folder = new File(directoryName);
        parseFilesInDirectory(folder);
    }

    private void parseFilesInDirectory(File folder) {
        if (folder == null) return;
        
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry == null) continue;

            if (!fileEntry.isDirectory() && !isHiddenFile(fileEntry)) {
                jsonParser.parse(fileEntry);
            }
        }
    }

    private boolean isHiddenFile(File file) {
        // Check if the first character is a '.', which signals that it is a hidden file
        return file.getName().charAt(0) == '.';
    }
}
