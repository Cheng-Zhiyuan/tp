package ezmealplan.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ezmealplan.exceptions.MissingIngKeywordException;
import ezmealplan.exceptions.MissingIngredientException;
import ezmealplan.food.Ingredient;
import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

public class RecommendCommandTest {

    private static final Logger logger = Logger.getLogger(RecommendCommandTest.class.getName());

    @BeforeAll
    public static void setupLogger() {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler("RecommendCommandTest.log", true);
            fileHandler.setLevel(Level.FINE);
            logger.addHandler(fileHandler);
        } catch (IOException ioException) {
            logger.log(Level.SEVERE, "Logger setup failed", ioException);
        }
    }

    public static class TestUI extends UserInterface {
        public String capturedMessage = null;

        @Override
        public void printMessage(String message) {
            this.capturedMessage = message;
        }
    }

    @Test
    public void recommendCommand_matchingIngredientInWishlist_success() throws Exception {
        logger.fine("Running recommendCommand_matchingIngredientInWishlist_success()");
        MealManager mealManager = new MealManager();
        mealManager.getWishList().getList().clear();
        mealManager.getRecipesList().getList().clear();

        Meal meal = new Meal("Salmon Rice");
        meal.addIngredient(new Ingredient("salmon", "2.50"));
        mealManager.getWishList().addMeal(meal);

        TestUI ui = new TestUI();
        RecommendCommand command = new RecommendCommand("recommend /ing salmon");
        command.execute(mealManager, ui);

        StringBuilder sb = new StringBuilder();
        sb.append("Recommended Meal: ").append("Salmon Rice")
                .append(" (").append("Salmon Rice ($2.50)").append(")")
                .append(System.lineSeparator());
        sb.append("Ingredients:").append(System.lineSeparator());
        sb.append("   ").append(1).append(". ").append("salmon ($2.50)")
                .append(System.lineSeparator());
        sb.append("Missing Ingredients: salmon");
        assertEquals(sb.toString(), ui.capturedMessage);
        logger.info("recommendCommand_matchingIngredientInWishlist_success passed");
    }

    @Test
    public void recommendCommand_matchingIngredientInRecipes_success() throws Exception {
        logger.fine("Running recommendCommand_matchingIngredientInRecipes_success()");
        MealManager mealManager = new MealManager();
        mealManager.getWishList().getList().clear();
        mealManager.getRecipesList().getList().clear();

        Meal meal = new Meal("Tofu Soup");
        meal.addIngredient(new Ingredient("tofu", "1.20"));
        mealManager.getRecipesList().getList().add(meal);

        TestUI ui = new TestUI();
        RecommendCommand command = new RecommendCommand("recommend /ing tofu");
        command.execute(mealManager, ui);

        StringBuilder sb = new StringBuilder();
        sb.append("Recommended Meal: ").append("Tofu Soup")
                .append(" (").append("Tofu Soup ($1.20)").append(")")
                .append(System.lineSeparator());
        sb.append("Ingredients:").append(System.lineSeparator());
        sb.append("   ").append(1).append(". ").append("tofu ($1.20)")
                .append(System.lineSeparator());
        sb.append("Missing Ingredients: tofu");
        assertEquals(sb.toString(), ui.capturedMessage);
        logger.info("recommendCommand_matchingIngredientInRecipes_success passed");
    }

    @Test
    public void recommendCommand_ingredientNotFound_printsNoMatch() throws Exception {
        logger.fine("Running recommendCommand_ingredientNotFound_printsNoMatch()");
        MealManager mealManager = new MealManager();
        mealManager.getWishList().getList().clear();
        mealManager.getRecipesList().getList().clear();

        TestUI ui = new TestUI();
        RecommendCommand command = new RecommendCommand("recommend /ing icecream");
        command.execute(mealManager, ui);

        assertEquals("No meal found containing ingredient: icecream", ui.capturedMessage);
        logger.info("recommendCommand_ingredientNotFound_printsNoMatch passed");
    }

    @Test
    public void recommendCommand_nonMatchingCase_successfulMatch() throws Exception {
        logger.fine("Running recommendCommand_nonMatchingCase_successfulMatch()");
        MealManager mealManager = new MealManager();
        mealManager.getWishList().getList().clear();
        mealManager.getRecipesList().getList().clear();

        Meal meal = new Meal("Miso Soup");
        Ingredient miso = new Ingredient("miso", "1.00");
        meal.addIngredient(miso);
        mealManager.getRecipesList().getList().add(meal);

        TestUI ui = new TestUI();
        RecommendCommand command = new RecommendCommand("recommend /ing MiSo");
        command.execute(mealManager, ui);

        StringBuilder sb = new StringBuilder();
        sb.append("Recommended Meal: ").append("Miso Soup")
                .append(" (").append("Miso Soup ($1.00)").append(")")
                .append(System.lineSeparator());
        sb.append("Ingredients:").append(System.lineSeparator());
        sb.append("   ").append(1).append(". ").append("miso ($1.00)")
                .append(System.lineSeparator());
        sb.append("Missing Ingredients: miso");
        assertEquals(sb.toString(), ui.capturedMessage);
        logger.info("recommendCommand_nonMatchingCase_successfulMatch passed");
    }

    @Test
    public void recommendCommand_missingKeyword_throwsRecommendFormatException() {
        logger.fine("Running recommendCommand_missingKeyword_throwsRecommendFormatException()");
        RecommendCommand command = new RecommendCommand("recommend salmon");
        assertThrows(MissingIngKeywordException.class,
                () -> command.execute(new MealManager(), new TestUI()));
        logger.info("recommendCommand_missingKeyword_throwsRecommendFormatException passed");
    }

    @Test
    public void recommendCommand_missingIngAfterKeyword_throwsRecommendFormatException() {
        logger.fine("Running recommendCommand_missingIngredientAfterKeyword_throwsRecommendFormatException()");
        RecommendCommand command = new RecommendCommand("recommend /ing");
        assertThrows(MissingIngredientException.class,
                () -> command.execute(new MealManager(), new TestUI()));
        logger.info("recommendCommand_missingIngredientAfterKeyword_throwsRecommendFormatException passed");
    }
}
