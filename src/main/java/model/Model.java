package model;

import java.util.Collection;
import java.util.HashMap;

public class Model {
    private HashMap<String, Paper> papers;
    private int numDataSet;

    public Model() {
        papers = new HashMap<>();
        numDataSet = 0;
    }

    public void addPaper(Paper paper) {
        if (papers.containsKey(paper.getTitle())) {
            papers.get(paper.getTitle()).updateMissingInformation(paper);
            return;
        }

        papers.put(paper.getTitle(), paper);
    }

    public Paper getPaper(String paperName) {
        return papers.get(paperName);
    }

    /**
     * Adds a citation to a paper. Both papers must have been added to the Model before using this method.
     * Nothing happens when at least one of the papers is not found or citation already exists
     */
    public void addCitation(String paperName, String citationName) {
        Paper paper = papers.get(paperName);
        Paper citation = papers.get(citationName);
        if (paper == null || citation == null) return;

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
}
