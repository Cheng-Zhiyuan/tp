package ezmealplan.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.food.list.MealList;
import ezmealplan.ui.UserInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ClearCommandTest {

    private static final Logger logger = Logger.getLogger(ClearCommandTest.class.getName());
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private UserInterface ui;
    private MealManager mealManager;

    // Use the system line separator in expected outputs
    private final String ls = System.lineSeparator();

    public ClearCommandTest() {
        String fileName = "ClearCommandTest.log";
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

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent)); // Redirect System.out to capture output
        ui = new UserInterface();
        mealManager = new MealManager();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut); // Restore original System.out
        outContent.reset();         // Reset captured output
    }

    /**
     * Tests that the ClearCommand clears the wishlist and prints the expected message.
     */
    @Test
    void clearsWishList_noInputs_printsMessage() throws EZMealPlanException {
        logger.fine("running execute_clearsWishList_printsMessage()");

        // Prepare wishlist with some meals
        MealList wishList = mealManager.getWishList();
        wishList.addMeal(new Meal("Chicken Rice"));
        wishList.addMeal(new Meal("Pasta"));

        // Ensure wishList is not empty before clearing
        assertEquals(2, wishList.getList().size(), "Wishlist should contain 2 meals before clearing.");

        // Execute ClearCommand
        ClearCommand clearCommand = new ClearCommand();
        clearCommand.execute(mealManager, ui);

        // Verify wishList is now empty
        assertTrue(wishList.getList().isEmpty(), "Wishlist should be empty after ClearCommand.");

        // Verify output
        String expectedOutput = "All meals cleared from your wishlist!" + ls;
        assertEquals(expectedOutput, outContent.toString(), "Command output does not match expected message.");

        logger.info("execute_clearsWishList_printsMessage() passed");
    }
}
