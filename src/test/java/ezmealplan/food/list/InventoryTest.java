package ezmealplan.food.list;

import ezmealplan.food.Ingredient;
import org.junit.jupiter.api.Test;
import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.IngredientPriceFormatException;
import ezmealplan.exceptions.InvalidPriceException;
import ezmealplan.exceptions.InventoryIngredientNotFound;
import ezmealplan.exceptions.InventoryMultipleIngredientsException;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class InventoryTest {
    private static final Logger logger = Logger.getLogger(InventoryTest.class.getName());

    private final Ingredient ingredient1;
    private final Ingredient ingredient2;
    private final Ingredient ingredient3;
    private final Ingredient ingredient4;
    private final Ingredient ingredient5;

    private final String ls = System.lineSeparator();

    public InventoryTest() throws InvalidPriceException, IngredientPriceFormatException {
        String fileName = "MealListTest.log";
        setupLogger(fileName);
        ingredient1 = new Ingredient("Apple", "1.00");
        ingredient2 = new Ingredient("Apple", "2.00");
        ingredient3 = new Ingredient("Banana", "3.00");
        ingredient4 = new Ingredient("Chocolate", "4.00");
        ingredient5 = new Ingredient("Apple", "1.00");
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
    void addIngredient_differentIngredients_success() {
        logger.fine("Running addIngredient_differentIngredients_success()");
        Inventory inventory = new Inventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String expectedOutput = "    1. Apple ($1.00): 1" + ls + "    2. Banana ($3.00): 1" + ls
                + "    3. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredients and quantities");
    }

    @Test
    void addIngredient_repeatedIngredientsSamePrice_repetitionCaptured() {
        logger.fine("Running addIngredient_repeatedIngredientsSamePrice_repetitionCaptured()");
        Inventory inventory = new Inventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient5);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String expectedOutput = "    1. Apple ($1.00): 2" + ls + "    2. Banana ($3.00): 1" + ls
                + "    3. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredients and quantities");
    }

    @Test
    void addIngredient_repeatedIngredientsDifferentPrice_success() {
        logger.fine("Running addIngredient_repeatedIngredientsDifferentPrice_success()");
        Inventory inventory = new Inventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient2);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        String expectedOutput = "    1. Apple ($1.00): 1" + ls + "    2. Apple ($2.00): 1" + ls
                + "    3. Banana ($3.00): 1" + ls + "    4. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredients and quantities");
    }

    @Test
    void removeIngredient_uniqueIngredients_success() throws InventoryMultipleIngredientsException,
            InventoryIngredientNotFound {
        logger.fine("Running removeIngredient_uniqueIngredients_success()");
        Inventory inventory = new Inventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        inventory.removeIngredient("Apple");
        String expectedOutput = "    1. Banana ($3.00): 1" + ls + "    2. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredients and quantities");
    }

    @Test
    void removeIngredient_repeatedIngredientSamePrice_success() throws InventoryMultipleIngredientsException,
            InventoryIngredientNotFound {
        logger.fine("Running removeIngredient_repeatedIngredientSamePrice_success()");
        Inventory inventory = new Inventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient5);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        inventory.removeIngredient("Apple");
        String expectedOutput = "    1. Apple ($1.00): 1" + ls + "    2. Banana ($3.00): 1" + ls
                + "    3. Chocolate ($4.00): 1" + ls;
        assertEquals(expectedOutput, inventory.toString());
        logger.info("Correct ingredients and quantities");
    }

    @Test
    void removeIngredient_repeatedIngredientDifferentPrice_exceptionThrown() {
        logger.fine("Running removeIngredient_repeatedIngredientDifferentPrice_exceptionThrown()");
        Inventory inventory = new Inventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient2);
        inventory.addIngredient(ingredient3);
        inventory.addIngredient(ingredient4);
        try {
            inventory.removeIngredient("Apple");
            fail();
        } catch (EZMealPlanException ezMealPlanException) {
            String expectedMessage = "There are multiple ingredients with the same name present in the Inventory: \n" +
                    "Apple ($1.00)\n" + "Apple ($2.00)\n" + "Please refine your arguments.";
            assertEquals(expectedMessage, ezMealPlanException.getMessage());
            logger.info("Correct exception thrown");
        }
    }

    @Test
    void removeIngredient_ingredientDoesNotExist_exceptionThrown() {
        logger.fine("Running removeIngredient_ingredientDoesNotExist_exceptionThrown()");
        Inventory inventory = new Inventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        try {
            inventory.removeIngredient("Chocolate");
            fail();
        } catch (EZMealPlanException ezMealPlanException) {
            assertEquals("Chocolate not found in Inventory", ezMealPlanException.getMessage());
            logger.info("Correct exception thrown");
        }
    }

    @Test
    void hasIngredient_ingredientExists_returnsTrue() {
        logger.fine("Running hasIngredient_ingredientExists_returnsTrue()");
        Inventory inventory = new Inventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        assertTrue(inventory.hasIngredient(ingredient1));
        assertTrue(inventory.hasIngredient(ingredient5));
        logger.info("Ingredient exists");
    }

    @Test
    void hasIngredient_ingredientDoesNotExist_returnsFalse() {
        logger.fine("Running hasIngredient_ingredientDoesNotExist_returnsFalse()");
        Inventory inventory = new Inventory();
        inventory.addIngredient(ingredient1);
        inventory.addIngredient(ingredient3);
        assertFalse(inventory.hasIngredient(ingredient2));
        assertFalse(inventory.hasIngredient(ingredient4));
        logger.info("Ingredient does not exist");
    }
}
