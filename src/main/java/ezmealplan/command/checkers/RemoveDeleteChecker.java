package ezmealplan.command.checkers;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.RemoveFormatException;

import java.util.logging.Logger;

public class RemoveDeleteChecker extends Checker {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public RemoveDeleteChecker(String userInputText) {
        this.userInput = userInputText;
    }

    @Override
    public void check() throws EZMealPlanException {
        logger.fine("Checking '" + userInput + "' for errors.");
        String indexString = extractIndex(userInput);
        parseIndex(indexString);
        setPassed(true);
    }

    private void parseIndex(String input) throws EZMealPlanException {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException numberFormatException) {
            throw new RemoveFormatException(userInput);
        }
    }

    private String extractIndex(String input) throws EZMealPlanException {
        int indexOfIndex = 1;
        try {
            return input.split("\\s+")[indexOfIndex];
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new RemoveFormatException(userInput);
        }
    }
}
