package ezmealplan.storage;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.food.Ingredient;
import ezmealplan.food.list.Inventory;
import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.storage.presetmeals.PresetMeals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private static File wishListFile;
    private static File recipesListFile;
    private static File inventoryListFile;
    private static final String WISH_LIST_FILE_PATH = "data/wishList.txt";
    private static final String RECIPES_LIST_FILE_PATH = "data/recipesList.txt";
    private static final String INVENTORY_LIST_FILE_PATH = "data/inventoryList.txt";

    public static File getWishListFile() {
        return wishListFile;
    }

    public static File getRecipesListFile() {
        return recipesListFile;
    }

    public static File getInventoryListFile() {
        return inventoryListFile;
    }

    public static String getWishListFilePath() {
        return WISH_LIST_FILE_PATH;
    }

    public static String getRecipesListFilePath() {
        return RECIPES_LIST_FILE_PATH;
    }

    public static String getInventoryListFilePath() {
        return INVENTORY_LIST_FILE_PATH;
    }

    /**
     * Creates the files for Recipes List, Wishlist and Inventory in the data directory, if they do not exist already.
     */
    public static void createListFiles() throws IOException {
        wishListFile = new File(WISH_LIST_FILE_PATH);
        recipesListFile = new File(RECIPES_LIST_FILE_PATH);
        inventoryListFile = new File(INVENTORY_LIST_FILE_PATH);
        createListFile(recipesListFile);
        createListFile(wishListFile);
        createListFile(inventoryListFile);
    }

    private static void createListFile(File listFile) throws IOException {
        // Ensure the parent directories exist (if there are any)
        if (listFile.getParentFile() != null) {
            listFile.getParentFile().mkdirs();
        }
        // Create the file if it doesn't already exist
        if (!listFile.exists()) {
            listFile.createNewFile();
        }
    }

    /**
     * Opens the Inventory file in the data folder and loads its contents into the application.
     *
     * @param mealManager The mealManager handling the application's Inventory.
     */
    public static void loadExistingInventory(MealManager mealManager) throws FileNotFoundException {
        Inventory inventory = mealManager.getInventory();
        if (inventoryListFile.exists()) {
            Scanner scanner = new Scanner(inventoryListFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                checkValidIngredients(line, inventory);
            }
            scanner.close();
        }
    }

    /**
     * Opens a MealList file in the data folder and loads its contents.
     *
     * @param selectedFile The file to open from.
     * @return A List of meals that were in the file.
     */
    public static List<Meal> loadExistingList(File selectedFile) throws IOException {
        List<Meal> meals = new ArrayList<>();
        if (selectedFile.exists()) {
            Scanner scanner = new Scanner(selectedFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                // Split the line by "|" with optional spaces around it.
                String[] parts = line.split("\\s*\\|\\s*");
                int minLengthToHaveIng = 2;
                if (parts.length < minLengthToHaveIng) {
                    continue; // Skip lines that don't have ingredients.
                }
                checkValidMeal(parts, meals);
            }
            scanner.close();
        }
        return meals;
    }

    /**
     * Loads preset meals from the PresetMeals class.
     * This is used when no files for the Recipes List is found in the data directory.
     *
     * @return A List of meals that were in the PresetMeals class.
     */
    public static List<Meal> loadPresetMeals() {
        String[] allInitialisedMeals = PresetMeals.createPresetMeals();
        List<Meal> meals = new ArrayList<>();
        for (String eachInitialisedMeal : allInitialisedMeals) {
            String[] parts = eachInitialisedMeal.split("\\s*\\|\\s*");
            int minLengthToHaveIng = 2;
            if (parts.length < minLengthToHaveIng) {
                continue; // Skip initialised meal that don't have ingredients if there is any.
            }
            checkValidMeal(parts, meals);
        }
        return meals;
    }

    private static void checkValidMeal(String[] parts, List<Meal> meals) {
        try {
            checkMealsBeforeAdd(parts, meals);
        } catch (Exception exception) {
            //Throw error message if detected an ingredient with invalid price and skips to the next meal.
            System.err.println(exception.getMessage());
        }
    }

    private static void checkMealsBeforeAdd(String[] parts, List<Meal> meals)
            throws Exception {
        int mealNameIndex = 0;
        String mealName = parts[mealNameIndex];
        Meal meal = addIngredientsToMeal(mealName, parts);
        meals.add(meal);
    }

    private static Meal addIngredientsToMeal(String mealName, String[] parts)
            throws Exception {
        Meal meal = new Meal(mealName);
        // For each remaining part, extract ingredient name and its actual price.
        for (int i = 1; i < parts.length; i++) {
            try {
                Ingredient ingredient = getIngredient(parts[i]);
                meal.addIngredient(ingredient);
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }
        }
        return meal;
    }

    private static Ingredient getIngredient(String parts) throws Exception {
        String ingredientStr = parts.trim();
        int openBracketIndex = ingredientStr.indexOf("(");
        int closeBracketIndex = ingredientStr.indexOf(")");
        int notFoundIndex = -1;
        if (openBracketIndex == notFoundIndex || closeBracketIndex == notFoundIndex) {
            throw new IllegalArgumentException("Invalid ingredient format: " + ingredientStr);
        }
        int startIndex = 0;
        int indexAdjustment = 1;
        int afterOpenBracketIndex = openBracketIndex + indexAdjustment;
        String ingredientName = ingredientStr.substring(startIndex, openBracketIndex).trim();
        String priceStr = ingredientStr.substring(afterOpenBracketIndex, closeBracketIndex).trim();
        return new Ingredient(ingredientName, priceStr);
    }

    /**
     * Writes a string input to a file at the designated filePath.
     */
    public static void writeToFile(String input, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            fileWriter.append(input).append(System.lineSeparator());
        }
    }

    /**
     * Clears the contents of the file at the designated filePath.
     */
    public static void clearFile(String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
        } catch (IOException ioException) {
            throw ioException; //for the calling function to handle
        }
    }

    private static void checkValidIngredients(String line, Inventory inventory) {
        String[] parts = line.split("\\s*\\|\\s*");
        int validLength = 3;
        if (parts.length < validLength) {
            throw new IllegalArgumentException("Invalid ingredient data: " + line);
        }
        addIngredientToInventory(parts, inventory);
    }

    private static void addIngredientToInventory(String[] parts, Inventory inventory) {
        int nameIndex = 0;
        int costIndex = 1;
        int quantityIndex = 2;
        String name = parts[nameIndex];
        String price = parts[costIndex];
        int quantity = Integer.parseInt(parts[quantityIndex]);

        try {
            Ingredient newIngredient = new Ingredient(name, price);
            inventory.addIngredient(newIngredient, quantity);
        } catch (EZMealPlanException ezMealPlanException) {
            System.err.println(ezMealPlanException.getMessage());
            System.err.println("The current meal will be skipped.\n");
        }
    }
}
