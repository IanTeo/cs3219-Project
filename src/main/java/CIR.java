import logic.Controller;
import view.CommandLineUI;
import view.UserInterface;
import model.Model;

public class CIR {

    public void run() {
        Model model = new Model();
        Controller controller = new Controller(model);
        UserInterface ui = new CommandLineUI(controller);
        ui.start();

        //model.paperDetails("Syntactic Transfer Using a Bilingual Lexicon");
        //model.paperDetails("Inter-coder agreement for computational linguistics.");
        //System.out.println("Number of papers cited multiple times: " + model.findMultipleCitation());
        model.findInOutCitation();
    }

    public static void main(String[] args) {
        CIR cir = new CIR();
        cir.run();
    }


}
