package logic.command;

import model.Model;

public interface Command {
    public String execute();

    public void setParameters(Model model, String arguments) throws Exception;
}
