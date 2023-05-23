package org.example;

/**
TODAY'S TASK 5/22/23

1. Theo will fix the chatbot run method
2. Ohm will fix the chatbot array methods
3. Ohm will also transfer a couple of ChatBot methods to Calculator
4. Ohm will fix the condition method

*/


/**
     * Task for ChatBot:
     * Your job is to simply handle the information entered by the user. The purpose
     * of this bot is to guide the user to the correct answer. The user will be prompted by the bot
     * with some questions. The bot will then check the responses and verify that they are correct based
     * on the user's responses. If the human gets it right, the bot will move on. If the human gets it wrong, the
     * bot will give the right answer, explain it (if needed), and then move on. The following is what I would
     * like for you to implement
     *
     * @THEO
     * 1. Welcome the user:
     *
     *      - Get their name
     *      - Ask them what they need help in
     *      - Based on the response, you can either tell them that you cannot help them or that you can but only for
     *      - statistics involving chi-square tests
     *      - Get their response and either proceed or end the program
     *
     *      // Extra if you have time
     *      ***- If you are feeling lucky/have extra time, incorporate
     *      natural language processing to find out how the user is feeling (refer to ArrayList project
     *      we did in class), but please do this after you have finished the entirety of this project
     *
     * @THEO
     * 2. Determine which test to use
     *
     *      // BOT PROCESSING
     *      - First, ask them if it is 2D array or 1D array
     *      - Next, ask them to enter the word problem
     *      - If it is a 2D array
     *          - If the string entered contains these words
     *                  .contains("association")
     *                  .contains("independence")
     *                  .contains("relationship")
     *
     *          -> Then the test is independence
     *          -> Else, it is homogeneity
     *      - else it has to be a 1D array and is a goodness of fit test
     *
     *      // HUMAN INTERACTION
     *      - Ask the user what type of test do they think it is
     *      - If that test matches what the bot thought it was, congratulate them
     *      - and move onto the next step
     *      - If they got it wrong, tell them why it was test the bot thought was right
     *          if bot thought independence: say it contained words such as association, independence, or relationship
     *          if bot thought homogeneity: say it didn't contain words association, independence, or relationship and
     *          had a 2D array, therefore homogeneity
     *          if bot thought goodness of fit: say it was 1D array, so it had to be GOF test
     *      - Move on anyways
     *
     * 3. Determine if conditions were passed
     *
     * @THEO
     * // The conditions vary slightly for each one. Just type exactly what I have written here
     * // Do not try to compare the user's response with this
     *
     *  print out "These are you null and alternative hypothesis"
     *
     * if (GOF) print
     *  H_0: The stated distribution of a categorical variable from the population of interest is correct
     *  H_a: The stated distribution of a categorical variable from the population of interest is incorrect
     *
     * if (homogeneity) print
     * H_0: There is no difference for a categorical variable across several populations
     * H_a: There is a difference for a categorical variable across several populations
     *
     * if (independence) print
     * H_0: The two variables are independent
     * H_a: The two variables are dependent
     *
     * @OHM
     * // Create a method that will take input for 1D or 2D array and do some calculations behind
     * the scenes for expected values
     * // The leave this to me, it's a little tricky to do if you don't understand what's going on completely
     *
     * // I'll implement the compare function as well
     *
     *
     * 4. Compare the chi-square value, df, and P-value
     *
     * Based on the information given in the previous steps, the bot would have already calculated all of these
     * statistics using chi-square methods. I have pre-made methods for this ste
     *
     *      - I want you to prompt the user for their
     *          - chi-square:
     *          - df:
     *          - P-value:
     *      - If it is equal to the bot calculated values, congratulate them and they sucessfully solved the problem
     *      - If not, give the correct bot solution and then tell them try again by re-running the program
     *
     * // end of task
     */


import org.apache.commons.math3.stat.inference.TestUtils;

import java.sql.SQLOutput;
import java.util.Scanner;
import java.lang.Math.*;
import java.util.Random;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/* https://github.com/OhmRajpal/FinalProject/blob/master/src/main/java/org/example/Calculator.java
 */
public class ChatBot{

    Scanner in;
    //some vocab or different sentences for our chatbot so it doesn't say the same thing every time. you can edit the contents of these arrays at any time
    private String[] words = {"Perfect"};
    private String[] array = {"2D array it is!","Great,","Perfect,"};
    private String[] correct = {"Correct! You are one smart cookie.","That is correct!","You are very smart, that is correct!"};
    private String[] incorrect = {"Incorrect!","That is not right.","I was expecting better from you..."};


    // These are the user expected value arrays
    private double[] userExpectedValue1D;
    private double[][] userExpectedValue2D;

    boolean condition = false;
    boolean random = false;

    private Calculator calc;

    String ntest = "";

