package ezmealplan.command;

import java.io.IOException;
import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.EmptyListException;
import ezmealplan.exceptions.RemoveFormatException;
import ezmealplan.exceptions.RemoveIndexOutOfRangeException;

import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.food.list.MealList;
import ezmealplan.ui.UserInterface;

import org.junit.jupiter.api.Test;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RemoveCommandTest {
    private static final Logger logger = Logger.getLogger(RemoveCommandTest.class.getName());
    private final UserInterface testUI = new TestUI();

    public RemoveCommandTest() {
        String fileName = "RemoveCommandTest.log";
        setupLogger(fileName);
    }

    private static void setupLogger(String fileName) {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);
        createLogFile(fileName);
    }

    private static void createLogFile(String fileName) {
        try {
            FileHandler fileHandler = new FileHandler(fileName, true);
            fileHandler.setLevel(Level.FINE);
            logger.addHandler(fileHandler);
        } catch (IOException ioException) {
            logger.log(Level.SEVERE, "File logger is not working.", ioException);
        }
    }

    /**
     * Minimal UserInterface stub to suppress delete output.
     */
    public static class TestUI extends UserInterface {
        @Override
        public void printRemovedMessage(Meal removedMeal, int newSize) {
            // Suppress output
        }
    }

    @Test
    public void removeCommand_validIndex_success() throws EZMealPlanException {
        logger.fine("Running removeCommand_validIndex_success()");
        MealManager mealManager = new MealManager();
        Meal testMeal = new Meal("Egg Fried Rice");
        MealList wishList = mealManager.getWishList();
        wishList.addMeal(testMeal);
        assertEquals(1, wishList.size());

        Command command = new RemoveCommand("remove 1");
        command.execute(mealManager, testUI);

        assertEquals(0, wishList.size());
        logger.info("Meal successfully removed from wish list");
    }

    @Test
    public void removeCommand_extraSpacingInput_success() throws EZMealPlanException {
        logger.fine("Running removeCommand_extraSpacingInput_success()");
        MealManager mealManager = new MealManager();
        Meal testMeal = new Meal("Soup");
        MealList wishList = mealManager.getWishList();
        wishList.addMeal(testMeal);
        assertEquals(1, wishList.size());

        Command command = new RemoveCommand("   remove     1   ");
        command.execute(mealManager, testUI);

        assertEquals(0, wishList.size());
        logger.info("Meal successfully removed from wish list");
    }

    @Test
    public void removeCommand_emptyList_throwsEmptyListException() throws EZMealPlanException {
        logger.fine("Running removeCommand_emptyList_throwsRemoveFormatException()");
        MealManager mealManager = new MealManager();

        String[] userInputs = {"remove 1", "remove 0", "remove -1"};
        for (String userInput : userInputs) {
            Command command = new RemoveCommand(userInput);
            assertThrows(EmptyListException.class, () -> command.execute(mealManager, testUI));
            logger.info("Correct exception thrown");
        }
    }

    @Test
    public void removeCommand_indexOutOfRange_throwsRemoveIndexOutOfRangeException() throws EZMealPlanException {
        logger.fine("Running removeCommand_indexOutOfRange_throwsRemoveIndexOutOfRangeException()");
        MealManager mealManager = new MealManager();
        Meal testMeal = new Meal("Soup");
        MealList wishList = mealManager.getWishList();
        wishList.addMeal(testMeal);

        String[] userInputs = {"remove 2", "remove 0", "remove -1"};
        for (String userInput : userInputs) {
            Command command = new RemoveCommand(userInput);
            assertThrows(RemoveIndexOutOfRangeException.class, () -> command.execute(mealManager, testUI));
            logger.info("Correct exception thrown");
        }
    }

    @Test
    public void removeCommand_missingIndex_throwsRemoveFormatException() throws EZMealPlanException {
        logger.fine("Running removeCommand_missingIndex_throwsRemoveFormatException()");
        MealManager mealManager = new MealManager();
        Meal testMeal = new Meal("Soup");
        MealList wishList = mealManager.getWishList();
        wishList.addMeal(testMeal);

        String[] userInputs = {"remove", "remove  "};
        for (String userInput : userInputs) {
            Command command = new RemoveCommand(userInput);
            assertThrows(RemoveFormatException.class, () -> command.execute(mealManager, testUI));
            logger.info("Correct exception thrown");
        }
    }

    @Test
    public void removeCommand_nonIntegerIndex_throwsRemoveFormatException() throws EZMealPlanException {
        logger.fine("Running removeCommand_nonIntegerIndex_throwsRemoveFormatException()");
        MealManager mealManager = new MealManager();
        Meal testMeal = new Meal("Soup");
        MealList wishList = mealManager.getWishList();
        wishList.addMeal(testMeal);

        String[] userInputs = {"remove abc", "remove abc 1", "remove 1.11", "remove -1.98"};
        for (String userInput : userInputs) {
            Command command = new RemoveCommand(userInput);
            assertThrows(RemoveFormatException.class, () -> command.execute(mealManager, testUI));
            logger.info("Correct exception thrown");
        }
    }
}
