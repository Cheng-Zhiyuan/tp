package ezmealplan.command;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.logic.MealManager;
import ezmealplan.food.list.MealList;
import ezmealplan.ui.UserInterface;

import java.util.logging.Logger;

public class DeleteCommand extends RemoveDeleteCommand {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public DeleteCommand(String userInputText) {
        super(userInputText);
        this.removeOrDelete = delete;
        logger.fine("Received \"Delete\" command, user input: " + userInputText);
    }

    /**
     * Executes the Delete command.
     *
     * @param mealManager the MealManager providing access to the lists.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) throws EZMealPlanException {
        super.execute(mealManager, ui);

        MealList wishList = mealManager.getWishList();
        if (wishList.contains(removedOrDeletedMeal)) {
            int indexInWishList = wishList.getIndex(removedOrDeletedMeal);
            wishList.removeMeal(indexInWishList);
            ui.printRemovedMessage(removedOrDeletedMeal, wishList.size());
            logger.fine("Command finished executing: Removed \"" + removedOrDeletedMeal.getName() + "\" meal " +
                    "from wishlist");
        }
        logger.fine("Command finished executing: Deleted \"" + removedOrDeletedMeal.getName() + "\" meal from " +
                "recipes list");
    }
}
