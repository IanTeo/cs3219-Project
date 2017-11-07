package logic.command;

import org.junit.Before;
import org.junit.Test;

import logic.exception.ParseException;
import model.Model;

import util.FileReader;
import util.ModelStub;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WebCommandTest {
    private Model model;

    @Before
    public void initModel() {
        model = new ModelStub();
    }

    @Test(expected = ParseException.class)
    public void setParameter_missingPaperArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("level", "4");

        WebCommand command = new WebCommand();
        command.setParameters(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void setParameter_missingLevelArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("paper", "P1");

        WebCommand command = new WebCommand();
        command.setParameters(model, paramMap);
    }

    @Test
    public void execute_paperNotFound_printHelp() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("level", "4");
        paramMap.put("paper", "invalid");

        WebCommand command = new WebCommand();
        command.setParameters(model, paramMap);

        String result = command.execute();
        String expected = String.format(command.HELP, "Paper not found");
        assertEquals(result, expected);
    }

    @Test
    public void execute_validArguments_printJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("level", "4");
        paramMap.put("paper", "P3");

        WebCommand command = new WebCommand();
        command.setParameters(model, paramMap);

        String result = command.execute();
        String expected = FileReader.readFile("Web_ValidTestResult.json");
        assertEquals(result, expected);
    }
}
