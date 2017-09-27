package logic.parser;

import model.Model;

import java.io.File;

public class FileParserManager {
    private Model model;
    private int numFiles;
    private FileParser xmlParser;

    public FileParserManager(Model model) {
        this.model = model;
        numFiles = 0;
        xmlParser = new XmlFileParser(model);
    }
    public void parseFilesInDirectory(String directoryName) {
        File folder = new File(directoryName);
        parseFilesInDirectory(folder);
    }

    private void parseFilesInDirectory(File folder) {
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                parseFilesInDirectory(fileEntry);
            } else {
                if (!isHiddenFile(fileEntry)) {
                    numFiles++;
                    //System.out.println(fileEntry.getName());
                    FileParser parser = selectParser(fileEntry);
                    parser.parse(fileEntry);
                }
            }
        }
        model.incNumDataSet(numFiles);
    }

    private boolean isHiddenFile(File file) {
        // Check if the first character is a '.', which signals that it is a hidden file
        return file.getName().charAt(0) == '.';
    }

    private FileParser selectParser(File file) {
        FileParser parser = xmlParser;
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
        switch (fileType.toLowerCase()) {
            case "xml" :
            default :
                parser = xmlParser;
                break;

            /*case "json" :
                parser = jsonParser;
                break;*/
        }
        return parser;
    }
}
