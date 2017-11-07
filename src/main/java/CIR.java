import logic.Controller;
import model.Model;
import view.HttpUI;
import view.UserInterface;
import model.ModelManager;

public class CIR {

    public void run() {
        System.out.println("Initializing..");
        Model model = new ModelManager();
        Controller controller = new Controller(model);
        UserInterface ui = new HttpUI(controller);
        ui.start();
    }

    public static void main(String[] args) {
        CIR cir = new CIR();
        cir.run();
    }


}
