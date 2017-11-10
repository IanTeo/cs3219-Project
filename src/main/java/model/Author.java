package model;

import org.json.simple.JSONObject;

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
    private final Set<Paper> papers;

    public Author(String name) {
        this.id = this.name = name;
        this.papers = new HashSet<>();
    }

    public Author(String id, String name) {
        this.id = id;
        this.name = name;
        this.papers = new HashSet<>();
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

    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("paperCount", papers.size());
        return object;
    }
}
