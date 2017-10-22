package logic.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.Model;
import model.Paper;

public class VenueCommandTest {
    private final VenueCommand command = new VenueCommand();
    private final Model model = new Model();

    private static final Paper PAPER_ONE = new Paper.PaperBuilder().withVenue("Singapore").withId("1").build();
    private static final Paper PAPER_TWO = new Paper.PaperBuilder().withYear(2000).withId("2").build(); // missing venue
    private static final Paper PAPER_THREE = new Paper.PaperBuilder().withVenue("Malaysia").withId("3").build();
    private static final Paper PAPER_FOUR = new Paper.PaperBuilder().withVenue("Singapore").withYear(2005)
            .withTitle("Ian is a Scrub").withId("4").build();

    @Test
    public void test() throws Exception {
        model.addPaper(PAPER_ONE);
        model.addPaper(PAPER_TWO);
        model.addPaper(PAPER_THREE);
        model.addPaper(PAPER_FOUR);

        command.setParameters(model, "Malaysia");
        assertEquals("[{\"citationCount\":0,\"venue\":\"Malaysia\",\"year\":0,\"id\":\"3\",\"title\":\"\",\"authors\":\"\"}]",
                command.execute());

        command.setParameters(model, "Singapore");
        assertEquals("[{\"citationCount\":0,\"venue\":\"Singapore\",\"year\":0,\"id\":\"1\",\"title\":\"\",\"authors\":\"\"}," +
                "{\"citationCount\":0,\"venue\":\"Singapore\",\"year\":2005,\"id\":\"4\",\"title\":\"Ian is a Scrub\"," +
                "\"authors\":\"\"}]", command.execute());
    }
}
