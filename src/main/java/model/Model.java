package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private Map<String, Paper> papers;
    private Map<String, String> titleToIdMap;
    private Map<String, Author> authors;

    public Model() {
        papers = new HashMap<>();
        titleToIdMap = new HashMap<>();
        authors = new HashMap<>();
    }

    public void addPaper(Paper paper) {
        if (papers.containsKey(paper.getId())) return;

        titleToIdMap.put(paper.getTitle().toLowerCase(), paper.getId());
        papers.put(paper.getId(), paper);
    }

    /**
     * Adds {@code author} if it does not exist.
     */
    public void addAuthor(Author author) {
        String uniqueIdentifier = author.getId().toLowerCase();
        if (authors.containsKey(uniqueIdentifier)) {
            return;
        }

        authors.put(uniqueIdentifier, author);
    }

    public boolean hasPaper(String paperId) {
        return papers.containsKey(paperId);
    }

    /**
     * Returns true if this model contains an author that can be uniquely identified with {@code uniqueIdentifier}.
     */
    public boolean hasAuthor(String id) {
        return authors.containsKey(id.toLowerCase());
    }

    /**
     * Returns the author stored in this model that is uniquely identified as {@code uniqueIdentifier}.
     */
    public Author getAuthor(String id) {
        String lowerCaseId = id.toLowerCase();
        if (!hasAuthor(lowerCaseId)) {
            throw new IllegalArgumentException("No such author exists.");
        }

        return authors.get(lowerCaseId);
    }

    public Paper getPaper(String id) {
        Paper paper = papers.get(id);
        if (paper == null) {
            paper = papers.get(titleToIdMap.get(id.toLowerCase()));
        }
        return paper;
    }

    public Collection<Paper> getPapers() {
        return new ArrayList<>(papers.values());
    }

    public Collection<Author> getAuthors() {
        return authors.values();
    }

    public void clear() {
        papers.clear();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Model)) {
            return false;
        }

        Model otherModel = (Model) other;
        return papers.equals(otherModel.papers) && authors.equals(otherModel.authors);
    }

    @Override
    public String toString() {
        return papers.values().toString() + authors.values().toString();
    }
}
