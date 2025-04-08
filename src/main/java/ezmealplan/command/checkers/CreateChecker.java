package ezmealplan.command.checkers;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.InvalidCreateIndexException;
import ezmealplan.exceptions.InvalidIngMnameException;
import ezmealplan.exceptions.InvalidIngredientFormatException;
import ezmealplan.exceptions.MissingIngKeywordException;
import ezmealplan.exceptions.MissingIngredientException;
import ezmealplan.exceptions.MissingMealNameException;
import ezmealplan.exceptions.MissingMnameKeywordException;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateChecker extends Checker {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String ING = "/ing";
    private static final String MNAME = "/mname";
    private static final String CREATE = "create";

    public CreateChecker(String userInputText) {
        this.userInput = userInputText.trim();
        this.lowerCaseInput = userInputText.toLowerCase();
    }

    @Override
    public void check() throws EZMealPlanException {
        logger.fine("Checking '" + userInput + "' for errors.");
        checkMnameExists();
        checkIngExists();
        checkMnameIngIndexes();
        checkMealNameExists();
        checkIngredientExists();
        checkIngredientFormat();
        setPassed(true);
    }

    private void checkMnameIngIndexes() throws EZMealPlanException {
        int mnameIndex = lowerCaseInput.indexOf(MNAME);
        int createIndex = lowerCaseInput.indexOf(CREATE);
        int ingIndex = lowerCaseInput.indexOf(ING);
        boolean isValidCreateIndex = createIndex < mnameIndex && createIndex < ingIndex;
        if (!isValidCreateIndex) {
            String message = "Triggers InvalidCreateIndexException()!";
            logger.warning(message);
            throw new InvalidCreateIndexException();
        }
        boolean isValidSeqIndexes = mnameIndex < ingIndex;
        if (!isValidSeqIndexes) {
            String message = "Triggers InvalidIngMnameException()!";
            logger.warning(message);
            throw new InvalidIngMnameException();
        }
    }

    private void checkIngExists() throws MissingIngKeywordException {
        boolean isIngPresent = lowerCaseInput.contains(ING);
        if (!isIngPresent) {
            String message = "Triggers MissingIngKeywordException()!";
            logger.warning(message);
            throw new MissingIngKeywordException("CREATE");
        }
    }

    private void checkMnameExists() throws MissingMnameKeywordException {
        boolean isMnamePresent = lowerCaseInput.contains(MNAME);
        if (!isMnamePresent) {
            String message = "Triggers MissingMnameKeywordException()!";
            logger.warning(message);
            throw new MissingMnameKeywordException();
        }
    }

    private void checkMealNameExists() throws MissingMealNameException {
        int afterMnameIndex = lowerCaseInput.indexOf(MNAME) + MNAME.length();
        int ingIndex = lowerCaseInput.indexOf(ING);
        String mealName = userInput.substring(afterMnameIndex, ingIndex).trim();
        if (mealName.isEmpty()) {
            String message = "Triggers MissingMealNameException()!";
            logger.warning(message);
            throw new MissingMealNameException(CREATE);
        }
    }

    private void checkIngredientExists() throws MissingIngredientException {
        int afterIngIndex = lowerCaseInput.indexOf(ING) + ING.length();
        String ingredients = userInput.substring(afterIngIndex).trim();
        if (ingredients.isEmpty()) {
            String message = "Triggers MissingIngredientException()!";
            logger.warning(message);
            throw new MissingIngredientException(CREATE);
        }
    }

    private void checkIngredientFormat() throws InvalidIngredientFormatException {
        int afterIngIndex = lowerCaseInput.indexOf(ING) + ING.length();
        String ingredients = userInput.substring(afterIngIndex).trim();
        String splitRegex = "\\s*,\\s*";
        String[] ingredientArray = ingredients.split(splitRegex);
        String matchingRegex = "^([\\S\\s]+)\\s*\\((-?\\d+(\\.\\d*)?)\\)$";
        Pattern ingredientPattern = Pattern.compile(matchingRegex);
        for (String ingredient : ingredientArray) {
            Matcher ingredientMatcher = ingredientPattern.matcher(ingredient);
            if (!ingredientMatcher.matches()) {
                String message = "Triggers InvalidIngredientFormatException()!";
                logger.warning(message);
                throw new InvalidIngredientFormatException();
            }
        }
    }
}
