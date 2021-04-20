package Controller;

import Presenter.InputPresenter;

import java.util.Scanner;

/**
 * A controller class used for validating inputs that have certain restrictions on them.
 *
 * @author Filip Jovanovic
 */
public class InputValidator {
    Scanner scanner = new Scanner(System.in);
    InputPresenter inputPresenter = new InputPresenter();

    /**
     * Gets an integer greater or equal to a certain integer from the user.
     * If the user enters a number less than the specified integer,
     * they will be prompted to try again.
     *
     * @param num - the integer that the input should be greater to or equal to.
     * @return number received as input from user.
     */
    public int getIntGreaterOrEqual(int num) {
        int input;
        boolean validInput = false;

        do {
            while (!scanner.hasNextInt()) {
                scanner.next();
                inputPresenter.printEnterNumberGreaterOrEqual(num);
            }

            input = scanner.nextInt();

            if (input < num)
                inputPresenter.printEnterNumberGreaterOrEqual(num);
            else
                validInput = true;

        } while(!validInput);

        scanner.nextLine(); // Gets rid of the \n after getting the int.
        return input;
    }

    /**
     * Gets a string input from the user and returns a boolean if input is "yes" or "no".
     * If user enters a value that is not "yes" or "no", the user is prompted for an input again.
     *
     * @return true if input was "yes", false if input is "no".
     */
    public boolean getStringForBoolean() {
        String input;
        boolean output = false;
        boolean validInput = false;

        while (!validInput) {
            input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("yes")) {
                output = true;
                validInput = true;
            } else if (input.equals("no")) {
                validInput = true;
            } else {
                inputPresenter.printEnterYesOrNo();
            }
        }

        return output;
    }

    /**
     * Gets a string input from the user as long as it is not empty. If it is empty,
     * the user is prompted for an input again.
     *
     * @return the nonempty string input.
     */
    public String getNonEmptyString() {
        String input = "";
        boolean validInput = false;

        while (!validInput) {
            input = scanner.nextLine();

            if (input == null || input.isEmpty())
                inputPresenter.printEnterNonEmptyString();
            else
                validInput = true;
        }

        return input;
    }
}
