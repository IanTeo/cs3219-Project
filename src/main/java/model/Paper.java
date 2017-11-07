package model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import util.StringUtil;

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
        this.authors = new HashSet<>(Arrays.asList(authors));
        this.venue = venue;
        this.inCitation = new HashSet<>();
        this.outCitation = new HashSet<>();
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Paper)) {
            return false;
        }

        Paper otherPaper = (Paper) other;
        // Calling paper or author equality will cause infinite recursion. See Author#equals(Object).
        Set<String> thisInCitationId = inCitation.stream().map(Paper::getId).collect(Collectors.toSet());
        Set<String> otherInCitationId = otherPaper.inCitation.stream().map(Paper::getId).collect(Collectors.toSet());
        Set<String> thisOutCitationId = outCitation.stream().map(Paper::getId).collect(Collectors.toSet());
        Set<String> otherOutCitationId = otherPaper.outCitation.stream().map(Paper::getId).collect(Collectors.toSet());
        Set<String> thisAuthorId = authors.stream().map(Author::getId).collect(Collectors.toSet());
        Set<String> otherAuthorId = otherPaper.authors.stream().map(Author::getId).collect(Collectors.toSet());

        return id.equals(otherPaper.id)
                && title.equals(otherPaper.title)
                && year == otherPaper.year
                && venue.equals(otherPaper.venue)
                && thisAuthorId.equals(otherAuthorId)
                && thisInCitationId.equals(otherInCitationId)
                && thisOutCitationId.equals(otherOutCitationId);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!id.equals("")) builder.append("ID: ").append(id).append("\n");
        builder.append("Title: ").append(title).append("\n");
        builder.append("Date: ").append(year).append("\n");
        builder.append("Authors: ");
        builder.append(authors.stream().map(Author::getName).collect(Collectors.joining(", ")));
        builder.append("\n");
        builder.append("In Citations: ").append(getInCitationCount()).append("\n");
        builder.append("Out Citations: ").append(getOutCitationCount()).append("\n");

        return builder.toString();
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
