package logic.command;

import static org.junit.Assert.assertEquals;

import logic.exception.ParseException;
import model.Model;
import org.junit.Before;
import org.junit.Test;

import model.ModelManager;
import model.Paper;
import util.FileReader;
import util.ModelStub;
import util.PaperBuilder;

import java.util.HashMap;
import java.util.Map;

public class WordCommandTest {
    private static final String BASE_URL = "WordCommandTest/%s";

    private Model model;

    @Before
    public void init() {
        model = new ModelStub();
    }
    @Test(expected = ParseException.class)
    public void setParameter_noCategoryArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();

        WordCommand command = new WordCommand();
        command.setParameters(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void setParameter_invalidCategoryArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("category", "invalid");

        WordCommand command = new WordCommand();
        command.setParameters(model, paramMap);
    }

    @Test
    public void execute_validCategoryPaper_printJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("category", "paper");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidPaperResult.json"));

        paramMap.put("ignore", "VENUE");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidPaperWithFilterResult.json"));
    }

    @Test
    public void execute_validCategoryVenue_printJson() throws Exception {
        // Paper with no venue should not affect result when category=VENUE
        model.addPaper(new PaperBuilder().withId("t1").build());

        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("category", "venue");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidVenueResult.json"));

        paramMap.put("ignore", "arXiv");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidVenueWithFilterResult.json"));
    }

    @Test
    public void execute_validCategoryAuthor_printJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("category", "author");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidAuthorResult.json"));

        paramMap.put("ignore", "author,PaperS");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidAuthorWithFilterResult.json"));
    }

    @Test
    public void execute_manyWords_printJson() throws Exception {
        model.addPaper(new PaperBuilder().withId("t1").withTitle("this is a test title with many different words").build());
        model.addPaper(new PaperBuilder().withId("t2").withTitle("A new minimally invasive technique for pudendal nerve stimulation.").build());
        model.addPaper(new PaperBuilder().withId("t3").withTitle("Systems Biology of Endothelial Mechano-activated Pathways Libraries Systems Biology of the Endothelial Mechano-activated Pathways").build());
        model.addPaper(new PaperBuilder().withId("t4").withTitle("Model-based estimation of male urethral resistance and elasticity using pressure-flow data").build());
        model.addPaper(new PaperBuilder().withId("t5").withTitle("Issn 2347-9523 (print) a New Non-monotone Self-adaptive Trust Region Method for Unconstrained Optimization").build());
        model.addPaper(new PaperBuilder().withId("t6").withTitle("Managing Road Maintenance Using Geographic Information System Application").build());
        model.addPaper(new PaperBuilder().withId("t7").withTitle("Visualizing the actin cytoskeleton in living plant cells using a photo-convertible mEos::FABD-mTn fluorescent fusion protein").build());
        model.addPaper(new PaperBuilder().withId("t8").withTitle("Comparative effectiveness of using computed tomography alone to exclude cervical spine injuries in obtunded or intubated patients: meta-analysis of 14,327 patients with blunt trauma").build());
        model.addPaper(new PaperBuilder().withId("t9").withTitle("Triplesets: Tagging and Grouping in Rdf Datasets 1 Named Graphs – the Current State of Affairs").build());
        model.addPaper(new PaperBuilder().withId("t10").withTitle("LEMONS - A Tool for the Identification of Splice Junctions in Transcriptomes of Organisms Lacking Reference Genomes").build());
        model.addPaper(new PaperBuilder().withId("t11").withTitle("A Linear Dynamic Model for Microgrid Voltages in Presence of Distributed Generation").build());

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("category", "paper");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidPaperLargeResult.json"));
    }

    private void assertCommand(Map<String, String> paramMap, String expectedOutputFileName) throws Exception {
        WordCommand command = new WordCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = FileReader.readFile(expectedOutputFileName);
        assertEquals(expected, actual);
    }
}
