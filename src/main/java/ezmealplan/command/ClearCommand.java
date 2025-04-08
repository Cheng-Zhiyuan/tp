//@@author ryanling169
package ezmealplan.command;

import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;
import ezmealplan.food.Meal;

import java.util.List;

public class ClearCommand extends Command {

    /**
     * Executes the clear command.
     *
     * @param mealManager the MealManager providing access to the WishList.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) {
        List<Meal> wishList = mealManager.getWishList().getList();
        wishList.clear();
        ui.printClearedList();
    }
}
