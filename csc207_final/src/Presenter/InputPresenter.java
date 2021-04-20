package Presenter;

/**
 * A presenter class for messages related to validating input from the user.
 */
public class InputPresenter {

    /**
     * Prints a message indicating the user must enter a number that is greater than or equal to a specified number.
     * @param number the number that the input must be greater than or equal to
     */
    public void printEnterNumberGreaterOrEqual(int number) {
        System.out.print("Please enter a number that is " + number + " or greater: ");
    }

    /**
     * Prints a message indicating the user must enter either Yes or No.
     */
    public void printEnterYesOrNo() {
        System.out.print("Please enter either Yes or No: ");
    }

    /**
     * Prints a message indicating the user must not leave the input empty.
     */
    public void printEnterNonEmptyString() {
        System.out.println("Please do not leave this field blank: ");
    }

    /**
     * Prints a message indicating the user entered an invalid input.
     */
    public void printInvalidInput() {
        System.out.println("Invalid input, please try again.");
    }
}
