package stub;

import model.Author;
import model.Model;
import model.Paper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModelStub implements Model {
    private Map<String, Paper> papers;
    private Map<String, Author> authors;

    public ModelStub() {
        papers = new HashMap<>();
        authors = new HashMap<>();

        papers.put("", new Paper.PaperBuilder().withId());
    }

    @Override
    public void addPaper(Paper paper) {

    }

    @Override
    public boolean hasPaper(String paper) {
        return false;
    }

    @Override
    public Paper getPaper(String paper) {
        return null;
    }

    @Override
    public Collection<Paper> getPapers() {
        return null;
    }

    @Override
    public void addAuthor(Author author) {

    }

    @Override
    public boolean hasAuthor(String author) {
        return false;
    }

    @Override
    public Author getAuthor(String author) {
        return null;
    }

    @Override
    public Collection<Author> getAuthors() {
        return null;
    }

    @Override
    public void clear() {

    }
}
