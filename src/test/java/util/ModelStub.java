package util;

import model.Author;
import model.Model;
import model.Paper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static util.SampleData.*;

public class ModelStub implements Model {
    private Map<String, Paper> papers;
    private Map<String, Author> authors;

    public ModelStub() {
        papers = new HashMap<>();
        authors = new HashMap<>();

        papers.put(PAPER_1.getId(), PAPER_1);
        papers.put(PAPER_2.getId(), PAPER_2);
        papers.put(PAPER_3.getId(), PAPER_3);
        papers.put(PAPER_4.getId(), PAPER_4);

        authors.put(AUTHOR_1.getId(), AUTHOR_1);
        authors.put(AUTHOR_2.getId(), AUTHOR_2);
        authors.put(AUTHOR_3.getId(), AUTHOR_3);
    }

    @Override
    public void addPaper(Paper paper) { }

    @Override
    public boolean hasPaper(String paper) {
        return papers.containsKey(paper);
    }

    @Override
    public Paper getPaper(String paper) {
        return papers.get(paper);
    }

    @Override
    public Collection<Paper> getPapers() {
        return papers.values();
    }

    @Override
    public void addAuthor(Author author) { }

    @Override
    public boolean hasAuthor(String author) {
        return authors.containsKey(author);
    }

    @Override
    public Author getAuthor(String author) {
        return authors.get(author);
    }

    @Override
    public Collection<Author> getAuthors() {
        return authors.values();
    }

    @Override
    public void clear() { }
}
