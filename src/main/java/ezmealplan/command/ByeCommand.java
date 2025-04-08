package ezmealplan.command;

import ezmealplan.food.Meal;
import ezmealplan.storage.Storage;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ByeCommand extends Command {
    @Override
    public boolean isExit() {
        return true;
    }

    /**
     * Executes the bye command.
     * Saves the WishList, RecipesList, and Inventory to their respective locations on disk.
     *
     * @param mealManager the MealManager providing access to the lists.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) {
        updateRecipesListFile(mealManager,ui);
        updateWishListFile(mealManager, ui);
        updateInventoryListFile(mealManager, ui);
        ui.printGoodbye();
    }

    private void updateWishListFile(MealManager mealManager, UserInterface ui) {
        List<Meal> wishList = mealManager.getWishList().getList();
        String wishListFilePath = Storage.getWishListFilePath();
        clearAndUpdateFile(wishList, wishListFilePath, ui);
    }

    private void updateRecipesListFile(MealManager mealManager, UserInterface ui) {
        List<Meal> recipesList = mealManager.getRecipesList().getList();
        String recipesListFilePath = Storage.getRecipesListFilePath();
        clearAndUpdateFile(recipesList, recipesListFilePath, ui);
    }

    private void clearAndUpdateFile(List<Meal> mealList, String filePath, UserInterface ui) {
        clearFile(filePath, ui);
        writeMealsToFile(mealList, filePath, ui);
    }

    private static void writeMealsToFile(List<Meal> mealList, String filePath, UserInterface ui) {
        for (Meal newMeal : mealList) {
            try {
                Storage.writeToFile(newMeal.toDataString(), filePath);
            } catch (IOException ioException) {
                ui.printMessage("Error writing to file: " + ioException.getMessage());
            }
        }
    }

    private static void clearFile(String filePath, UserInterface ui) {
        try {
            Storage.clearFile(filePath);
        } catch (IOException ioException) {
            ui.printMessage("Error clearing file: " + ioException.getMessage());
        }
    }

    private static void writeIngredientsToFile(ArrayList<String> inventoryDataArray, String filePath,
                                               UserInterface ui) {
        for (String data : inventoryDataArray) {
            try {
                Storage.writeToFile(data, filePath);
            } catch (IOException ioException) {
                ui.printMessage("Error writing to file: " + ioException.getMessage());
            }
        }
    }

    private void updateInventoryListFile(MealManager mealManager, UserInterface ui) {
        ArrayList<String> inventoryDataArray = mealManager.getInventory().toDataArray();
        String inventoryListFilePath = Storage.getInventoryListFilePath();

        clearFile(inventoryListFilePath, ui);
        writeIngredientsToFile(inventoryDataArray, inventoryListFilePath, ui);
    }
}
