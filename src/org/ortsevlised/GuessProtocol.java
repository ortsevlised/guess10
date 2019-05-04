package org.ortsevlised;

public class GuessProtocol {

    public static final int PORT = 6666;
    public static final String SERVER_ADDRESS = "localhost";
    public static final String CLIENT_WINS = "Correct – you win!";
    public static final String SERVER_WINS = "You’re out of guesses – you lose!";
    public static final String BYE = "Thanks for playing, bye!";
    public static final String PLAY_AGAIN_Y_N = ". Would you like to play again (y/n)";

    public final String WELCOME = "Welcome to 10 guesses SimpleNetworkProject. ";
    public final String TOO_LOW = "Too low – guess again";
    public final String TOO_HIGH = "Too high – guess again";
    public final String ENTER_YOUR_NAME = "Please enter your name";
    public final String ENTER_A_NUMBER = "Please enter a number between 1 and 1000";
    public final String NUMBER_OUT_OF_RANGE = "Your number is too large/small, please make a guess between 1 to 1000";
    public final String NOT_A_NUMBER = "Just choose numbers! Try again";

    private final int NEW_CONNECTION = 0;
    private final int WAITING_FOR_NAME = 1;
    private final int NUMBER_RECEIVED = 2;
    private final int AGAIN = 3;
    private final int GUESSES_LIMIT = 11;

    private String name;
    private int state = NEW_CONNECTION;
    private int attempts = 0;
    private int numberToGuess;
    private int clientNumber;
    private int userScore = 0;
    private int serverScore = 0;

    public void setNumberToGuess(int numberToGuess) {
        this.numberToGuess = numberToGuess;
    }

    public String processInput(String theInput) {
        switch (state) {
            case NEW_CONNECTION:
                state = WAITING_FOR_NAME;
                return WELCOME + ENTER_YOUR_NAME;
            case WAITING_FOR_NAME:
                return processName(theInput);
            case NUMBER_RECEIVED:
                return processNumber(theInput);
            case AGAIN:
                return evaluateIfPlayAgain(theInput);
            default:
                throw new RuntimeException("Something went wrong with the states");
        }
    }

    private String processName(String theInput) {
        String theOutput;
        if (!isANumber(theInput)) {
            name = theInput;
            theOutput = "Hi " + name + ". " + ENTER_A_NUMBER;
            state = NUMBER_RECEIVED;
        } else {
            theOutput = "That's not a valid name";
            state = WAITING_FOR_NAME;
        }
        return theOutput;
    }

    private String processNumber(String theInput) {
        String theOutput;
        if (attempts < GUESSES_LIMIT) {
            try {
                clientNumber = Integer.parseInt(theInput);
                if (clientNumber < 1 || clientNumber > 999) {
                    theOutput = NUMBER_OUT_OF_RANGE;
                    return theOutput;
                }
            } catch (NumberFormatException nfe) {
                theOutput = NOT_A_NUMBER;
                return theOutput;
            }
            attempts++;

            if (clientNumber == numberToGuess) {
                userScore++;
                theOutput = CLIENT_WINS + " The score is -> " + name + ": " + userScore + " Server: " + serverScore + PLAY_AGAIN_Y_N;
                ;
                state = AGAIN;

            } else if (attempts == GUESSES_LIMIT - 1) {
                serverScore++;
                theOutput = SERVER_WINS + " The score is -> " + name + ": " + userScore + " Server: " + serverScore + PLAY_AGAIN_Y_N;
                ;
                ;
                state = AGAIN;

            } else {
                theOutput = (Integer.parseInt(theInput) < numberToGuess ? TOO_LOW : TOO_HIGH);
            }
        } else {
            serverScore++;
            theOutput = SERVER_WINS + " The score is -> " + name + ": " + userScore + " Server: " + serverScore + PLAY_AGAIN_Y_N;
            state = AGAIN;
        }
        return theOutput;
    }

    private String evaluateIfPlayAgain(String theInput) {
        String theOutput;
        if (theInput.equalsIgnoreCase("y")) {
            theOutput = ENTER_A_NUMBER;
            attempts = 0;
            state = NUMBER_RECEIVED;
        } else {
            theOutput = BYE;
        }
        return theOutput;
    }

    private boolean isANumber(String data) {
        try {
            Integer.parseInt(data);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
