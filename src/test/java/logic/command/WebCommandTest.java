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
    private Model model = new ModelStub();
    private static final String BASE_URL = "WebCommandTest/%s";

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

    @Test(expected = ParseException.class)
    public void setParameter_invalidLevelArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("level", "invalid");
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

        String actual = command.execute();
        String expected = String.format(command.HELP, "Paper not found");
        assertEquals(expected, actual);
    }

    @Test
    public void execute_validLevel4_printJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("level", "4");
        paramMap.put("paper", "P3");

        WebCommand command = new WebCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = FileReader.readFile(String.format(BASE_URL, "Web_ValidLevel4TestResult.json"));
        assertEquals(expected, actual);
    }

    @Test
    public void execute_validLevel2_printJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("level", "2");
        paramMap.put("paper", "P3");

        WebCommand command = new WebCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = FileReader.readFile(String.format(BASE_URL, "Web_ValidLevel2TestResult.json"));
        assertEquals(expected, actual);
    }
}
