package logic.command;

import java.util.Map;

import logic.exception.ParseException;
import model.Model;

public interface Command {
    public String execute();

    public void setParameters(Model model, Map<String, String> paramMap) throws ParseException;
}
