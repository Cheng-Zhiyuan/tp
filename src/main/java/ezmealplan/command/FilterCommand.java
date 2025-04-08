package ezmealplan.command;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

import java.util.List;
import java.util.logging.Logger;

public class FilterCommand extends FilterSelectCommand {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public FilterCommand(String userInput) {
        validUserInput = userInput.trim();
        this.lowerCaseInput = validUserInput.toLowerCase();
        this.filterOrSelect = "filter";
    }

    /**
     * Executes the Filter command.
     *
     * @param mealManager the MealManager providing access to the RecipeList.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) throws EZMealPlanException {
        boolean isValidUserInput = checkValidUserInput(filterOrSelect);
        if (!isValidUserInput) {
            logger.severe("Huge issue detected! The user input format remains invalid despite " +
                    "passing all the checks for input formatting error.");
        }
        assert isValidUserInput;
        List<Meal> filteredMealList = getFilteredMealList(mealManager);
        printFilteredMealList(filteredMealList, ui);
    }

}
