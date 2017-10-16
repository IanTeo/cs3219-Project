package model;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.StringUtil;

public class Paper {
    private String id;
    private String title;
    private int year;
    private Set<String> authors;
    private String venue;
    // List of papers that cite this paper
    private Map<String, Paper> inCitation;
    // List of papers that are cited in this paper
    private Map<String, Paper> outCitation;

    private Paper(String id, String title, int year, Set<String> authors, String venue, Map<String, Paper> inCitation,
            Map<String, Paper> outCitation) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.authors = authors;
        this.venue = venue;
        this.inCitation = inCitation;
        this.outCitation = outCitation;
    }

    protected void addCitation(Paper citation) {
        if (citation.inCitation.containsKey(this.id) || this.outCitation.containsKey(citation.id)) return;

        this.outCitation.put(citation.id, citation);
        citation.inCitation.put(this.id, this);
    }

    public void updateMissingInformation(Paper paper) {
        if (year == 0) this.year = paper.year;
        this.authors.addAll(parseAuthors(paper.getAuthors()));
        if ("".equals(venue)) this.venue = StringUtil.parseString(paper.venue);
        if ("".equals(title)) this.title = StringUtil.parseString(paper.title);
    }

    private static Set<String> parseAuthors(String[] authors) {
        Set<String> authorSet = new HashSet<>();
        for (String author : authors) {
            authorSet.add(StringUtil.parseString(author));
        }
        return authorSet;
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

    public String[] getAuthors() {
        return authors.toArray(new String[authors.size()]);
    }
    
    public boolean hasAuthor(String author) {
        return authors.contains(author);
    }

    public String getVenue() {
        return venue;
    }

    public Collection<Paper> getInCitation() {
        return inCitation.values();
    }

    public Collection<Paper> getOutCitation() {
        return outCitation.values();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!id.equals("")) builder.append("ID: ").append(id).append("\n");
        builder.append("Title: ").append(title).append("\n");
        builder.append("Date: ").append(year).append("\n");
        builder.append("Authors: ");
        for (String author : getAuthors()) {
            builder.append(author + ",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("\n");

        return builder.toString();
    }

    public static class PaperBuilder {
        // copy of attributes used by Paper
        private String id;
        private String title;
        private int year;
        private Set<String> authors;
        private String venue;
        private Map<String, Paper> inCitation;
        private Map<String, Paper> outCitation;

        public PaperBuilder() {
            id = "";
            title = "";
            year = 0;
            authors = new HashSet<>();
            venue = "";
            inCitation = new HashMap<>();
            outCitation = new HashMap<>();
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

        public PaperBuilder withAuthors(String[] authors) {
            this.authors = new HashSet<>(parseAuthors(authors));
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
