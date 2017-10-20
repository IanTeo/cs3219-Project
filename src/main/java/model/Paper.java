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

    private Paper(String id, String title, int year, Set<Author> authors, String venue, Set<Paper> inCitation,
            Set<Paper> outCitation) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.authors = authors;
        this.venue = venue;
        this.inCitation = inCitation;
        this.outCitation = outCitation;
    }

    protected void addCitation(Paper citation) {
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
        for (Author author : getAuthors()) {
            builder.append(author + ",");
        }
        builder.deleteCharAt(builder.length() - 1);
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
        object.put("citationCount", getInCitationCount());
        StringBuilder authorBuilder = new StringBuilder();
        if (!authors.isEmpty()) {
            for (Author author : authors) {
                authorBuilder.append(author.getName()).append(", ");
            }
            authorBuilder.delete(authorBuilder.length() - 2, authorBuilder.length());
        }
        object.put("authors", authorBuilder.toString());
        return object;
    }

    public static class PaperBuilder {
        // copy of attributes used by Paper
        private String id;
        private String title;
        private int year;
        private Set<Author> authors;
        private String venue;
        private Set<Paper> inCitation;
        private Set<Paper> outCitation;

        public PaperBuilder() {
            id = "";
            title = "";
            year = 0;
            authors = new HashSet<>();
            venue = "";
            inCitation = new HashSet<>();
            outCitation = new HashSet<>();
        }

        public PaperBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public PaperBuilder withTitle(String title) {
            this.title = StringUtil.parseString(title);
            return this;
        }

        public PaperBuilder withYear(int year) {
            this.year = year;
            return this;
        }

        public PaperBuilder withAuthors(Author[] authors) {
            this.authors = new HashSet<>(Arrays.asList(authors));
            return this;
        }

        public PaperBuilder withVenue(String venue) {
            this.venue = StringUtil.parseString(venue);
            return this;
        }

        public Paper build() {
            if (id.isEmpty()) {
                throw new RuntimeException("Paper's id is empty.");
            }
            return new Paper(id, title, year, authors, venue, inCitation, outCitation);
        }
    }
}
