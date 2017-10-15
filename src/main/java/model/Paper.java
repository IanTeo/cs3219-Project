package model;
import util.StringUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Paper {
    private String id;
    private String title;
    private int date;
    private HashSet<String> authors;
    private String venue;
    // List of papers that cite this paper
    private HashMap<String, Paper> inCitation;
    // List of papers that are cited in this paper
    private HashMap<String, Paper> outCitation;

    public Paper(String id) {
        this.id = id;
        this.title = "";
        this.date = 0;
        this.authors = new HashSet<>();
        this.venue = "";
        inCitation = new HashMap<>();
        outCitation = new HashMap<>();
    }
    public Paper(String id, String title, int date, String[] authors, String venue) {
        this.id = id;
        this.title = StringUtil.parseString(title);
        this.date = date;
        this.authors = new HashSet<>();
        this.authors.addAll(parseAuthors(authors));
        this.venue = StringUtil.parseString(venue);
        inCitation = new HashMap<>();
        outCitation = new HashMap<>();
    }

    protected void addCitation(Paper citation) {
        if (citation.inCitation.containsKey(this.title) || this.outCitation.containsKey(citation.title)) return;

        this.outCitation.put(citation.title, citation);
        citation.inCitation.put(this.title, this);
    }

    public void updateMissingInformation(Paper paper) {
        if (date == 0) this.date = paper.date;
        this.authors.addAll(parseAuthors(paper.getAuthors()));
        if ("".equals(venue)) this.venue = StringUtil.parseString(paper.venue);
        if ("".equals(title)) this.title = StringUtil.parseString(paper.title);
    }

    private HashSet<String> parseAuthors(String[] authors) {
        HashSet<String> authorSet = new HashSet<>();
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

    public int getDate() {
        return date;
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
        builder.append("Date: ").append(date).append("\n");
        builder.append("Authors: ");
        for (String author : getAuthors()) {
            builder.append(author + ",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("\n");

        return builder.toString();
    }
}
