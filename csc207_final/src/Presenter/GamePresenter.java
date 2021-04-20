package Presenter;

public class GamePresenter {

    /**
     * Prints the Game Mode Menu.
     */
    public void printModeMenu() {
        System.out.println("Game Modes: \n" +
                "1. Summation (+)\n" +
                "2. Subtraction (-)\n" +
                "3. Multiplication (*)\n" +
                "4. Division (/)\n" +
                "5. Exit Game\n"+
                "Please enter 1, 2, 3, 4 or 5");
    }

    /**
     * Prints the Level Menu.
     */
    public void printLevelMenu() {
        System.out.println("Welcome to Math Game Menu!\n" +
                "You will get 5 questions in each game. Please select a level.\n" +
                "Hint: You may even win a prize on hard mode\n" +
                "Game Levels:\n" +
                "1. Easy\n" +
                "2. Normal\n" +
                "3. Hard\n" +
                "4. Exit Game\n" +
                "Please enter 1, 2, 3 or 4");
    }

    /**
     * Prints "Invalid input. Please try again...".
     */
    public void printInvalidInput() {
        System.out.println("Invalid Input. Please try again...");
    }

    /**
     * Prints the question in format
     * "Question count: num1 sign num2 = ?"
     * If the question if for division, it prints message that
     * reminds the user to enter the whole number part of the answer.
     *
     * @param count - number of the question
     * @param num1 - the first number
     * @param num2 - the second number
     * @param sign - the sign of this question (+, -, *, /)
     */
    public void printQuestion(int count, int num1, int num2, String sign) {
        System.out.print("Question " + (count + 1) + ": ");
        System.out.println(num1 + " " + sign + " " + num2 + " = ?");
        if(sign.equals("/")){
            System.out.println("Enter the whole number part. (Ex. 4 / 3 = 1)");
        }
    }

    /**
     * Prints "Correct Answer! \^o^/".
     */
    public void printCorrect() {
        System.out.println("Correct Answer! \\^o^/");
    }

    /**
     * Prints "Wrong Answer...".
     */
    public void printWrong() {
        System.out.println("Wrong Answer...");
    }

    /**
     * Prints the correct answer.
     *
     * @param num1 - the first number
     * @param num2 - the second number
     * @param sign - the sign of this question (+, -, *, /)
     */
    public void printAnswer(int num1, int num2, String sign) {
        System.out.print("The correct answer is ");
        switch(sign){
            case "+":
                System.out.println(num1 + num2);
                break;
            case "-":
                System.out.println(num1 - num2);
                break;
            case "*":
                System.out.println(num1 * num2);
                break;
            case "/":
                System.out.println(Math.floorDiv(num1, num2));
                break;
        }
    }

    /**
     * Prints the final result out of 5 questions.
     *
     * @param correct - the correct questions the user gets.
     */
    public void printResult(int correct) {
        System.out.println("You got " + correct + " out of 5 questions correct!");
    }

    /**
     * Prints congratulations for a perfect score on hard mode and offers an invitation code
     */
    public void printCongratulate() {
        System.out.println("Congratulations! You got a perfect score on hard mode.\n" +
                "You are eligible for an invitation code for a VIP account.\n\n" +
                "Do you wish to receive this?\n" +
                "Enter \"Y\" to receive a code or anything else to pass");
    }

    /**
     * Prints a generated invitation code
     * @param code The code generated through UserManager
     */
    public void printInvitationCode(String code) {
        System.out.println("Here is your code: \n" +
                code + "\n" +
                "Reminder: Please copy down this code, as it is for one time use only.\n" +
                "You may return to the main menu and create a new VIP account to redeem it");
    }
}
