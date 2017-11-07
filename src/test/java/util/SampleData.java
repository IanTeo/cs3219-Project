package util;

import model.Paper;

public class SamplePaper {
    public static final Paper PAPER_1 = new Paper.PaperBuilder()
            .withId("P1")
            .withTitle("venue ICSE with 3 authors 1 2 3")
            .withAuthors({ "A1", "A2", "A3" })
            .build();
}
