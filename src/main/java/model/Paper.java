package model;
import util.StringUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Paper {
    private String title;
    private int date;
    private HashSet<String> authors;
    private String fileName;
    private String rawString;
    // List of papers that cite this paper
    private HashMap<String, Paper> inCitation;
    // List of papers that are cited in this paper
    private HashMap<String, Paper> outCitation;

    //TODO: Need to keep track of number of citations
    public Paper(String title, int date, String[] authors, String fileName, String rawString) {
        this.title = title;
        this.date = date;
        this.authors = new HashSet<>();
        this.authors.addAll(parseAuthors(authors));
        this.fileName = fileName;
        this.rawString = rawString.toLowerCase();
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
        if ("".equals(fileName)) this.fileName = paper.fileName;
        if ("".equals(rawString)) this.rawString = paper.rawString.toLowerCase();
    }

    private HashSet<String> parseAuthors(String[] authors) {
        HashSet<String> authorSet = new HashSet<>();
        for (String author : authors) {
            authorSet.add(StringUtil.parseString(author));
        }
        return authorSet;
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

    public String getFileName() {
        return fileName;
    }
    
    public String getRawString() {
        return rawString;
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
