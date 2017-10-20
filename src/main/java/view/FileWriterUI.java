package view;

import logic.Controller;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.PrintWriter;
import java.util.Scanner;

public class FileWriterUI implements UserInterface {
    private Controller controller;

    public FileWriterUI(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Loading data in JSON folder");
        System.out.println(controller.executeQuery("load json"));
        String input = sc.nextLine();
        while (!input.equals("0")) {
            int fileNameEndIndex = input.indexOf(" ");
            String fileName = input.substring(0, fileNameEndIndex).trim();
            String command = input.substring(fileNameEndIndex).trim();
            outputToFile(fileName, controller.executeQuery(command));
            System.out.println("Output successful");
            input = sc.nextLine();
        }
    }

    private void outputToFile(String fileName, String text) {
        try {
            PrintWriter out = new PrintWriter(fileName);
            out.println(text);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Unable to write file: " + fileName);
        }
    }
}
