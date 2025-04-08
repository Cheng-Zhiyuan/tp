package ezmealplan.command;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

public abstract class Command {
    protected static String validUserInput;
    protected String lowerCaseInput;

    public boolean isExit() {
        return false;
    }

    /**
     * Executes the command
     *
     * @param mealManager the MealManager providing access to the lists.
     * @param ui          the UserInterface for printing messages.
     */
    public abstract void execute(MealManager mealManager, UserInterface ui) throws EZMealPlanException;
}
