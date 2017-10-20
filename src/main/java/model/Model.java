package model;

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

        titleToIdMap.put(paper.getTitle(), paper.getId());
        papers.put(paper.getId(), paper);
    }

    /**
     * Adds {@code author} if it does not exist.
     */
    public void addAuthor(Author author) {
        String uniqueIdentifier = author.getId();
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
    public boolean hasAuthor(String uniqueIdentifier) {
        return authors.containsKey(uniqueIdentifier);
    }

    /**
     * Returns the author stored in this model that is uniquely identified as {@code uniqueIdentifier}.
     */
    public Author getAuthor(String uniqueIdentifier) {
        if (!hasAuthor(uniqueIdentifier)) {
            throw new IllegalArgumentException("No such author exists.");
        }

        return authors.get(uniqueIdentifier);
    }

    public Paper getPaper(String id) {
        Paper paper = papers.get(id);
        if (paper == null) {
            paper = papers.get(titleToIdMap.get(id));
        }
        return paper;
    }

    public void addCitation(Paper paper, Paper citation) {
        paper.addCitation(citation);
    }

    public Collection<Paper> getPapers() {
        return papers.values();
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
