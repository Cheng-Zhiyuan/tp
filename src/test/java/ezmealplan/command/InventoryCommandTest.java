package ezmealplan.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.food.Ingredient;
import ezmealplan.food.list.Inventory;
import ezmealplan.logic.MealManager;
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

class InventoryCommandTest {
    private static final Logger logger = Logger.getLogger(InventoryCommandTest.class.getName());
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final UserInterface ui = new UserInterface();
    private final String ls = System.lineSeparator();

    public InventoryCommandTest() {
        String fileName = "InventoryCommandTest.log";
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
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut); // Restore original System.out
        outContent.reset();         // Reset captured output
    }

    @Test
    public void testExecute_inventoryCommand_printsInventory() throws EZMealPlanException {
        logger.fine("Running testExecute_inventoryCommand_printsInventory()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        Ingredient ingredient1 = new Ingredient("Apple", "1.00");
        Ingredient ingredient2 = new Ingredient("Apple", "2.00");
        Ingredient ingredient3 = new Ingredient("Banana", "3.00");
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient2);
        inventory.addIngredient(ingredient1, 2);

        Command command = new InventoryCommand();
        command.execute(mealManager, ui);

        String expectedString = "Here are the ingredients that you own:" + ls + "    1. Apple ($1.00): 2" + ls
                + "    2. Apple ($2.00): 1" + ls + "    3. Banana ($3.00): 1" + ls;
        assertEquals(expectedString.trim(), outContent.toString().trim());
        logger.info("Correct inventory printed");
    }

    @Test
    public void testExecute_emptyInventory_printsEmptyInventory() throws EZMealPlanException {
        logger.fine("Running testExecute_emptyInventory_printsEmptyInventory()");
        MealManager mealManager = new MealManager();

        Command command = new InventoryCommand();
        command.execute(mealManager, ui);

        String expectedString = "No ingredients found in your inventory.";
        assertEquals(expectedString, outContent.toString().trim());
        logger.info("Empty inventory printed");
    }
}
