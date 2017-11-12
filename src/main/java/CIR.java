import logic.Controller;
import logic.parser.FileParser;
import logic.parser.JsonFileParser;
import model.Model;
import view.HttpUI;
import view.UserInterface;
import model.ModelManager;

public class CIR {

    public void run() {
        System.out.println("Initializing..");
        Model model = new ModelManager();
        FileParser fileParser = new JsonFileParser(model);
        Controller controller = new Controller(model, fileParser);
        UserInterface ui = new HttpUI(controller);
        ui.start();
    }

    public static void main(String[] args) {
        CIR cir = new CIR();
        cir.run();
    }


}
