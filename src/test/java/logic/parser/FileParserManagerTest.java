package logic.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

import model.Model;
import org.junit.Test;

import model.Author;
import model.ModelManager;
import model.Paper;
import util.AssertUtil;
import util.PaperBuilder;

public class FileParserManagerTest {
    private final Model model = new ModelManager();
    private final FileParserManager parser = new FileParserManager(model);
    private final String directory = "src/test/res/ParserTest/";

    private static final ModelManager EXPECTED_MODEL = new ModelManager();

    private static final Author AUTHOR_ONE = new Author("1", "Alice A. Alex");
    private static final Author AUTHOR_TWO = new Author("Alice A. Alex");
    private static final Author AUTHOR_THREE = new Author("2", "Benny Bob");
    private static final Author AUTHOR_FOUR = new Author("3", "Charlie Cassell");
    private static final Author AUTHOR_FIVE = new Author("4", "Elephante");

    private static final Paper PAPER_ONE = new PaperBuilder().withTitle("Overloading").withVenue("Inf. Lett.")
            .withYear(2001).withId("1").withAuthors(new Author[]{AUTHOR_ONE}).build();
    private static final Paper PAPER_TWO = new PaperBuilder().withTitle("Gluodynamics")
            .withYear(1997).withId("2").withAuthors(new Author[]{AUTHOR_TWO}).build();
    private static final Paper PAPER_THREE = new PaperBuilder().withTitle("Convergence Processes")
            .withVenue("ICMI").withYear(2015).withId("3").withAuthors(new Author[]{AUTHOR_THREE, AUTHOR_FOUR}).build();
    private static final Paper PAPER_FOUR = new PaperBuilder().withTitle("Solder fume")
            .withVenue("Annals").withId("4").build();
    private static final Paper PAPER_FIVE = new PaperBuilder().withTitle("Kimberlite").withVenue("PloS one")
            .withYear(2012).withId("5").withAuthors(new Author[]{AUTHOR_FIVE, AUTHOR_ONE}).build();

    static {
        AUTHOR_ONE.addPaper(PAPER_ONE);
        AUTHOR_ONE.addPaper(PAPER_FIVE);
        AUTHOR_TWO.addPaper(PAPER_TWO);
        AUTHOR_THREE.addPaper(PAPER_THREE);
        AUTHOR_FOUR.addPaper(PAPER_THREE);
        AUTHOR_FIVE.addPaper(PAPER_FIVE);

        EXPECTED_MODEL.addAuthor(AUTHOR_ONE);
        EXPECTED_MODEL.addAuthor(AUTHOR_TWO);
        EXPECTED_MODEL.addAuthor(AUTHOR_FOUR);
        EXPECTED_MODEL.addAuthor(AUTHOR_THREE);
        EXPECTED_MODEL.addAuthor(AUTHOR_FIVE);

        EXPECTED_MODEL.addPaper(PAPER_ONE);
        EXPECTED_MODEL.addPaper(PAPER_TWO);
        EXPECTED_MODEL.addPaper(PAPER_THREE);
        EXPECTED_MODEL.addPaper(PAPER_FOUR);
        EXPECTED_MODEL.addPaper(PAPER_FIVE);

        PAPER_TWO.addCitation(PAPER_ONE);
        PAPER_THREE.addCitation(PAPER_ONE);
        PAPER_ONE.addCitation(PAPER_FOUR);
    }

    @Test
    public void parseFilesInDirectory() {
        parser.parseFilesInDirectory(directory);
        assertModelEquals(EXPECTED_MODEL, model);
    }

    private void assertModelEquals(Model expected, Model actual) {
        assertEquals(expected.getPapers().size(), actual.getPapers().size());
        for (Paper paper : expected.getPapers()) {
            Paper toCompare = actual.getPaper(paper.getId());
            AssertUtil.assertPaperEquals(paper, toCompare);
        }

        assertEquals(expected.getAuthors().size(), actual.getAuthors().size());
        for (Author author : expected.getAuthors()) {
            Author toCompare = actual.getAuthor(author.getId());
            AssertUtil.assertAuthorEquals(author, toCompare);
        }
    }
}
