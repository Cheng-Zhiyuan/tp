package ezmealplan.command;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.InvalidIngredientFormatException;
import ezmealplan.exceptions.MissingIngKeywordException;
import ezmealplan.exceptions.MissingIngredientException;

import ezmealplan.food.list.Inventory;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BuyCommandTest {
    private static final Logger logger = Logger.getLogger(BuyCommandTest.class.getName());
    private final UserInterface ui = new UserInterface();

    private final String ls = System.lineSeparator();

    public BuyCommandTest() {
        String fileName = "BuyCommandTest.log";
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

    @Test
    public void testExecute_validInputs_success() throws EZMealPlanException {
        logger.fine("Running testExecute_validInputs_success()");
        MealManager mealManager = new MealManager();
        String userInput = "buy /ing Apple (1.00), Banana(3.00)";
        Command command = new BuyCommand(userInput);
        command.execute(mealManager, ui);

        Inventory inventory = mealManager.getInventory();
        String expectedOutput = "    1. Apple ($1.00): 1" + ls + "    2. Banana ($3.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredients added");
    }

    @Test
    public void testExecute_repeatedIngredientsSamePrice_success() throws EZMealPlanException {
        logger.fine("Running testExecute_repeatedIngredientsSamePrice_success()");
        MealManager mealManager = new MealManager();
        String userInput = "buy /ing Apple (1.00), Banana (3.00), Apple (1.00)";
        Command command = new BuyCommand(userInput);
        command.execute(mealManager, ui);

        Inventory inventory = mealManager.getInventory();
        String expectedOutput = "    1. Apple ($1.00): 2" + ls + "    2. Banana ($3.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredients added");
    }

    @Test
    public void testExecute_repeatedIngredientsDifferentPrice_success() throws EZMealPlanException {
        logger.fine("Running testExecute_repeatedIngredientsDifferentPrice_success()");
        MealManager mealManager = new MealManager();
        String userInput = "buy /ing Apple (1.00), Banana (3.00), Apple (2.00)";
        Command command = new BuyCommand(userInput);
        command.execute(mealManager, ui);

        Inventory inventory = mealManager.getInventory();
        String expectedOutput = "    1. Apple ($1.00): 1" + ls + "    2. Apple ($2.00): 1" + ls
                + "    3. Banana ($3.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredients added");
    }

    @Test
    public void testExecute_missingIng_exceptionThrown() {
        logger.fine("Running testExecute_missingIng_exceptionThrown()");
        MealManager mealManager = new MealManager();
        String userInput = "buy Apple (1.00), Banana (3.00), Apple (2.00)";
        Command command = new BuyCommand(userInput);
        assertThrows(MissingIngKeywordException.class, () -> command.execute(mealManager, ui));
        logger.info("Correct exception thrown");
    }

    @Test
    public void testExecute_missingIngredient_exceptionThrown() {
        logger.fine("Running testExecute_missingIngredient_exceptionThrown()");
        MealManager mealManager = new MealManager();
        String userInput = "buy /ing";
        Command command = new BuyCommand(userInput);
        assertThrows(MissingIngredientException.class, () -> command.execute(mealManager, ui));
        logger.info("Correct exception thrown");
    }

    @Test
    public void testExecute_invalidIngredientFormat_exceptionThrown() {
        logger.fine("Running testExecute_invalidIngredientFormat_exceptionThrown()");
        MealManager mealManager = new MealManager();
        String[] userInput = {"buy /ing ing1", "buy /ing ing1 ing2", "buy /ing ing1, ing2", "buy /ing ing1 ()",
            "buy /ing ing1 1.00", "buy /ing ing1 (abc)"};
        for (String invalidInput : userInput) {
            Command command = new BuyCommand(invalidInput);
            assertThrows(InvalidIngredientFormatException.class, () -> command.execute(mealManager, ui));
        }
        logger.info("Correct exceptions thrown");
    }
}
