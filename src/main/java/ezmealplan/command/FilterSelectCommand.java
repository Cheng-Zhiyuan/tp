package ezmealplan.command;

import ezmealplan.command.checkers.FilterChecker;
import ezmealplan.command.checkers.FilterSelectChecker;
import ezmealplan.command.checkers.SelectChecker;
import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.InvalidFilterMethodException;
import ezmealplan.exceptions.InvalidMcostException;

import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

import java.util.List;

public abstract class FilterSelectCommand extends Command {
    protected static final String ING = "/ing";
    protected static final String MNAME = "/mname";
    protected static final String MCOST = "/mcost";
    protected static final String BY_ING = "byIng";
    protected static final String BY_MNAME = "byMname";
    protected static final String BY_MCOST = "byMcost";
    String filterOrSelect;
    String filterMethod = "";

    protected boolean checkValidUserInput(String filterOrSelect) throws EZMealPlanException {
        getFilterMethod();
        FilterSelectChecker checker = getFilterSelectChecker(filterOrSelect, filterMethod);
        checker.check();
        return checker.isPassed();
    }

    private FilterSelectChecker getFilterSelectChecker(String filterOrSelect, String filterMethod) {
        String filter = "filter";
        return filterOrSelect.equals(filter) ? new FilterChecker(validUserInput, filterMethod) :
                new SelectChecker(validUserInput, filterMethod);
    }

    private void getFilterMethod() throws EZMealPlanException {
        boolean isContainIng = this.lowerCaseInput.contains(ING);
        boolean isContainMname = this.lowerCaseInput.contains(MNAME);
        boolean isContainMcost = this.lowerCaseInput.contains(MCOST);
        if (isContainIng && !isContainMname && !isContainMcost) {
            filterMethod = BY_ING;
            return;
        } else if (!isContainIng && isContainMname && !isContainMcost) {
            filterMethod = BY_MNAME;
            return;
        } else if (!isContainIng && !isContainMname && isContainMcost) {
            filterMethod = BY_MCOST;
            return;
        }
        String select = "select";
        boolean isSelect = filterOrSelect.equals(select);
        if (isSelect && !(isContainMcost && isContainIng && isContainMname)) {
            return;
        }
        throw new InvalidFilterMethodException();
    }

    protected List<Meal> getFilteredMealList(MealManager mealManager)
            throws EZMealPlanException {
        return switch (filterMethod) {
        case BY_ING -> filterByIngList(mealManager);
        case BY_MNAME -> filterByMnameList(mealManager);
        case BY_MCOST -> filterByMcostList(mealManager);
        default -> mealManager.getRecipesList().getList();
        };
    }

    private List<Meal> filterByMcostList(MealManager mealManager) throws EZMealPlanException {
        int afterMcostIndex = this.lowerCaseInput.indexOf(MCOST) + MCOST.length();
        String mcostInput = validUserInput.substring(afterMcostIndex).trim();
        double mcostDouble = checkValidMcostPrice(mcostInput);
        return mealManager.filteringByMcost(mcostDouble);
    }

    private double checkValidMcostPrice(String mcostInput) throws EZMealPlanException {
        double mcostDouble;
        try {
            checkTwoDecimalPlace(mcostInput);
            mcostDouble = Double.parseDouble(mcostInput);
            checkMcostOutOfRange(mcostDouble);
        } catch (NumberFormatException numberFormatException) {
            throw new InvalidMcostException();
        }
        return mcostDouble;
    }

    private static void checkMcostOutOfRange(double mcostDouble) {
        double zero = 0.00;
        double maxPrice = 9999999999999.99;
        if (mcostDouble < zero || mcostDouble > maxPrice) {
            throw new NumberFormatException();
        }
    }

    private void checkTwoDecimalPlace(String ingredientPrice) throws NumberFormatException {
        String twoDecimalPlaceRegex = "^-?\\d+\\.\\d{2}$";
        if (!ingredientPrice.matches(twoDecimalPlaceRegex)) {
            throw new NumberFormatException();
        }
    }

    private List<Meal> filterByMnameList(MealManager mealManager) {
        int afterMnameIndex = this.lowerCaseInput.indexOf(MNAME) + MNAME.length();
        String mnameInput = validUserInput.substring(afterMnameIndex).trim();
        String splitRegex = "\\s*,\\s*";
        String[] mealNameArray = mnameInput.split(splitRegex);
        return mealManager.filteringByMname(mealNameArray);
    }

    private List<Meal> filterByIngList(MealManager mealManager) {
        int afterIngIndex = this.lowerCaseInput.indexOf(ING) + ING.length();
        String ingInput = validUserInput.substring(afterIngIndex).trim();
        String splitRegex = "\\s*,\\s*";
        String[] ingredientsArray = ingInput.split(splitRegex);
        return mealManager.filteringByIng(ingredientsArray);
    }

    /**
     * Prints the filtered MealList.
     *
     * @param filteredMealList a List of the filtered Meals.
     * @param ui          the UserInterface for printing messages.
     */
    public void printFilteredMealList(List<Meal> filteredMealList, UserInterface ui) {
        String mealCost = "meal cost";
        String mealName = "meal name";
        String ingredients = "ingredient(s)";
        if (filteredMealList.isEmpty()) {
            System.out.println("The filtered meal list is empty.");
            return;
        }
        String filterMessage = "the meal list filtered by ";
        String inputMessage = getString(mealCost, ingredients, mealName);
        String message = filterMessage + inputMessage;
        ui.printMealList(filteredMealList, message);
    }

    protected String getString(String mealCost, String ingredients, String mealName) {
        String inputMessage = "";
        switch (filterMethod) {
        case BY_MCOST:
            inputMessage = mealCost;
            break;
        case BY_ING:
            inputMessage = ingredients;
            break;
        case BY_MNAME:
            inputMessage = mealName;
            break;
        default:
            break;
        }
        return inputMessage;
    }
}
