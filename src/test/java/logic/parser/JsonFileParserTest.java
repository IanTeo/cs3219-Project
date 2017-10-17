package logic.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import model.Author;
import model.Model;
import model.Paper;

public class JsonFileParserTest {
    private final Model model = new Model();
    private final JsonFileParser parser = new JsonFileParser(model);
    private final File parser_test = new File("src/test/res/parser_test.json");

    private static final Model EXPECTED_MODEL = new Model();

    private static final Author AUTHOR_ONE = new Author("1", "alice a. alex");
    private static final Author AUTHOR_TWO = new Author("alice a. alex");
    private static final Author AUTHOR_THREE = new Author("2", "benny bob");
    private static final Author AUTHOR_FOUR = new Author("3", "charlie cassell");
    private static final Author AUTHOR_FIVE = new Author("4", "elephante");

    private static final Paper PAPER_ONE = new Paper.PaperBuilder().withTitle("Overloading").withVenue("Inf. Lett.")
            .withYear(2001).withId("1").withAuthors(new Author[]{AUTHOR_ONE}).build();
    private static final Paper PAPER_TWO = new Paper.PaperBuilder().withTitle("Gluodynamics")
            .withYear(1997).withId("2").withAuthors(new Author[]{AUTHOR_TWO}).build();
    private static final Paper PAPER_THREE = new Paper.PaperBuilder().withTitle("Convergence Processes")
            .withVenue("ICMI").withYear(2015).withId("3").withAuthors(new Author[]{AUTHOR_THREE, AUTHOR_FOUR}).build();
    private static final Paper PAPER_FOUR = new Paper.PaperBuilder().withTitle("Solder fume")
            .withVenue("Annals").withId("4").build();
    private static final Paper PAPER_FIVE = new Paper.PaperBuilder().withTitle("Kimberlite").withVenue("PloS one")
            .withYear(2012).withId("5").withAuthors(new Author[]{AUTHOR_FIVE, AUTHOR_ONE}).build();

    static {
        AUTHOR_ONE.addPaper(PAPER_FIVE);
        AUTHOR_ONE.addPaper(PAPER_ONE);
        AUTHOR_TWO.addPaper(PAPER_TWO);
        AUTHOR_THREE.addPaper(PAPER_THREE);
        AUTHOR_FOUR.addPaper(PAPER_THREE);
        AUTHOR_FIVE.addPaper(PAPER_FIVE);

        EXPECTED_MODEL.addAuthor(AUTHOR_ONE);
        EXPECTED_MODEL.addAuthor(AUTHOR_TWO);
        EXPECTED_MODEL.addAuthor(AUTHOR_THREE);
        EXPECTED_MODEL.addAuthor(AUTHOR_FOUR);
        EXPECTED_MODEL.addAuthor(AUTHOR_FIVE);

        EXPECTED_MODEL.addPaper(PAPER_ONE);
        EXPECTED_MODEL.addPaper(PAPER_TWO);
        EXPECTED_MODEL.addPaper(PAPER_THREE);
        EXPECTED_MODEL.addPaper(PAPER_FOUR);
        EXPECTED_MODEL.addPaper(PAPER_FIVE);

        EXPECTED_MODEL.addCitation(PAPER_TWO, PAPER_ONE);
        EXPECTED_MODEL.addCitation(PAPER_THREE, PAPER_ONE);
        EXPECTED_MODEL.addCitation(PAPER_ONE, PAPER_FOUR);
    }

    @Test
    public void parse() {
        parser.parse(parser_test);
        assertEquals(EXPECTED_MODEL, model);
    }
}
