package org.example;

import java.util.Scanner;
import java.util.Arrays;

public class ChatBot {

    public void greet(Scanner in) {

        System.out.println("Hello, I'm a chat bot. What's your name?");
        String name = in.nextLine();

        System.out.println("Nice to meet you, " + name + "!");

        while (true) {
            System.out.print(name + ": ");
            String input = in.nextLine();
            String response = handleInput(input);
            System.out.println("ChatBot: " + response);
        }
    }

    private static String handleInput(String input) {
        // Split input into words
        String[] words = input.split("\\s+");

        // Check for statistical tests
        if (Arrays.asList(words).contains("chi-square")) {
            // Perform chi-square test and generate response
            double result = performChiSquareTest(words);
            if (result >= 0) {
                return "Yes, the chi-square test is appropriate for this data. The test statistic is " + result;
            } else {
                return "No, the chi-square test is not appropriate for this data.";
            }
        } else {
            // Generate a generic response if no statistical tests were found
            return "I'm sorry, I didn't understand your question.";
        }
    }

    private static double performChiSquareTest(String[] words) {
        // Extract data and expected frequencies from input
        // You'll need to implement this method to parse the input and extract the relevant data for the test
        // You may want to use an external library like Apache Commons Math to perform the test

        // Perform chi-square test
        // You'll need to implement this method to perform the chi-square test and return the test statistic

        // Return test statistic or -1 if the test is not appropriate for this data
        // You'll need to implement this method to determine whether the chi-square test is appropriate for the given data
        return -1;
    }

    /**
     * Task for ChatBot:
     *
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

}
