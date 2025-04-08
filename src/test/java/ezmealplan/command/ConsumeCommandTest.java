package ezmealplan.command;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.IngredientPriceFormatException;
import ezmealplan.exceptions.InvalidIngredientFormatException;
import ezmealplan.exceptions.InvalidPriceException;
import ezmealplan.exceptions.InventoryIngredientNotFound;
import ezmealplan.exceptions.InventoryMultipleIngredientsException;
import ezmealplan.exceptions.MissingIngKeywordException;
import ezmealplan.exceptions.MissingIngredientException;

import ezmealplan.food.Ingredient;
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

class ConsumeCommandTest {
    private static final Logger logger = Logger.getLogger(ConsumeCommandTest.class.getName());
    private final UserInterface ui = new UserInterface();
    private final String ls = System.lineSeparator();

    private final Ingredient ingredient1;
    private final Ingredient ingredient2;
    private final Ingredient ingredient3;
    private final Ingredient ingredient4;

    public ConsumeCommandTest() throws InvalidPriceException, IngredientPriceFormatException {
        ingredient1 = new Ingredient("Apple", "1.00");
        ingredient2 = new Ingredient("Apple", "2.00");
        ingredient3 = new Ingredient("Banana", "3.00");
        ingredient4 = new Ingredient("Chocolate", "4.00");

        String fileName = "ConsumeCommandTest.log";
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
    public void testExecute_validInput_success() throws EZMealPlanException {
        logger.fine("Running testExecute_validInput_success()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String userInput = "consume /ing apple";
        Command command = new ConsumeCommand(userInput);
        command.execute(mealManager, ui);

        String expectedOutput = "    1. Banana ($3.00): 1" + ls + "    2. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredient consumed");
    }

    @Test
    public void testExecute_ingredientNotFound_exceptionThrown() {
        logger.fine("testExecute_ingredientNotFound_exceptionThrown()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String userInput = "consume /ing duck";
        Command command = new ConsumeCommand(userInput);

        assertThrows(InventoryIngredientNotFound.class, () -> command.execute(mealManager, ui));
        logger.info("Correct exception thrown");
    }

    @Test
    public void testExecute_validInputWithPriceInput_success() throws EZMealPlanException {
        logger.fine("Running testExecute_validInput_success()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String userInput = "consume /ing apple (1.00)";
        Command command = new ConsumeCommand(userInput);
        command.execute(mealManager, ui);

        String expectedOutput = "    1. Banana ($3.00): 1" + ls + "    2. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredient consumed");
    }

    @Test
    public void testExecute_validInputWithMixedInput_success() throws EZMealPlanException {
        logger.fine("Running testExecute_validInput_success()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String userInput = "consume /ing apple (1.00), Banana";
        Command command = new ConsumeCommand(userInput);
        command.execute(mealManager, ui);

        String expectedOutput = "    1. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredients consumed");
    }

    @Test
    public void testExecute_ingredientNotFoundWithPriceInput_exceptionThrown() {
        logger.fine("Running testExecute_ingredientNotFoundWithPriceInput_exceptionThrown()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String userInput = "consume /ing apple (3.00)";
        Command command = new ConsumeCommand(userInput);

        assertThrows(InventoryIngredientNotFound.class, () -> command.execute(mealManager, ui));
        logger.info("Correct exception thrown");
    }

    @Test
    public void testExecute_validInputMultipleConsumed_success() throws EZMealPlanException {
        logger.fine("Running testExecute_validInputMultipleConsumed_success()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String userInput = "consume /ing apple, Banana";
        Command command = new ConsumeCommand(userInput);
        command.execute(mealManager, ui);

        String expectedOutput = "    1. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredients consumed");
    }

    @Test
    public void testExecute_duplicateIngredientsSamePrice_success() throws EZMealPlanException {
        logger.fine("Running testExecute_duplicateIngredientSamePrice_success()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String userInput = "consume /ing apple";
        Command command = new ConsumeCommand(userInput);
        command.execute(mealManager, ui);

        String expectedOutput = "    1. Apple ($1.00): 2" + ls + "    2. Banana ($3.00): 1" + ls
                + "    3. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredient consumed");
    }

    @Test
    public void testExecute_duplicateIngredientsDifferentPrice_exceptionThrown() throws EZMealPlanException {
        logger.fine("Running testExecute_duplicateIngredientDifferentPrice_exceptionThrown()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient2);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String userInput1 = "consume /ing Banana";
        Command command1 = new ConsumeCommand(userInput1);
        command1.execute(mealManager, ui);

        String expectedOutput = "    1. Apple ($1.00): 2" + ls + "    2. Apple ($2.00): 1" + ls
                + "    3. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredient consumed");

        String userInput2 = "consume /ing apple";
        Command command2 = new ConsumeCommand(userInput2);

        assertThrows(InventoryMultipleIngredientsException.class, () -> command2.execute(mealManager, ui));
        logger.info("Correct exception thrown when attempting to remove ingredient with multiple prices");
    }

    @Test
    public void testExecute_duplicateIngredientsDifferentPriceWithPriceInput_success() throws EZMealPlanException {
        logger.fine("Running testExecute_duplicateIngredientDifferentPriceWithPriceInput_exceptionThrown()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient2);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String userInput = "consume /ing apple (1.00)";
        Command command = new ConsumeCommand(userInput);
        command.execute(mealManager, ui);

        String expectedOutput = "    1. Apple ($1.00): 1" + ls + "    2. Apple ($2.00): 1" + ls
                + "    3. Banana ($3.00): 1" + ls + "    4. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredient consumed");
    }

    @Test
    public void testExecute_duplicateIngredientsDifferentPriceMultipleConsumed_exceptionThrown() {
        logger.fine("Running testExecute_duplicateIngredientsDifferentPriceMultipleConsumed_exceptionThrown()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient2);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String userInput1 = "consume /ing apple, banana";
        Command command1 = new ConsumeCommand(userInput1);

        assertThrows(InventoryMultipleIngredientsException.class, () -> command1.execute(mealManager, ui));
        logger.info("Correct exception thrown");

        String userInput2 = "consume /ing banana, apple";
        Command command2 = new ConsumeCommand(userInput2);

        assertThrows(InventoryMultipleIngredientsException.class, () -> command2.execute(mealManager, ui));
        logger.info("Correct exception thrown");
    }

    @Test
    public void testExecute_duplicateIngredientsDifferentPriceMultipleConsumedWithPriceInput_exceptionThrown()
            throws EZMealPlanException {
        logger.fine("Running testExecute_duplicateIngredientsDifferentPriceMultipleConsumedWithPriceInput" +
                "_exceptionThrown()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient2);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String userInput = "consume /ing apple (1.00), apple (2.00)";
        Command command = new ConsumeCommand(userInput);
        command.execute(mealManager, ui);

        String expectedOutput = "    1. Apple ($1.00): 1" + ls + "    2. Banana ($3.00): 1" + ls +
                "    3. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredient consumed");
    }

    @Test
    public void testExecute_missingIng_exceptionThrown() {
        logger.fine("Running testExecute_missingIng_exceptionThrown()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        String[] userInputs = {"consume", "consume Apple", "consume Apple, Banana"};
        for (String userInput : userInputs) {
            Command command = new BuyCommand(userInput);
            assertThrows(MissingIngKeywordException.class, () -> command.execute(mealManager, ui));
        }
        logger.info("Correct exception thrown");
    }

    @Test
    public void testExecute_missingIngredient_exceptionThrown() {
        logger.fine("Running testExecute_missingIngredient_exceptionThrown()");
        MealManager mealManager = new MealManager();
        String userInput = "consume /ing";
        Command command = new ConsumeCommand(userInput);
        assertThrows(MissingIngredientException.class, () -> command.execute(mealManager, ui));
        logger.info("Correct exception thrown");
    }

    @Test
    public void testExecute_invalidPriceInput_exceptionThrown() {
        logger.fine("Running testExecute_invalidPriceInput_exceptionThrown()");
        MealManager mealManager = new MealManager();
        Inventory inventory = mealManager.getInventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        String[] userInputs = {"consume /ing apple ()", "consume /ing apple (1.00) banana", "consume /ing apple (abc)"};
        for (String userInput : userInputs) {
            Command command = new ConsumeCommand(userInput);
            assertThrows(InvalidIngredientFormatException.class, () -> command.execute(mealManager, ui));
        }
    }
}
