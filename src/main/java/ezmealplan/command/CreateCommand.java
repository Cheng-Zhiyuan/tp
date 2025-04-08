package ezmealplan.command;

import ezmealplan.command.checkers.CreateChecker;
import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.food.Ingredient;
import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.food.list.MealList;
import ezmealplan.ui.UserInterface;

import java.util.logging.Logger;

public class CreateCommand extends Command {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String MNAME = "/mname";
    private static final String ING = "/ing";

    public CreateCommand(String userInputText) {
        validUserInput = userInputText.trim();
        lowerCaseInput = userInputText.toLowerCase();
    }

    /**
     * Executes the Create command.
     *
     * @param mealManager the MealManager providing access to the RecipeList.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) throws EZMealPlanException {
        boolean isValidUserInput = checkValidUserInput();
        if (!isValidUserInput) {
            logger.severe("Huge issue detected! The user input format remains invalid despite " +
                    "passing all the checks for input formatting error.");
        }
        assert isValidUserInput;

        Meal newMeal = createNewMeal();
        MealList recipesList = mealManager.getRecipesList();
        mealManager.addMeal(newMeal, recipesList);
        ui.printAddMealMessage(newMeal, recipesList);
    }

    private Meal createNewMeal() throws EZMealPlanException {
        int afterMnameIndex = lowerCaseInput.indexOf(MNAME) + MNAME.length();
        int ingIndex = lowerCaseInput.indexOf(ING);
        String mealName = validUserInput.substring(afterMnameIndex, ingIndex).trim();
        logger.fine("The user is now creating a new meal: " + mealName + ".");
        Meal newMeal = new Meal(mealName);
        addAllIngredients(newMeal);
        return newMeal;
    }

    private String[] extractIngredients() {
        int afterIngIndex = lowerCaseInput.indexOf(ING) + ING.length();
        String ingredients = validUserInput.substring(afterIngIndex).trim();
        String splitRegex = "\\s*,\\s*";
        return ingredients.split(splitRegex);
    }

    private void addAllIngredients(Meal newMeal) throws EZMealPlanException {
        String[] ingredientArray = extractIngredients();
        for (String ingredientString : ingredientArray) {
            String[] ingredientNamePrice = getNamePrice(ingredientString);
            int nameIndex = 0;
            int priceIndex = 1;
            String ingredientName = ingredientNamePrice[nameIndex];
            String ingredientPrice = ingredientNamePrice[priceIndex];
            Ingredient newIngredient = new Ingredient(ingredientName, ingredientPrice);
            newMeal.addIngredient(newIngredient);
        }
    }

    private static String[] getNamePrice(String ingredient) {
        String openBracket = "(";
        String closeBracket = ")";
        int zeroIndex = 0;
        int openBracketIndex = ingredient.indexOf(openBracket);
        int closeBracketIndex = ingredient.indexOf(closeBracket);
        int afterOpenBracketIndex = ingredient.indexOf(openBracket) + openBracket.length();
        String ingredientName = ingredient.substring(zeroIndex, openBracketIndex).trim();
        String ingredientPrice = ingredient.substring(afterOpenBracketIndex, closeBracketIndex).trim();
        return new String[]{ingredientName, ingredientPrice};
    }

    private boolean checkValidUserInput() throws EZMealPlanException {
        CreateChecker checker = new CreateChecker(validUserInput);
        checker.check();
        return checker.isPassed();
    }
}
