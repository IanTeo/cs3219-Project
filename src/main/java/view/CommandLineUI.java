package view;

import logic.Controller;
import model.Model;

import java.util.Scanner;

public class CommandLineUI implements UserInterface {
    private Controller controller;

    public CommandLineUI(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome!");
        String input = sc.nextLine();
        while (!input.equals("0")) {
            System.out.println(controller.executeQuery(input));
            input = sc.nextLine();
        }
    }
}
