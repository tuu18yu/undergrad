package Presenter;

/**
 * Provides methods for printing text to the console related to file reading and writing.
 *
 * @author Filip Jovanovic
 */
public class ReadWritePresenter {

    /**
     * Prints text indicating that reading from files was successful.
     */
    public void printReadSuccess() {
        System.out.println("Successfully read in data from the files.");
    }

    /**
     * Prints text indicating that writing to files was successful.
     */
    public void printWriteSuccess() {
        System.out.println("Successfully wrote data to the files.");
    }

    /**
     * Prints text indicating an error has occurred while attempting to read from files.
     */
    public void printReadError() {
        System.out.println("Unable to read in data from the files.");
    }

    /**
     * Prints text indicating an error has occurred while attempting to write to files.
     */
    public void printWriteError() {
        System.out.println("Unable to write data to the files.");
    }

    /**
     * Prints text displaying a menu of options that can be chosen when there is an error while
     * attempting to read from files.
     */
    public void printReadErrorMenu() {
        System.out.println("Please select one of the following:\n" +
                "1. Retry reading from files.\n" +
                "2. Continue without reading.\n" +
                "3. Exit the program.\n" +
                "Enter 1, 2, or 3: ");
    }

    /**
     * Prints text displaying a menu of options that can be chosen when there is an error while
     * attempting to write to files.
     */
    public void printWriteErrorMenu() {
        System.out.println("Please select one of the following:\n" +
                "1. Retry saving to files.\n" +
                "2. Exit without saving.\n" +
                "Enter 1 or 2: ");
    }

    /**
     * Prints text indicating an invalid option has been selected.
     */
    public void printInvalidOption() {
        System.out.println("Invalid option selected. Please try again: ");
    }
}
