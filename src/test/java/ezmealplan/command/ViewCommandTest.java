package ezmealplan.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.EmptyListException;
import ezmealplan.exceptions.ViewIndexOutOfRangeException;
import ezmealplan.food.Ingredient;
import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

public class ViewCommandTest {
    private static final Logger logger = Logger.getLogger(ViewCommandTest.class.getName());

    @BeforeAll
    public static void setupLogger() {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler("ViewCommandTest.log", true);
            fileHandler.setLevel(Level.FINE);
            logger.addHandler(fileHandler);
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "File logger setup failed", exception.getMessage());
        }
    }

    /**
     * Define a UserInterface class for testing, to capture the params of printMealList.
     */
    public static class TestUserInterface extends UserInterface {
        Meal capturedMeal;
        List<Ingredient> capturedIngredients;

        @Override
        public void printIngredientList(Meal meal) {
            this.capturedMeal = meal;
            this.capturedIngredients = meal.getIngredientList();
        }
    }

    @Test
    public void testExecute_viewRecipeMeal_success() throws EZMealPlanException {
        logger.fine("Running testExecute_viewRecipeMeal_success()");

        MealManager mealManager = new MealManager();
        Meal meal1 = new Meal("Recipes Meal 1");
        Ingredient firstIngredient = new Ingredient("egg", "0.50");
        Ingredient secondIngredient = new Ingredient("rice", "1.00");
        meal1.addIngredient(firstIngredient);
        meal1.addIngredient(secondIngredient);

        mealManager.getRecipesList().getList().add(meal1);

        ViewCommandTest.TestUserInterface testUI = new ViewCommandTest.TestUserInterface();
        ViewCommand viewCommand = new ViewCommand("view /r 1");
        viewCommand.execute(mealManager, testUI);

        assertEquals("Recipes Meal 1 ($1.50)", testUI.capturedMeal.toString());
        List<Ingredient> expectedIngredients = new ArrayList<>();
        expectedIngredients.add(firstIngredient);
        expectedIngredients.add(secondIngredient);
        expectedIngredients.sort(Comparator.comparing(Ingredient::getName,
                String.CASE_INSENSITIVE_ORDER).thenComparing(Ingredient::getPrice));
        assertIterableEquals(expectedIngredients, testUI.capturedIngredients);

        logger.info("testExecute_viewRecipeMeal_success passed");
    }


    @Test
    public void testExecute_viewWishlistMeal_success() throws EZMealPlanException {
        logger.fine("Running testExecute_viewWishlistMeal_success()");
        MealManager mealManager = new MealManager();
        Meal meal = new Meal("Wishlist Meal 1");
        Ingredient firstIngredient = new Ingredient("tofu", "1.20");
        Ingredient secondIngredient = new Ingredient("noodles", "1.80");
        meal.addIngredient(firstIngredient);
        meal.addIngredient(secondIngredient);

        mealManager.getWishList().getList().add(meal);

        TestUserInterface testUI = new TestUserInterface();
        ViewCommand command = new ViewCommand("view /w 1");
        command.execute(mealManager, testUI);

        assertEquals("Wishlist Meal 1 ($3.00)", testUI.capturedMeal.toString());
        List<Ingredient> expectedIngredients = new ArrayList<>();
        expectedIngredients.add(firstIngredient);
        expectedIngredients.add(secondIngredient);
        expectedIngredients.sort(Comparator.comparing(Ingredient::getName,
                String.CASE_INSENSITIVE_ORDER).thenComparing(Ingredient::getPrice));
        assertIterableEquals(expectedIngredients, testUI.capturedIngredients);
        logger.info("testExecute_viewWishlistMeal_success passed");
    }

    @Test
    public void testExecute_wrongKeyword_exceptionThrown() throws EZMealPlanException {
        logger.fine("Running testExecute_wrongKeyword_exceptionThrown()");
        MealManager mealManager = new MealManager();
        Meal meal = new Meal("Wishlist Meal 1");
        Ingredient firstIngredient = new Ingredient("tofu", "1.20");
        Ingredient secondIngredient = new Ingredient("noodles", "1.80");
        meal.addIngredient(firstIngredient);
        meal.addIngredient(secondIngredient);

        mealManager.getWishList().getList().add(meal);

        TestUserInterface testUI = new TestUserInterface();
        ViewCommand command = new ViewCommand("view /u 1");
        try {
            command.execute(mealManager, testUI);
        } catch (EZMealPlanException ezMealPlanException) {
            String expectedMessage = "Only 1 of the keywords '/r' (recipes list) or '/w' (wishlist) is allowed" +
                    " and must be present in the 'view' command.\n";
            assertEquals(expectedMessage, ezMealPlanException.getMessage());
        }
        logger.info("testExecute_wrongKeyword_exceptionThrown passed");
    }

    @Test
    public void testExecute_viewRecipesMeal_emptyList(){
        logger.fine("Running testExecute_viewRecipesMeal_emptyList()");
        MealManager mealManager = new MealManager();
        TestUserInterface testUI = new TestUserInterface();
        ViewCommand command = new ViewCommand("view /r 1");
        assertThrows(EmptyListException.class, () -> command.execute(mealManager, testUI));

        logger.info("testExecute_viewRecipesMeal_emptyList passed");
    }

    @Test
    public void testExecute_viewWishlistMeal_emptyList(){
        logger.fine("Running testExecute_viewWishlistMeal_emptyList()");
        MealManager mealManager = new MealManager();
        TestUserInterface testUI = new TestUserInterface();
        ViewCommand command = new ViewCommand("view /w 5");
        assertThrows(EmptyListException.class, () -> command.execute(mealManager, testUI));

        logger.info("testExecute_viewWishlistMeal_emptyList passed");
    }

    @Test
    public void testExecute_viewRecipesMeal_outOfRange() throws EZMealPlanException {
        logger.fine("Running testExecute_viewRecipesMeal_outOfRange()");
        MealManager mealManager = new MealManager();
        Meal meal = new Meal("Recipes Meal 1");
        Ingredient firstIngredient = new Ingredient("tofu", "1.20");
        Ingredient secondIngredient = new Ingredient("noodles", "1.80");
        meal.addIngredient(firstIngredient);
        meal.addIngredient(secondIngredient);
        mealManager.getRecipesList().getList().add(meal);

        TestUserInterface testUI = new TestUserInterface();
        ViewCommand command = new ViewCommand("view /r 2");

        try {
            command.execute(mealManager, testUI);
        } catch (EZMealPlanException ezMealPlanException) {
            String expectedMessage = "The index provided for the recipes list (2) is out of range.\n" +
                    "It must be between 1 and 1.\n";
            assertEquals(expectedMessage, ezMealPlanException.getMessage());
        }
        assertThrows(ViewIndexOutOfRangeException.class, () -> command.execute(mealManager,testUI));
        logger.info("testExecute_viewRecipesMeal_outOfRange passed");
    }

    @Test
    public void testExecute_viewWishlistMeal_outOfRange() throws EZMealPlanException{
        logger.fine("Running testExecute_viewWishlistMeal_outOfRange()");
        MealManager mealManager = new MealManager();
        Meal meal = new Meal("Wishlist Meal 1");
        Ingredient firstIngredient = new Ingredient("tofu", "1.20");
        Ingredient secondIngredient = new Ingredient("noodles", "1.80");
        meal.addIngredient(firstIngredient);
        meal.addIngredient(secondIngredient);
        mealManager.getWishList().getList().add(meal);

        TestUserInterface testUI = new TestUserInterface();
        ViewCommand command = new ViewCommand("view /w 2");

        assertThrows(ViewIndexOutOfRangeException.class, () -> command.execute(mealManager,testUI));

        logger.info("testExecute_viewWishlistMeal_outOfRange passed");
    }
}


