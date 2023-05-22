package org.example;

import java.util.Scanner;
public class FinalProject {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Calculator myCalc = new Calculator();
        ChatBot bot = new ChatBot();
        int choice = 0;

        System.out.println("Welcome to the stats chat bot or HW helper!");
        System.out.println("1. For chat bot");
        System.out.println("2. For HW solver");
        System.out.print("Enter selection: ");

        choice = input.nextInt();
        input.nextLine();

        if (choice == 1) {
            bot.run();
        }
        else {
            myCalc.solver(input);
        }
    }
}