    public ChatBot() {
        clearScreen();
        in = new Scanner(System.in);
        calc = new Calculator();
    }

    public static void clearScreen(){


        System.out.flush();
    }

    public void run(){
      clearScreen();
        System.out.println("Hello, I am Bob. The GREATEST stats calculator.");
        String[] matches = { "math", "stats", "chi-square", "test", "homework", "statistics" };
        String wantsHelp = prompt("How can I help you?", "Sorry, I can only help with math", matches);
      processQuestion();
    }

    public String prompt(String message, String error, String[] keyword) {
        System.out.println(message);
        String input = in.nextLine();
        input = input.toLowerCase();

        boolean matches = false;
        for (String s : keyword) {
            if (input.contains(s)) {
                matches = true;
                break;
            }
        }
        if (matches)
            return input;
        else {
            System.out.println(error);
            return prompt(message, error, keyword);
        }
    }


    public boolean containWord(String message,String[] keyword) {
        System.out.println(message);
        String input = in.nextLine();
        input = input.toLowerCase();
        boolean matches = false;
        for (String s : keyword) {
            if (input.contains(s)) {
                matches = true;
                if(ntest.length()==0){
                    ntest+=s;
                }
                else{
                    ntest+= "and "+ s;
                }

            }
        }

        if (matches) {
            return true;
        }
        else {
            return false;
        }
    }

    //every method above process question is a helper method for process question
    public void processQuestion(){

        boolean conditionsPassed;

        String test = "unknown";
        String[] keyword = { "association", "independence", "relationship"};

        //asks user if problem includes 2D Array
        System.out.println("Does your problem include a 2D array?");
        String input = in.nextLine();
        input = input.toLowerCase();

        if(input.contains("yes")||input.contains("y")){
            int ran = (int) (Math.random()*array.length);
            //if yes then they enter the word problem
            System.out.println(array[ran] + "\nplease enter your word problem: ");
            String input1 = in.nextLine();
            //contain word method is called to check if the inputted text contains the keyword (association, independence, relationship) and return a boolean
            boolean check = containWord(input1, keyword);

            if(check){
                //if the text does contain the keyword then independence is set to true
                calc.setIndependence(true);
                test = "independence";
            }
            else{
                //else if the text does not contain the keywords than homogeneity is set to true
                calc.setHomogeneity(true);
                test = "homogeneity";
            }
        }
        else if(input.contains("no")||input.contains("n")){
            //if not a 2d array then it sets goodnessoffit to true in Calculator
            calc.setGoodnessOfFit(true);
            test = "goodness";
        }

        else{
            //if they dont respond yes or no then the method is called again so that the user can input a yes or no
            System.out.println("Your problem must include 2D array.");
            processQuestion();
        }

        System.out.println("What type of test do you think this is?");
        String answer = in.nextLine();
        
        if(answer.toLowerCase().contains(test)){ // the user gets it right

            int ran1 = (int)(Math.random()*correct.length);
            if (test.equals("goodness")) {
                System.out.println(correct[ran1] + " The test is a goodness of fit test!");
            }
            else {
                System.out.println(correct[ran1] + " The test is a "+test+"!");
            }
        }
        else{ // user gets it wrong
            int ran2 = (int)(Math.random()*correct.length);
            System.out.println(incorrect[ran2]+" The test is a " + test);
            if(test.equals("independence")){
                System.out.println("The test is an independence test because the keyword "+ ntest +" were included in the word problem and it has a 2D array.");
                calc.setGoodnessOfFit(true);
            }
            else if(test.equals("homogeneity")){
                System.out.println("The test is a test for homogeneity since it is a 2D array but does not contain any keyword");
                calc.setHomogeneity(true);
            }
            else{
                System.out.println("The test is a goodness of fit test as the problem only has a 1D array");
                calc.setIndependence(true);
            }
        }

        // This determines if 10% condition and randomness is true


        //conditionsPassed = condition();

        // THIS CHECKS THE CONDITIONS:
        System.out.print("What is your sample size: ");
        calc.setN(in.nextInt());
        in.nextLine();

        System.out.print("Is the population large greater than 10 times the sample size or for each category if homogeneity?: ");
        String s = in.nextLine();

        boolean condition = s.toLowerCase().contains("y");

        System.out.print("Is the sample randomized?: ");
        String r = in.nextLine();

        boolean random = r.toLowerCase().contains("y");
        //if the problems isn't randomized or the 10% condition is not met than it ends the program

        // if conditions passed, then you can apply the tests
        // else short circuit the program

        //calc.twoVarStats();

        // MODIFY THE GOF FUNCTION FOR THE CALC

        if (condition && random) { // this means the two conditions were passed

            if (calc.isGoodnessOfFit()) {
                calc.GOFTest(in);
                userExpectedValue1D = new double[calc.getUserMatrix1D().length];
                promptExpectedValues1D();

                //calc.setExpectedValueMatrix1D();
                // calculate the expected values

                // this means the expected values entered are the same
                if (calc.compareExpectedValues(userExpectedValue1D)) {
                    System.out.println(correct[(int)(Math.random()*correct.length)] + " Your 1D expected values are correct!");
                }
                else { // incorrect
                    System.out.println(incorrect[(int)(Math.random()*correct.length)]+ " I will display the " +
                            "correct 1D array answer below");
                    calc.displayExpectedCounts1D();
                }
                calc.calculateAll1D();
            }
            else {
                calc.twoVarStats(in);
                userExpectedValue2D = new double[calc.getUserMatrix2D().length][calc.getUserMatrix2D()[0].length];
                promptExpectedValues2D();

                if (calc.compareExpectedValues(userExpectedValue2D)) {
                    System.out.println(correct[(int)(Math.random()*correct.length)] + " Your 2D expected values are correct!");
                }
                else { // If the user gets it wrong
                    System.out.println(incorrect[(int)(Math.random()*correct.length)]+ " I will display the " +
                                       "correct 2D array answer below");
                    calc.displayExpectedCounts2D();
                }
                calc.calculateAll2D();
            }
            promptStatistics();
        }
        else { // The program cannot go on after this point. This is the ending spot of the code
            calc.sorryMessage();
        }



    } // end of process method


