package model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Paper {
    private String title;
    private int date;
    private HashSet<String> authors;
    private String fileName;
    // List of papers that cite this paper
    private HashMap<String, Paper> inCitation;
    // List of papers that are cited in this paper
    private HashMap<String, Paper> outCitation;

    //TODO: Need to keep track of number of citations
    public Paper(String title, int date, String[] authors, String fileName) {
        this.title = title; //TODO: What if paper title is blank?
        this.date = date;
        this.authors = parseAuthors(authors);
        this.fileName = fileName;
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
        if (authors.size() == 0) this.authors = parseAuthors(paper.getAuthors());
        if (fileName.equals("")) this.fileName = paper.fileName;
    }

    private HashSet<String> parseAuthors(String[] authors) {
        return new HashSet<>(Arrays.asList(authors));
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

    public String getFileName() {
        return fileName;
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
        if (!fileName.equals("")) builder.append("File: ").append(fileName).append("\n");
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
