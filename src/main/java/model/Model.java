package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private Map<String, Paper> papers;
    private Map<String, String> titleToIdMap;
    private Map<String, Author> authors;
    private int numDataSet;

    public Model() {
        papers = new HashMap<>();
        titleToIdMap = new HashMap<>();
        authors = new HashMap<>();
        numDataSet = 0;
    }

    public void addPaper(Paper paper) {
        titleToIdMap.put(paper.getTitle(), paper.getId());
        if (papers.containsKey(paper.getId())) {
            papers.get(paper.getId()).updateMissingInformation(paper);
            return;
        }

        papers.put(paper.getId(), paper);
    }

    public Paper getPaperByName(String paperName) {
        return getPaperById(titleToIdMap.get(paperName));
    }

    /**
     * Adds {@code author} if it does not exist.
     */
    public void addAuthor(Author author) {
        String uniqueIdentifier = author.getUniqueIdentifier();
        if (authors.containsKey(uniqueIdentifier)) {
            return;
        }

        authors.put(uniqueIdentifier, author);
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

    public Paper getPaperById(String id) {
        return papers.get(id);
    }

    public void addCitation(Paper paper, Paper citation) {
        paper.addCitation(citation);
    }

    public Collection<Paper> getPapers() {
        return papers.values();
    }

    public void incNumDataSet(int numDataSet) {
        this.numDataSet += numDataSet;
    }

    public int getNumDataSet() {
        return numDataSet;
    }

    public void clear() {
        papers.clear();
        numDataSet = 0;
    }

    // Stuff to do testing
    public void printPapers() {
        Collection<Paper> paperList = papers.values();
        for (Paper p : paperList) {
            System.out.println(p);
        }
    }

    public int findMultipleCitation() {
        Collection<Paper> paperList = papers.values();
        int count = 0;
        for (Paper p : paperList) {
            Collection<Paper> citations = p.getInCitation();
            if (citations.size() > 1) {
                count += citations.size() - 1;
                System.out.println("===== " + p.getTitle() + " cited by =====\n");
                for (Paper citation : citations) {
                    System.out.println(citation);
                }
                System.out.println("======================");
            }
        }
        return count;
    }

    public int findInOutCitation() {
        Collection<Paper> paperList = papers.values();
        int count = 0;
        for (Paper paper : paperList) {
            Collection<Paper> inCitations = paper.getInCitation();
            Collection<Paper> outCitations = paper.getOutCitation();
            if (inCitations.size() > 0 && outCitations.size() > 0) {
                count++;
                System.out.print(paper);
                System.out.println("======== IN CITATION ========");
                for (Paper p : paper.getInCitation()) {
                    System.out.println(p);
                }
                System.out.println("========================\n");
            }
        }
        return count;
    }

    public void paperDetails(String paperName) {
        Paper paper = papers.get(paperName);
        if (paper == null) {
            System.out.println("Paper not found");
            return;
        }

        System.out.println("======== IN CITATION ========");
        for (Paper p : paper.getInCitation()) {
            System.out.println(p);
        }
        System.out.println("======== OUT CITATION ========");
        for (Paper p : paper.getOutCitation()) {
            System.out.println(p);
        }
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
