package org.example;

import java.util.Scanner;
import org.apache.commons.math3.stat.inference.TestUtils;

public class Calculator {


    /**
     * Identifying which test to use
     */
    private boolean isGoodnessOfFit;
    private boolean isHomogeneity;
    private boolean isIndependence;

    /**
     * Information needed to calculate statistics
     */
    private double level; // significance level
    private double n; // sample size
    private int df; // degrees of freedom
    private double chiSquare; // the chi-square test statistic
    private double pVal; // p-value of the chi-square

    /**
     * These are the array variables
     */
    private long[][] userMatrix2D; // this will be the 2D array of the user input
    private double[][] expectedValueMatrix2D; // this will be the expected values matrix
    private long[] userMatrix1D; // this will be the 1D array of the user input
    private double[] expectedValueMatrix1D; // this will be the expected value
    private double[] dist; // distribution of categorical variables for GOF



    public Calculator(){
        isGoodnessOfFit = false;
        isHomogeneity = false;
        isIndependence = false;
        level = 0.05;
        n = 0;
        df = 0;
        chiSquare = 0.0;
        pVal = 0.0;
    }


    /**
     * Method Name: solver
     * Purpose: Lets the user enter information
     * and then this code checks if the conditions are met
     * and then does statistical tests
     * return void
     **/
    public void solver(Scanner in){
        System.out.println("Welcome to the stats HW solver!");
        System.out.println("Does the problem have a 2 dimensional array or no: ");
        String check = in.nextLine();
        if (check.toLowerCase().contains("y")) {
            check = "yes";
        } else {
            check = "no";
        }

        if (check.equals("yes")) {
            System.out.print("Your problem seems to be either a test for homogeneity or independence");
            System.out.print("Does your problem have any of the following words? Association, dependence, relationship, etc?");
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

        /*
         * We will now collect the necessary information
         */
        //  Scanner.nextInt method does not read the newline character in your input created by hitting "Enter,"
        //  and so the call to Scanner.nextLine returns after reading that newline.

        System.out.print("Now input the total sample size n: ");
        n = in.nextInt();
        in.nextLine();
        System.out.print("Is the population large (greater than 10 times the sample size)?");
        String s = in.nextLine();
        boolean isLarge = s.toLowerCase().contains("y");

        System.out.print("Is the sample randomized?");
        String r = in.nextLine();
        boolean isRandom = r.toLowerCase().contains("y");

        // GOF TEST
        if (isGoodnessOfFit) { // if it is a GOF
            GOFTest(isLarge, isRandom, in);
        } // PROGRAM ENDS HERE
        else if (isHomogeneity) {
            System.out.println("The test for homogeneity can be applied. Now this calculator requires more info");
        }
        else {
            System.out.println("The test for independence can be applied. Now this calculator requires more info");
        }
        twoVarStats(isLarge, isRandom, in);
    }


    /**
     * Method Name: twoVarStats
     * Purpose: Lets the user enter boolean values for the
     * ten percent condition and random variables.
     * This code will check if the test can be applied for
     * homogeneity or independence
     * return void
     **/
    public void twoVarStats(boolean tenPercentCondition, boolean random, Scanner in) {
        if (!tenPercentCondition || !random) {
            sorryMessage();
        } else { // this means the ten percent condition is met

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

            System.out.println("The chi-square value is: " + chiSquare);
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

            setExpectedValueMatrix1D(entries);

            if (verifyExpectedCounts1D(entries)) {
                System.out.println("Each value in the expected value is greater than or equal to 5");
                System.out.println("This means that the expected counts passed");
                System.out.println("The 1D array below is the expected counts: ");
                displayExpectedCounts1D();

                chiSquare = TestUtils.chiSquare(expectedValueMatrix1D, userMatrix1D);
                pVal = TestUtils.chiSquareTest(expectedValueMatrix1D, userMatrix1D);

                System.out.println("The chi-square value is: " + chiSquare);
                System.out.println("The degrees of freedom is: " + calculateDF(entries));
                System.out.println("The calculated P-value is: "+ pVal);
                System.out.println(summary());  // solution achieved
            }
            else {
                System.out.println("The expected counts condition was not met.");
                System.out.println("The GOF test cannot be applied.");
            }
        }
    }



    /**
     * Method Name: setExpectedValueMatrix1D
     * Purpose: Sets the expected value matrix
     * for the 1D array. If all the values are
     * greater than or equal to 5, then return true.
     * Else, the conditions have not been met and
     * return false
     * return boolean
     **/
    public void setExpectedValueMatrix1D(int entries) {
        for (int i = 0; i < entries; i++) {
            expectedValueMatrix1D[i] = dist[i] * n;
        }
    }

    public boolean verifyExpectedCounts1D(int entries) {
        for (int i = 0; i < entries; i++) {
            if (expectedValueMatrix1D[i] < 5) return false;
        }
        return true;
    }

    public void displayExpectedCounts1D() {
        for (double v : expectedValueMatrix1D) System.out.print(v + " ");
        System.out.println();
    }

    /**
     * Method Name: setExpectedValueMatrix2D
     * Purpose: Sets the expected value matrix
     * for the 2D array
     **/
    public void setExpectedValueMatrix2D() {
        for (int r = 0; r < expectedValueMatrix2D.length; r++) {
            for (int c = 0; c < expectedValueMatrix2D[0].length; c++) {
                calcIndivVal(r, c, calcRow(r), calcCol(c));
            }
        }
    }

    /**
     * Helper method for setExpectedValueMatrix2D
     * @return sum of row
     */
    public double calcRow(int row) {
        double sum = 0.0;

        // Row major traversal
        for (int i = 0; i < expectedValueMatrix2D.length; i++) {
            sum += expectedValueMatrix2D[row][i];
        }
        return sum;
    }

    /**
     * Helper method for setExpectedValueMatrix2D
     * @return sum of col
     */
    public double calcCol(int col) {
        double sum = 0.0;

        // Column major traversal
        for (int i = 0; i < expectedValueMatrix2D[0].length; i++) {
            sum += expectedValueMatrix2D[i][col];
        }
        return sum;
    }

    /**
     * Helper method for setExpectedValueMatrix2D
     * Sets the expected value given the row index and col index.
     * Then takes the row sum and col sum and divides it by the total
     * to get the expected count value.
     */
    public void calcIndivVal(int row, int col, double rowSum, double colSum) {
        expectedValueMatrix2D[row][col] = ((rowSum) * (colSum))/n;
    }

    /*
    public static double[][] getExpectedValues(double[][] observed_values) {
        int num_rows = observed_values.length;
        int num_cols = observed_values[0].length;
        double[][] expected_values = new double[num_rows][num_cols];
        double row_total, col_total, grand_total;


        grand_total = 0.0;
        for (int i = 0; i < num_rows; i++) {
            for (int j = 0; j < num_cols; j++) {
                grand_total += observed_values[i][j];
            }
        }

        for (int i = 0; i < num_rows; i++) {
            row_total = 0.0;
            for (int j = 0; j < num_cols; j++) {
                row_total += observed_values[i][j];
            }
            for (int j = 0; j < num_cols; j++) {
                col_total = 0.0;
                for (int k = 0; k < num_rows; k++) {
                    col_total += observed_values[k][j];
                }
                expected_values[i][j] = (row_total * col_total) / grand_total;
            }
        }

        return expected_values;
    }
    */

    /**
     * Method Name: sorryMessage()
     * Purpose: Outputs sorry when the conditions for
     * the test have not been met
     * return void
     **/
    public void sorryMessage() {
        System.out.println("I'm sorry, the conditions for this test are not met");
    }

    public double calculateDF(int categories) {
        return categories - 1;
    }

    public double calculateDF(int rows, int cols) {
        return (rows - 1) * (cols - 1);
    }

    /**
     * Method Name: summary()
     * Purpose: Gives a brief summary of
     * @return String
     **/
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


}