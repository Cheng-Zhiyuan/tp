package ezmealplan.command;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

import java.util.logging.Logger;

public class RemoveCommand extends RemoveDeleteCommand {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public RemoveCommand(String userInputText) {
        super(userInputText);
        this.removeOrDelete = remove;
        logger.fine("Received \"Remove\" command, user input: " + userInputText);
    }

    /**
     * Executes the Remove command.
     *
     * @param mealManager the MealManager providing access to the lists.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) throws EZMealPlanException {
        super.execute(mealManager, ui);
        logger.fine("Command finished executing: Removed \"" + removedOrDeletedMeal.getName() + "\" meal");
    }
}
