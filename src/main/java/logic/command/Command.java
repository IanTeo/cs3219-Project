package logic.command;

import model.Model;

import java.util.Map;

public interface Command {
    public String execute();

    public void setParameters(Model model, Map<String, String> argumentMap) throws Exception;
}
