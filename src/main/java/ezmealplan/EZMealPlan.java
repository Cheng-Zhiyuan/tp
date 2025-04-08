package ezmealplan;

import ezmealplan.command.Command;
import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.ParserException;
import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.food.list.MealList;
import ezmealplan.storage.Storage;
import ezmealplan.ui.UserInterface;
import ezmealplan.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class EZMealPlan {
    /**
     * Main entry-point for EZMealPlan.
     */
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final UserInterface ui = new UserInterface();
    private static final MealManager mealManager = new MealManager();

    public static void main(String[] args) {
        String fileName = "EZMealPlan.log";
        setupLogger(fileName);
        checkConstructedLists();
        mealManager.removeIllegalMeals();

        logger.fine("Running EZMealPlan");
        ui.printGreetingMessage();
        String userInput;
        while (true) {
            ui.prompt();
            userInput = ui.readInput();
            Command command = checkParsedCommand(userInput);
            if (command != null) {
                executeCommand(command);
                if (command.isExit()) {
                    break;
                }
            }
        }
        logger.fine("Exiting EZMealPlan");
    }

    private static Command checkParsedCommand(String userInput) {
        try {
            return Parser.parse(userInput);
        } catch (ParserException exception) {
            ui.printErrorMessage(exception);
            return null;
        }
    }

    /**
     * Creates and loads both main meal list (mainList.txt) and user meal list (userList.txt).
     */
    private static void checkConstructedLists() {
        try {
            Storage.createListFiles();
            Storage.loadExistingInventory(mealManager);
            constructRecipesList();
            constructWishList();
        } catch (IOException ioException) {
            System.err.println("Could not load tasks: " + ioException.getMessage());
        }
    }

    private static void constructWishList() throws IOException {
        File wishListFile = Storage.getWishListFile();
        MealList wishList = mealManager.getWishList();
        constructList(wishListFile, wishList);
    }

    private static void constructRecipesList() throws IOException {
        File recipesListFile = Storage.getRecipesListFile();
        MealList recipesList = mealManager.getRecipesList();
        constructList(recipesListFile, recipesList);
    }

    /**
     * Retrieves saved meals from the respective file and appends them into the respective MealList class.
     * If the file (mainList.txt) is empty, preset meals are appended into the RecipesList class instead.
     */
    private static void constructList(File selectedFile, MealList selectedMeals)
            throws IOException {
        List<Meal> mealList = Storage.loadExistingList(selectedFile);
        if (mealList.isEmpty() && selectedFile.equals(Storage.getRecipesListFile())) {
            mealList = Storage.loadPresetMeals();
        }
        for (Meal meal : mealList) {
            extractMealIntoList(meal, selectedMeals);
        }
    }

    private static void extractMealIntoList(Meal meal, MealList mealList) {
        try {
            mealManager.addMeal(meal, mealList);
        } catch (EZMealPlanException ezMealPlanException) {
            //Throws error message if detected an ingredient with invalid price and skips to the next meal.
            System.err.println(ezMealPlanException.getMessage());
            System.err.println("The current meal will be skipped.\n");
            logger.info("EZMealPlanException triggered");
        }
    }

    private static void executeCommand(Command command) {
        try {
            command.execute(mealManager, ui);
        } catch (EZMealPlanException ezMealPlanException) {
            ui.printErrorMessage(ezMealPlanException);
            logger.info("EZMealPlanException triggered");
        }
    }

    private static void setupLogger(String fileName) {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);
        createLogFile(fileName);
    }

    private static void createLogFile(String fileName) {
        try {
            FileHandler fileHandler = new FileHandler(fileName, true);
            fileHandler.setLevel(Level.FINE);
            logger.addHandler(fileHandler);
        } catch (IOException ioException) {
            logger.log(Level.WARNING, "File logger is not working.", ioException);
        }
    }
}
