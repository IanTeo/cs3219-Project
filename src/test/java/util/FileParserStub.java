package util;

import logic.parser.FileParser;
import model.Model;

import java.io.File;

public class FileParserStub extends FileParser {

    public FileParserStub() {
        super(new ModelStub());
    }

    @Override
    protected void parse(File file) {
        // Do nothing
    }
}
