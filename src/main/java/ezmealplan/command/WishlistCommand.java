//@@author olsonwangyj
package ezmealplan.command;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

import java.util.List;
import java.util.logging.Logger;

public class WishlistCommand extends Command {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Executes the WishList command.
     *
     * @param mealManager the MealManager providing access to the WishList.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) throws EZMealPlanException {
        assert mealManager != null : "MealManager cannot be null";
        logger.fine("Executing 'wishlist' Command");
        List<Meal> wishList = mealManager.getWishList().getList();
        String wishListName = mealManager.getWishList().getMealListName();
        ui.printMealList(wishList, wishListName);
    }
}
