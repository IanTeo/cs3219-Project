package util;

import model.Author;
import model.Paper;

public class SampleData {
    // Sample Authors
    public static final Author AUTHOR_1 = new AuthorBuilder()
            .withId("A1")
            .withName("author with papers P1 P2")
            .build();

    public static final Author AUTHOR_2 = new AuthorBuilder()
            .withId("A2")
            .withName("author with papers P2 P3 P4")
            .build();

    public static final Author AUTHOR_3 = new AuthorBuilder()
            .withId("A3")
            .withName("author with papers P1 P3")
            .build();

    // Sample Papers
    public static final Paper PAPER_1 = new PaperBuilder()
            .withId("P1")
            .withTitle("venue ICSE with authors A1 A3 cite P3")
            .withAuthors(AUTHOR_1, AUTHOR_3)
            .withVenue("ICSE")
            .withYear(2011)
            .build();

    public static final Paper PAPER_2 = new PaperBuilder()
            .withId("P2")
            .withTitle("venue ICSE with authors A1 A2 cite P1 P3")
            .withAuthors(AUTHOR_1, AUTHOR_2)
            .withVenue("P2@ICSE")
            .withYear(2011)
            .build();

    public static final Paper PAPER_3 = new PaperBuilder()
            .withId("P3")
            .withTitle("venue arXiv with authors A2 A3")
            .withAuthors(AUTHOR_2, AUTHOR_3)
            .withVenue("arXiv")
            .withYear(2009)
            .build();

    public static final Paper PAPER_4 = new PaperBuilder()
            .withId("P4")
            .withTitle("venue singapore with author A2 cite P2")
            .withAuthors(AUTHOR_2)
            .withVenue("singapore")
            .withYear(2015)
            .build();

    static {
        // Links from Author to Paper
        AUTHOR_1.addPaper(PAPER_1);
        AUTHOR_1.addPaper(PAPER_2);

        AUTHOR_2.addPaper(PAPER_2);
        AUTHOR_2.addPaper(PAPER_3);
        AUTHOR_2.addPaper(PAPER_4);

        AUTHOR_3.addPaper(PAPER_1);
        AUTHOR_3.addPaper(PAPER_3);

        // Links from Paper to Paper
        PAPER_1.addCitation(PAPER_3);
        PAPER_2.addCitation(PAPER_1);
        PAPER_2.addCitation(PAPER_3);
        PAPER_4.addCitation(PAPER_2);
    }
}
