package util;

import model.Author;
import model.Model;
import model.Paper;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static util.SampleData.*;

public class ModelStub implements Model {
    private Map<String, Paper> papers;
    private Map<String, Author> authors;

    public ModelStub() {
        papers = new LinkedHashMap<>();
        authors = new LinkedHashMap<>();

        addPaper(PAPER_1);
        addPaper(PAPER_2);
        addPaper(PAPER_3);
        addPaper(PAPER_4);

        addAuthor(AUTHOR_1);
        addAuthor(AUTHOR_2);
        addAuthor(AUTHOR_3);
    }

    @Override
    public void addPaper(Paper paper) {
        papers.put(paper.getId(), paper);
    }

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
    public void addAuthor(Author author) {
        authors.put(author.getId(), author);
    }

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
