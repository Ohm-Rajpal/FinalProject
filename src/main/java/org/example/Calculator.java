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
    private final double level; // significance level
    private double n; // sample size
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
        chiSquare = 0.0;
        pVal = 0.0;
    }

    /**
     * Method Name: solver
     * Purpose: Lets the user enter information
     * and then this code checks if the conditions are met
     * and then does statistical tests
     **/
    public void solver(Scanner in){
        System.out.println("Welcome to the stats HW solver!");
        System.out.print("Does the problem have a 2 dimensional array or no: ");
        String check = in.nextLine();
        if (check.toLowerCase().contains("y")) {
            check = "yes";
        } else {
            check = "no";
        }

        if (check.equals("yes")) {
            System.out.print("Your problem seems to be either a test for homogeneity or independence");
            System.out.print("Does your problem have any of the following words? Association, dependence, relationship, etc?: ");
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
        System.out.print("Is the population large greater than 10 times the sample size or for each category if homogeneity?: ");
        String s = in.nextLine();
        boolean isLarge = s.toLowerCase().contains("y");

        System.out.print("Is the sample randomized?: ");
        String r = in.nextLine();
        boolean isRandom = r.toLowerCase().contains("y");

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


    /**
     * Method Name: twoVarStats
     * Purpose: Verify whether the homogeneity or independence
     * test can be applied and then apply the test if it can
     * @param tenPercentCondition is true if n_i < .10N_i
     * or n < .10N depending on the problem
     * @param random is true if randomized
     * @param in scanner object
     */
    public void twoVarStats(boolean tenPercentCondition, boolean random, Scanner in) {
        if (!tenPercentCondition || !random) {
            sorryMessage();
        } else { // this means the ten percent condition is met

            System.out.print("How many rows in your summary table?: ");
            int row = in.nextInt();
            in.nextLine();
            System.out.print("How many columns in your summary table?: ");
            int col = in.nextInt();
            in.nextLine();

            userMatrix2D = new long[row][col];
            expectedValueMatrix2D = new double[row][col];

            // this gets all the values for the 2D array
            for (int r = 0; r < row; r++) {
                System.out.print("Enter all the values in row " + (r + 1) + ": ");
                for (int c = 0; c < col; c++) {
                    userMatrix2D[r][c] = in.nextInt();
                }
            }

            setExpectedValueMatrix2D(row, col); // sets the expected value 2D matrix

            if (verifyExpectedCounts2D()) {

                System.out.println("Each value in the expected value is greater than or equal to 5. This means that the expected counts passed.");
                System.out.println("The 2D array below is the expected counts: ");
                displayExpectedCounts2D();

                chiSquare = TestUtils.chiSquare(userMatrix2D);
                pVal = TestUtils.chiSquareTest(userMatrix2D);

                System.out.println("The chi-square value is: " + chiSquare);
                System.out.println("The degrees of freedom is: " + calculateDF(row, col));
                System.out.println("The calculated P-value is: " + pVal);
                System.out.println(summary());  // solution achieved
            }
            else {
                System.out.println("The expected counts condition was not met.");
                if (isHomogeneity) {
                    System.out.println("The test for homogeneity cannot be applied.");
                } else {
                    System.out.println("The test for independence cannot be applied.");
                }
            }
        }
    }

    /**
     * Method Name: GOFTest
     * Purpose: Verify whether the GOF test can be applied
     * and then apply the test if it can
     * @param tenPercentCondition is true when n < .10N
     * @param random is true when randomized
     * @param in is the Scanner object
     */
    public void GOFTest(boolean tenPercentCondition, boolean random, Scanner in) {

        if (!tenPercentCondition || !random) {
            sorryMessage();
        } else { // this means that ten percent condition is met
            System.out.println("The goodness of fit test can be applied. Now this calculator requires more info");
            System.out.print("How many entries?: ");
            int entries = in.nextInt();
            in.nextLine();

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

            setExpectedValueMatrix1D();

            if (verifyExpectedCounts1D()) {
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
     * for the 1D array.
     **/
    public void setExpectedValueMatrix1D() {
        for (int i = 0; i < expectedValueMatrix1D.length; i++) {
            expectedValueMatrix1D[i] = dist[i] * n;
        }
    }

    /**
     * Method Name: verifyExpectedCounts1D
     * Purpose: If all the values are
     * greater than or equal to 5, then return true.
     * Else, the conditions have not been met and return false.
     * @return if expected counts is met
     **/
    public boolean verifyExpectedCounts1D() {
        for (double v : expectedValueMatrix1D) {
            if (v < 5) return false;
        }
        return true;
    }

    /**
     * Method Name: displayExpectedCounts1D
     * Purpose: Displays expected counts array
     */
    public void displayExpectedCounts1D() {
        for (double v : expectedValueMatrix1D) System.out.print(v + " ");
        System.out.println();
    }

    // 2D methods below

    /**
     * Method Name: setExpectedValueMatrix2D
     * Purpose: Sets the expected value matrix
     * for the 2D array
     **/
    public void setExpectedValueMatrix2D(int row, int col) {
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                calcIndividualVal(r, c);
            }
        }
    }

    /**
     * Method Name: verifyExpectedCounts2D
     * Purpose: Ensures all values are greater than
     * ore equal to 5. Returns true if they are, false
     * if they are not
     * @return if expected counts is met
     */
    public boolean verifyExpectedCounts2D() {
        for (double[] doubles : expectedValueMatrix2D) {
            for (int c = 0; c < expectedValueMatrix2D[0].length; c++) {
                if (doubles[c] < 5) return false;
            }
        }
        return true;
    }

    /**
     * Method Name: displayExpectedCounts2D
     * Purpose: Display all values in the 2D array
     */
    private void displayExpectedCounts2D() {

        for (int r = 0; r < expectedValueMatrix2D.length; r++) {
            int current = r + 1;
            System.out.print("Row " + current + ": ");
            for (int c = 0; c < expectedValueMatrix2D[0].length; c++) {
                System.out.print(expectedValueMatrix2D[r][c] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Helper method for setExpectedValueMatrix2D
     * @return sum of row
     */
    public double calcRow(int row) {
        double sum = 0.0;
        // userMatrix2D[0].length is the length of columns
        for (int i = 0; i < userMatrix2D[0].length; i++) {
             sum += userMatrix2D[row][i];
        }
        return sum;
    }

    /**
     * Helper method for setExpectedValueMatrix2D
     * @return sum of col
     */
    public double calcCol(int col) {
        double sum = 0.0;
        // userMatrix2D.length is the length of rows
        for (long[] longs : userMatrix2D) {
            sum += longs[col];
        }
        return sum;
    }

    /**
     * Helper method for setExpectedValueMatrix2D
     * Sets the expected value given the row index and col index.
     * Then takes the row sum and col sum and divides it by the total
     * to get the expected count value.
     */
    public void calcIndividualVal(int row, int col) {
        expectedValueMatrix2D[row][col] = (calcRow(row) * (calcCol(col))/n);
    }

    // END OF MOST IMPORTANT METHODS INVOLVING 1-var stats or 2-var stats

    /**
     * Method Name: sorryMessage()
     * Purpose: Outputs sorry when the conditions for
     * the test have not been met
     **/
    public void sorryMessage() {
        System.out.println("I'm sorry, the conditions for this test are not met");
    }

    /**
     * Method Name: calculateDF
     * Purpose: Calculate df of 1-var stats
     * @param categories is the number of categories
     * @return degrees of freedom
     */
    public double calculateDF(int categories) {
        return categories - 1;
    }

    /**
     * Method Name: calculateDF
     * Purpose: Calculate df of 2-var stats
     * @param rows is the number of rows
     * @param cols is the number of columns
     * @return degrees of freedom
     */
    public double calculateDF(int rows, int cols) {
        return (rows - 1) * (cols - 1);
    }

    /**
     * Method Name: summary()
     * Purpose: Gives a brief summary of test results.
     * The user will still have to contextualize this answer and cannot
     * use this as their standalone answer
     * @return String of the summary
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
        else if (isIndependence){
            str += "the two variables are dependent.";
        }
        else {
            str += "***ERROR***";
        }
        return str;
    }

    /**
     * THESE ARE THE GETTER AND SETTER METHODS USED
     */

    public boolean isGoodnessOfFit() {
        return isGoodnessOfFit;
    }

    public void setGoodnessOfFit(boolean goodnessOfFit) {
        isGoodnessOfFit = goodnessOfFit;
    }

    public boolean isHomogeneity() {
        return isHomogeneity;
    }

    public void setHomogeneity(boolean homogeneity) {
        isHomogeneity = homogeneity;
    }

    public boolean isIndependence() {
        return isIndependence;
    }

    public void setIndependence(boolean independence) {
        isIndependence = independence;
    }

    public double getLevel() {
        return level;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double getChiSquare() {
        return chiSquare;
    }

    public void setChiSquare(double chiSquare) {
        this.chiSquare = chiSquare;
    }

    public double getpVal() {
        return pVal;
    }

    public void setpVal(double pVal) {
        this.pVal = pVal;
    }

    public long[][] getUserMatrix2D() {
        return userMatrix2D;
    }

    public void setUserMatrix2D(long[][] userMatrix2D) {
        this.userMatrix2D = userMatrix2D;
    }

    public double[][] getExpectedValueMatrix2D() {
        return expectedValueMatrix2D;
    }

    public void setExpectedValueMatrix2D(double[][] expectedValueMatrix2D) {
        this.expectedValueMatrix2D = expectedValueMatrix2D;
    }

    public long[] getUserMatrix1D() {
        return userMatrix1D;
    }

    public void setUserMatrix1D(long[] userMatrix1D) {
        this.userMatrix1D = userMatrix1D;
    }

    public double[] getExpectedValueMatrix1D() {
        return expectedValueMatrix1D;
    }

    public void setExpectedValueMatrix1D(double[] expectedValueMatrix1D) {
        this.expectedValueMatrix1D = expectedValueMatrix1D;
    }

    public double[] getDist() {
        return dist;
    }

    public void setDist(double[] dist) {
        this.dist = dist;
    }

} // end of class