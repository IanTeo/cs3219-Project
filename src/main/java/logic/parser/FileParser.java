package logic.parser;

import model.Model;

import java.io.File;

public abstract class FileParser {
    protected Model model;

    public FileParser(Model model) {
        this.model = model;
    }

    protected abstract void parse(File file);
}
