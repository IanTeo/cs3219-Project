package logic.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        // Prevent de-referencing errors
        File[] files = folder.listFiles();
        if (files == null) return;

        List<File> fileNames = Arrays.asList(files);
        for (File fileEntry : fileNames) {
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
