package util;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {
    private static final String BASE_URL = "src/test/res/%s";
    public static String readFile(String fileName) {
        String fileContents;
        try {
            byte[] encodedFile = Files.readAllBytes(
                    Paths.get(String.format(BASE_URL, fileName)));

            fileContents = new String(encodedFile);
        } catch (IOException e) {
            fileContents = "Error parsing file";
        }
        return fileContents;
    }
}
