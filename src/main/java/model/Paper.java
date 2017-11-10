package model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

public class Paper {
    private String id;
    private String title;
    private int year;
    private Set<Author> authors;
    private String venue;
    // List of papers that cite this paper
    private Set<Paper> inCitation;
    // List of papers that are cited in this paper
    private Set<Paper> outCitation;

    public Paper(String id, String title, int year, Author[] authors, String venue) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.authors = new LinkedHashSet<>(Arrays.asList(authors));
        this.venue = venue.equals("") ? "<venue unspecified>" : venue;
        this.inCitation = new LinkedHashSet<>();
        this.outCitation = new LinkedHashSet<>();
    }

    public void addCitation(Paper citation) {
        this.outCitation.add(citation);
        citation.inCitation.add(this);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public Set<Author> getAuthors() {
        return authors;
    }
    
    public String getVenue() {
        return venue;
    }

    public Collection<Paper> getInCitation() {
        return inCitation;
    }

    public int getInCitationCount() {
        return inCitation.size();
    }

    public Collection<Paper> getOutCitation() {
        return outCitation;
    }

    public int getOutCitationCount() {
        return outCitation.size();
    }

    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("title", title);
        object.put("year", year);
        object.put("venue", venue);
        object.put("citationCount", getInCitationCount());
        String authorString = authors.stream().map(Author::getName).collect(Collectors.joining(", "));
        object.put("authors", authorString);
        return object;
    }
}