    //@OHM add Calculations here

    /**
     * Method Name: promptExpectedValues1D
     * Purpose: Prompt the user for their expected values
     */
    public void promptExpectedValues1D() {
        System.out.println("We will now compare your expected values with the bot");
        System.out.print("Enter your expected values: ");
        for (int i = 0; i < calc.getExpectedValueMatrix1D().length; i++) {
            userExpectedValue1D[i] = in.nextDouble();
        }
        in.nextLine();
    }

    /**
     * Method Name: promptExpectedValues2D
     * Purpose: Prompt the user for their expected values
     */
    public void promptExpectedValues2D() {

        System.out.println("We will now compare your expected values with the bot");

        for (int r = 0; r < calc.getExpectedValueMatrix2D().length; r++) {
            System.out.print("Enter values in row " + (r + 1) + ": ");
            for (int c = 0; c < calc.getExpectedValueMatrix2D()[0].length; c++) {
                userExpectedValue2D[r][c] = in.nextDouble();
            }
            in.nextLine();
        }
    }


    public void promptStatistics() {

        double chiSquare = 0.0;
        double degFree = 0.0;
        double pValue = 0.0;

        System.out.println("Now we need to check your final answers");
        System.out.print("What is your chi-square value?: ");
        chiSquare = in.nextDouble();
        in.nextLine();

        System.out.print("What is your degrees of freedom?: ");
        degFree = in.nextDouble();
        in.nextLine();

        System.out.print("What is your calculated P-value?: ");
        pValue = in.nextDouble();
        in.nextLine();

        if (compareStatistics(chiSquare, degFree, pValue)) {
            System.out.println(correct[(int) (Math.random() * correct.length)] + " Now we only have one question left");
        }
        else {
            System.out.println("That is incorrect. The correct values are: ");
            System.out.println("The chi-square value is: " + calc.getChiSquare());
            System.out.println("The degrees of freedom is: " + calc.getDf());
            System.out.println("The calculated P-value is: "+ calc.getpVal());
            // could be converted into a method
        }

        System.out.println("Select the correct choice: ");
        System.out.println("We reject the null hypothesis (1)");
        System.out.println("We fail to reject the null hypothesis (2)");
        System.out.print("Choice: ");
        int choice = in.nextInt();
        in.nextLine();

        if (pValue >= .05) { // fail to reject H_a
            if (choice == 2) {
                System.out.println("Congrats! You solved the problem!");
            }
            else {
                System.out.println("You fool. That is incorrect. This is not a display of mental prowess.");
            }
            System.out.println("Since P-value > alpha, fail to reject the null hypothesis. We do not have " +
                               "convincing evidence that the alternative hypothesis is true.");
        }
        else
        { // reject H_a
            if (choice == 1) {
                System.out.println("Congrats! You solved the problem!");
            }
            else {
                System.out.println("You fool. That is incorrect. This is not a display of mental prowess.");
            }
            System.out.println("Since P-value < alpha, reject the null hypothesis. We have " +
                               "convincing evidence that the alternative hypothesis is true.");
        }
    }

    public boolean compareStatistics(double chiSquare, double degFree, double pValue) {
        boolean param1 = (Math.abs(chiSquare - calc.getChiSquare()) < .01);
        boolean param2 = (Math.abs(degFree - calc.getDf()) < .01);
        boolean param3 = (Math.abs(pValue - calc.getpVal()) < .01);

        return (param1 && param2 && param3);
    }
}
