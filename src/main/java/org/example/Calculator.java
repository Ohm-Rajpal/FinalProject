package org.example;

import java.util.Scanner;
import org.apache.commons.math3.stat.inference.TestUtils;

public class Calculator {



    private String message; // Input message by the user to determine which chi square to use
    private boolean isGoodnessOfFit;
    private boolean isHomogeneity;
    private boolean isIndependence;
    private double level; // significance level

    private double n; // sample size
    private double chiSquare; // the chi-square statistic
    private double pVal; // p-value of the chi-square

    private long[][] userMatrix2D; // this will be the 2D array of the user input
    private double[][] expectedValueMatrix2D; // this will be the expected values matrix
    private long[] userMatrix1D; // this will be the 1D array of the user input
    private double[] expectedValueMatrix1D; // this will be the expected value
    private double[] dist; // distribution of categorical variables for GOF



    public Calculator(){
        message = "NOTHING";
        isGoodnessOfFit = false;
        isHomogeneity = false;
        isIndependence = false;
        level = 0.05;
        n = 0;
        chiSquare = 0.0;
    }

    public Calculator(String message, double level) {
        this.message = message;
        isGoodnessOfFit = false;
        isHomogeneity = false;
        isIndependence = false;
        this.level = level;
        n = 0;
        chiSquare = 0.0;
    }


    /*
     * Name: processQuestion
     * Purpose: If the user enters a string, this method will figure out if
     * the problem is a chi square GOF, homogeneity, or independence
     * @return void
     */

    // THIS MIGHT BE TOO HARD TO IMPLEMENT
    public void processQuestion(String msg, Scanner in) {
        // if the user did not enter a message
        if (msg.equals("NOTHING")) {
            figureOutOnOwn(in);
        }
        else { // This means that there is a message
        // this part here can be converted into a method
            // USE REGEX TO SPLIT THE MSG AND THEN TRY TO AUTOFILL VALUES

            // This is if there is an association
            System.out.println("Is your input a 1D array or 2D array? (1D/2D)");
            String choiceInput = in.nextLine();

            /*
             * If the input contains a 2D array, then it will execute these specific steps
             */

            if (choiceInput.contains("2") || choiceInput.toLowerCase().contains("two")) {
                if (msg.toLowerCase().contains("association") || msg.toLowerCase().contains("dependent") ||
                        msg.toLowerCase().contains("relationship"))
                {

                    isIndependence = true;
                }

                else if (msg.toLowerCase().contains("homogeneity") ||
                            msg.toLowerCase().contains("distribution") ||
                            msg.toLowerCase().contains("categorical"))
                {
                    isHomogeneity = true;
                }

                System.out.println("");
           }
           else { // There is only one variable and it's GOF test

            }
        }

    }


    /*
     * Name: figureOutOnOwn
     * Purpose: If the user was too lazy to copy and paste the word problem,
     * this code will help the user find out which test or interval to use
     * @return void
     */
    public void figureOutOnOwn(Scanner in){
        System.out.println("Looks like we need to get more information");
        System.out.println("Does the problem have a 2 dimensional array or no: ");
        String check = in.nextLine();
        if (check.toLowerCase().contains("y")) {
            check = "yes";
        } else {
            check = "no";
        }

        if (check.equals("yes")) {
            System.out.println("Your problem seems to be either a test for homogeneity or independence");
            System.out.println("Does your problem have any of the following words? Association, dependence, relationship, etc?");
            check = in.nextLine();
            if (check.toLowerCase().contains("y")) {
                check = "independence";
                isIndependence = true;
            } else {
                check = "homogeneity";
                isHomogeneity = true;
            }
        } else {
            check = "goodness of fit";
            isGoodnessOfFit = true;
        }
        System.out.println("This problem is most likely a " + check);

        /**
         * We will now collect the necessary information
         */
        //  Scanner.nextInt method does not read the newline character in your input created by hitting "Enter,"
        //  and so the call to Scanner.nextLine returns after reading that newline.

        System.out.print("Now input the total sample size n: ");
        n = in.nextInt();
        in.nextLine();
        System.out.print("Is the population large (greater than 10 times the sample size)?");
        String s = in.nextLine();
        boolean isLarge = false;
        if (s.toLowerCase().contains("y")) {
            isLarge = true;
        }

        System.out.print("Is the sample randomized?");
        String r = in.nextLine();
        boolean isRandom = false;
        if (s.toLowerCase().contains("y")) {
            isRandom = true;
        }

        // GOF TEST
        if (isGoodnessOfFit) { // if it is a GOF
            GOFTest(isLarge, isRandom, in);
        } // PROGRAM ENDS HERE
        else if (isHomogeneity) {
            System.out.println("The test for homogeneity can be applied. Now this calculator requires more info");
            twoVarStats(isLarge, isRandom, in);
        }
        else {
            System.out.println("The test for independence can be applied. Now this calculator requires more info");
            twoVarStats(isLarge, isRandom, in);
        }

    }

