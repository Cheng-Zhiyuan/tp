//@@author olsonwangyj
package ezmealplan.command.checkers;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.MissingIngKeywordException;
import ezmealplan.exceptions.MissingIngredientException;

import java.util.logging.Logger;

public class RecommendChecker extends Checker {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String ING = "/ing";
    private static final String COMMAND_NAME = "recommend";

    public RecommendChecker(String userInputText) {
        this.userInput = userInputText.trim();
        this.lowerCaseInput = userInputText.toLowerCase();
    }

    @Override
    public void check() throws EZMealPlanException {
        logger.fine("Checking '" + userInput + "' for recommend command errors.");
        checkIngExists();
        checkIngredientExists();
        setPassed(true);
    }

    /**
     * Checks whether the '/ing' keyword exists in the recommend command input.
     *
     * @throws MissingIngKeywordException if '/ing' is not found.
     */
    private void checkIngExists() throws MissingIngKeywordException {
        if (!lowerCaseInput.contains(ING)) {
            String message = "Triggers MissingIngKeywordException()!";
            logger.warning(message);
            throw new MissingIngKeywordException("RECOMMEND");
        }
    }

    /**
     * Checks that there is non-empty ingredient information provided after the '/ing' keyword.
     *
     * @throws MissingIngredientException if no ingredient details are found.
     */
    private void checkIngredientExists() throws MissingIngredientException {
        int afterIngIndex = lowerCaseInput.indexOf(ING) + ING.length();
        String ingredients = userInput.substring(afterIngIndex).trim();
        if (ingredients.isEmpty()) {
            String message = "Triggers MissingIngredientException()!";
            logger.warning(message);
            throw new MissingIngredientException(COMMAND_NAME);
        }
    }
}
