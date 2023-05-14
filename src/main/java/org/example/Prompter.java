package org.example;

import java.io.PrintStream;
import java.util.Scanner;

public class Prompter {
    Scanner input;

    PrintStream out;

    public Prompter(Scanner input, PrintStream out) {
        this.input = input;
        this.out = out;
    }

    public void print(String message) {
        out.println(message);
    }

    public boolean prompt(String message) {
        out.println(message);
        return input.nextLine().contains("y");
    }
}
