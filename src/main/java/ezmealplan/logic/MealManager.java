package ezmealplan.logic;

import ezmealplan.exceptions.DuplicateMealException;
import ezmealplan.exceptions.EmptyListException;
import ezmealplan.exceptions.RemoveIndexOutOfRangeException;
import ezmealplan.food.Ingredient;
import ezmealplan.food.list.Inventory;
import ezmealplan.food.Meal;
import ezmealplan.food.list.MealList;
import ezmealplan.food.list.RecipesList;
import ezmealplan.food.list.WishList;

import java.util.ArrayList;
import java.util.List;

public class MealManager {
    private final MealList wishList = new WishList();
    private final MealList recipesList = new RecipesList();
    private final Inventory inventory = new Inventory();

    private MealList chosenMealList;

    public MealList getWishList() {
        return wishList;
    }

    public MealList getRecipesList() {
        return recipesList;
    }

    /**
     * Adds a new meal to the specified MealList after checking for duplicates.
     *
     * @param newMeal The Meal object to be added.
     * @param mealsInput The MealList object to add the Meal to.
     * @throws DuplicateMealException If the Meal object already exists in the MealList.
     */
    public void addMeal(Meal newMeal, MealList mealsInput) throws DuplicateMealException {
        chosenMealList = mealsInput instanceof RecipesList ? getRecipesList() : getWishList();
        chosenMealList.addMeal(newMeal);
    }

    /**
     * Filters the Recipes List by a specified Meal cost.
     *
     * @return The filtered List.
     */
    public List<Meal> filteringByMcost(double mcostDouble) {
        List<Meal> filteredMealList = new ArrayList<>();
        List<Meal> recipesList = getRecipesList().getList();
        for (Meal meal : recipesList) {
            if (meal.getPrice() == mcostDouble) {
                filteredMealList.add(meal);
            }
        }
        return filteredMealList;
    }

    /**
     * Filters the Recipes List by an array of Meal names.
     *
     * @return The filtered List.
     */
    public List<Meal> filteringByMname(String[] mealNameArray) {
        List<Meal> filteredMealList = new ArrayList<>();
        List<Meal> recipesList = getRecipesList().getList();
        for (Meal meal : recipesList) {
            boolean isMatchingMname = checkMname(mealNameArray, meal);
            if (isMatchingMname) {
                filteredMealList.add(meal);
            }
        }
        return filteredMealList;
    }

    private static boolean checkMname(String[] mealNameArray, Meal meal) {
        boolean isMealNameContains = true;
        String getMealName = meal.getName().toLowerCase();
        for (String mealName : mealNameArray) {
            mealName = mealName.toLowerCase();
            if (!getMealName.contains(mealName)) {
                isMealNameContains = false;
                break;
            }
        }
        return isMealNameContains;
    }

    /**
     * Filters the Recipes List by an array of Ingredients.
     *
     * @return The filtered List.
     */
    public List<Meal> filteringByIng(String[] ingredientsArray) {
        List<Meal> filteredMealList = new ArrayList<>();
        List<Meal> mainMealList = getRecipesList().getList();
        for (Meal meal : mainMealList) {
            boolean isMealContainsIng = checkIngPerMeal(ingredientsArray, meal);
            if (isMealContainsIng) {
                filteredMealList.add(meal);
            }
        }
        return filteredMealList;
    }

    private static boolean checkIngPerMeal(String[] ingArray, Meal meal) {
        boolean isMealContainsIng = true;
        ArrayList<Ingredient> ingredientList = (ArrayList<Ingredient>) meal.getIngredientList();
        ArrayList<String> ingredientsNameList = new ArrayList<>();
        ingredientList.forEach(ingredient -> ingredientsNameList.add(ingredient.getName()));
        int overallCount = getOverallCount(ingArray, ingredientsNameList);
        if (overallCount < ingArray.length) {
            isMealContainsIng = false;
        }
        return isMealContainsIng;
    }

    private static int getOverallCount(String[] ingArray, ArrayList<String> ingredientsNameList) {
        int overallCount = 0;
        int zero = 0;
        for (String ingName : ingArray) {
            ingName = ingName.toLowerCase();
            int eachCount = getEachCount(ingName, ingredientsNameList);
            if (eachCount > zero) {
                overallCount++;
            }
        }
        return overallCount;
    }

    private static int getEachCount(String ingName, ArrayList<String> ingredientsNameList) {
        int eachCount = 0;
        for (String ingredientName : ingredientsNameList) {
            ingredientName = ingredientName.toLowerCase();
            if (ingredientName.contains(ingName)) {
                eachCount++;
            }
        }
        return eachCount;
    }

    /**
     * Removes a Meal from the specified MealList.
     *
     * @param index The index of the Meal to be removed in the MealList.
     * @param mealListInput The name of the MealList to remove from.
     * @return The removed Meal.
     * @throws EmptyListException If the MealList is empty.
     * @throws RemoveIndexOutOfRangeException If the input index is out of range for the specified MealList.
     */
    public Meal removeMeal(int index, MealList mealListInput)
            throws EmptyListException, RemoveIndexOutOfRangeException {
        chosenMealList = mealListInput instanceof RecipesList ? getRecipesList() : getWishList();
        return chosenMealList.removeMeal(index);
    }

    /**
     * Compares between the Recipes List and Wishlist in the application and removes Meals that are in the Wishlist
     * but not the Recipes List.
     */
    public void removeIllegalMeals() {
        List<Meal> recipesList = this.recipesList.getList();
        List<Meal> wishList = this.wishList.getList();
        List<Meal> wishListCopy = new ArrayList<>(wishList);
        if(wishList.isEmpty()){
            return;
        }
        for (Meal meal : wishListCopy) {
            if (!recipesList.contains(meal)) {
                wishList.remove(meal);
                String removeIllegalMealMessage = "Removed " + meal + " containing the ingredients: " +
                                                  meal.getIngredientList() +
                                                  " from " + this.wishList.getMealListName() +
                                                  " because it is not found in the "
                                                  + this.recipesList.getMealListName() + ".";
                System.err.println(removeIllegalMealMessage);
            }
        }
        System.out.println();
    }

    public Inventory getInventory() {
        return inventory;
    }
}
