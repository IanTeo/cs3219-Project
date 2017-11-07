package stub;

import model.Author;
import model.Model;
import model.Paper;

import java.util.ArrayList;
import java.util.Collection;

public class ModelStub implements Model {
    private Collection<Paper> papers;
    private Collection<Author> authors;

    public ModelStub() {
        papers = new ArrayList<>();
        authors = new ArrayList<>();
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
