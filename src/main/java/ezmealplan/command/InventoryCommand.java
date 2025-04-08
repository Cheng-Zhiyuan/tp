package ezmealplan.command;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.food.list.Inventory;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

import java.util.logging.Logger;

public class InventoryCommand extends Command {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Executes the Inventory command.
     *
     * @param mealManager the MealManager providing access to the Inventory.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) throws EZMealPlanException {
        assert mealManager != null : "MealManager cannot be null";
        logger.fine("Executing 'inventory' command");
        Inventory inventory = mealManager.getInventory();
        ui.printInventory(inventory.toString());
    }
}
