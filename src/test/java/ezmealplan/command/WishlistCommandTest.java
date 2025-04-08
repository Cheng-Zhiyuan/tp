//@@author olsonwangyj
package ezmealplan.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

public class WishlistCommandTest {
    private static final Logger logger = Logger.getLogger(WishlistCommandTest.class.getName());

    public WishlistCommandTest() {
        String fileName = "WishlistCommandTest.log";
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
     * Defines a UserInterface class for testing, to capture the params of printMealList.
     */
    public static class TestUserInterface extends UserInterface {
        List<Meal> capturedMeals;
        String capturedListName;

        @Override
        public void printMealList(List<Meal> meals, String mealListName) {

            this.capturedMeals = new ArrayList<>(meals);
            this.capturedListName = mealListName;
        }
    }

    @Test
    public void testExecute_wishlistCommand_printsUserChosenMeals() throws EZMealPlanException {
        logger.fine("running testExecute_wishlistCommand_printsUserChosenMeals()");
        MealManager mealManager = new MealManager();
        Meal meal1 = new Meal("Meal A");
        Meal meal2 = new Meal("Meal B");
        mealManager.getWishList().getList().add(meal1);
        mealManager.getWishList().getList().add(meal2);

        TestUserInterface testUI = new TestUserInterface();
        WishlistCommand wishlistCommand = new WishlistCommand();
        wishlistCommand.execute(mealManager, testUI);

        assertEquals(mealManager.getWishList().getMealListName(), testUI.capturedListName);
        List<Meal> expectedMeals = new ArrayList<>();
        expectedMeals.add(meal1);
        expectedMeals.add(meal2);
        assertIterableEquals(expectedMeals, testUI.capturedMeals);
        logger.fine("testExecute_wishlistCommand_printsUserChosenMeals() passed");
    }
}
