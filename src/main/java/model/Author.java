package model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Each author must have a name and at least one paper written. It optionally has an id.
 * If it has an id, it will be uniquely identified by its id. Otherwise, it will be uniquely identified by its name.
 */
public class Author {
    private final String id;
    private final String name;
    private final Set<Paper> papers = new HashSet<>();

    public Author(String name) {
        this.id = name;
        this.name = name;
    }

    public Author(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addPaper(Paper paper) {
        this.papers.add(paper);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Paper> getPapers() {
        return papers;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Author)) {
            return false;
        }

        Author otherAuthor = (Author) other;
        // Calling papers.equals(otherAuthor.papers) will result in recursion as the papers equality will check for
        // authors equality. Thus, a weaker check to avoid infinite recursion.
        Set<String> thisPapersId = papers.stream().map(Paper::getId).collect(Collectors.toSet());
        Set<String> otherPapersId = otherAuthor.papers.stream().map(Paper::getId).collect(Collectors.toSet());

        return id.equals(otherAuthor.id) && name.equals(otherAuthor.name) && thisPapersId.equals(otherPapersId);
    }

    @Override
    public String toString() {
        return id + " " + name + " " + papers.stream().map(Paper::getId).collect(Collectors.toList());
    }
}