    public void twoVarStats(boolean tenPercentCondition, boolean random, Scanner in) {
        if (!tenPercentCondition || !random) {
            sorryMessage();
        } else { // this means the ten percent condition is met

            /**
             * Now comes the difficult part. I will have to take the input of a 2D array
             */
            System.out.print("How many rows in your summary table?: ");
            int row = in.nextInt();
            in.nextLine();
            System.out.print("How many columns in your summary table?: ");
            int col = in.nextInt();

            userMatrix2D = new long[row][col];
            expectedValueMatrix2D = new double[row][col];


            // this gets all the values for the 2D array
            for (int r = 0; r < row; r++) {
                System.out.print("Enter all the values in row " + (r + 1) + ": ");
                for (int c = 0; c < col; c++) {
                    userMatrix2D[r][c] = in.nextInt();
                }
                in.nextLine();
            }
            // expected counts not even needed
            chiSquare = TestUtils.chiSquare(userMatrix2D);
            pVal = TestUtils.chiSquareTest(userMatrix2D);

            // calculate expected values method
            // a sum col and row method will be needed
            // total sum will be needed

            System.out.println("The chisquare value is: " + chiSquare);
            System.out.println("The calculated P-value is: " + pVal);
            System.out.println(summary());  // solution achieved
        }
    }

    public void GOFTest(boolean tenPercentCondition, boolean random, Scanner in) {

        if (!tenPercentCondition || !random) {
            sorryMessage();
        } else { // this means that ten percent condition is met
            System.out.println("The goodness of fit test can be applied. Now this calculator requires more info");
            System.out.println("How many entries?: ");
            int entries = in.nextInt();
            // ERROR CHECK THIS @THEO
            userMatrix1D = new long[entries];
            expectedValueMatrix1D = new double[entries];
            dist = new double[entries];

            System.out.print("Enter all the values in the array: ");
            for (int i = 0; i < entries; i++) {
                userMatrix1D[i] = in.nextInt(); // this initializes the userMatrix1D array
            }

            in.nextLine();

            System.out.print("Great! Now we need your distribution of categorical variables: ");
            for (int i = 0; i < entries; i++) {
                dist[i] = in.nextDouble(); // this initializes the 1D expected values array
            }

            in.nextLine();

            // MODIFYING OUTPUT PART IN THE RUBRIC. THIS EARNS THE POINT
            for (int i = 0; i < entries; i++) {
                expectedValueMatrix1D[i] = dist[i] * n;
            }

            chiSquare = TestUtils.chiSquare(expectedValueMatrix1D, userMatrix1D);
            pVal = TestUtils.chiSquareTest(expectedValueMatrix1D, userMatrix1D);

            System.out.println("The chisquare value is: " + chiSquare);
            System.out.println("The calculated P-value is: "+ pVal);
            System.out.println(summary());  // solution achieved
        }
    }



    public void sorryMessage() {
        System.out.println("I'm sorry, the conditions for this test are not met");
    }

    public String summary() {

        String str = "";

        if (pVal > level) {
            str += "Since P-value > alpha, fail to reject the null hypothesis. \nWe do not have convincing" +
                         " evidence that ";
        } else {
            str = "Since P-value < alpha, reject the null hypothesis. \nWe have convincing" +
                         " evidence that ";
        }

        if (isGoodnessOfFit) {
            str += "the stated distribution of a categorical variable from the population of \ninterest" +
                    " is incorrect.";// ADD THE CORRECT CONCLUSION HERE FOR EVERY ONE OF THE THREE CASES
        }
        else if (isHomogeneity) {
            str += "there is a difference for a categorical variable across several populations.";
        }
        else {
            str += "the two variables are dependent.";
        }
        return str;
    }

    public double chiSquareVal() {
        return chiSquare;
    }

    public double getpVal() {
        return pVal;
    }

}