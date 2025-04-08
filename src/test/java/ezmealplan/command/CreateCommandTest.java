package ezmealplan.command;

import ezmealplan.exceptions.DuplicateIngredientException;
import ezmealplan.exceptions.DuplicateMealException;
import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.IngredientPriceFormatException;
import ezmealplan.exceptions.InvalidIngredientFormatException;
import ezmealplan.exceptions.InvalidPriceException;
import ezmealplan.food.Ingredient;
import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class CreateCommandTest {
    private static final Logger logger = Logger.getLogger(CreateCommandTest.class.getName());
    final UserInterface ui = new UserInterface();

    public CreateCommandTest() {
        String fileName = "CreateCommandTest.log";
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
    public void createCommand_success() throws EZMealPlanException {
        MealManager mealManager = new MealManager();
        logger.fine("running createCommand_success()");
        String validUserInput = "create /mname chicken rice /ing chicken breast (2.50)," +
                                " rice (1.50), egg(0.50), cucumber(1.00)";
        Command command = new CreateCommand(validUserInput);
        command.execute(mealManager, ui);
        int mealListSize = mealManager.getRecipesList().size();
        int expectedMealListSize = 1;
        assertEquals(expectedMealListSize, mealListSize);
        checkExpectedStrings(mealManager);
        logger.info("create_success() test passed");
    }

    @Test
    public void createCommand_notDuplicateMeal_success() throws EZMealPlanException {
        MealManager mealManager = new MealManager();
        String firstInput = "create /mname chicken rice /ing chicken breast (2.50)," +
                " rice (1.50), egg (0.50), cucumber (1.00)";
        String secondInput = "create /mname chicken rice /ing chicken breast(2.50)," +
                " rice(1.50), egg(0.50), cucumber(1.01)";
        String thirdInput = "create /mname chicken rice /ing chicken breast(2.00)," +
                " rice(2.00), egg(0.40), cucumber(1.10)";
        String fourthInput = "create /mname chicken rice /ing chicken breast(2.50)," +
                " rice(1.50), egg(0.50), cucumber(1.00), tomato(2.00)";
        String fifthInput = "create /mname hainanese chicken rice /ing chicken breast(2.00)," +
                " rice(2.00), egg(0.40), cucumber(1.10), tomato(2.00)";
        String[] userInputs = {firstInput, secondInput, thirdInput, fourthInput, fifthInput};
        for (String userInput : userInputs) {
            Command command = new CreateCommand(userInput);
            command.execute(mealManager, ui);
        }
        assertEquals(5, mealManager.getRecipesList().size());
    }

    @Test
    public void createCommand_fail() throws EZMealPlanException {
        logger.fine("running createCommand_fail()");
        duplicate_ingredient_catch();
        duplicate_meal_catch();
        invalidPriceFormat();
        logger.info("createCommand_fail() test passed");
    }

    private void invalidPriceFormat() {
        checkPrices_cannotParsed_intoInteger();
        check_negative_prices();
        check_invalidIngredientPriceFormat();
    }

    private void check_invalidIngredientPriceFormat() {
        MealManager mealManager = new MealManager();
        String[] invalidPrices = {"create /mname test /ing ing(-1.000)", "create /mname test /ing ing(2.000)"
                , "create /mname test /ing ing(-0.5)"
                ,"create /mname test /ing ing(0.5)", "create /mname test /ing ing(1.5)"
                , "create /mname test /ing ing(-0.)", "create /mname test /ing ing(9.)"};
        for (String invalidPrice : invalidPrices) {
            Command command = new CreateCommand(invalidPrice);
            try {
                command.execute(mealManager, ui);
            } catch (EZMealPlanException ezMealPlanException) {
                String ingName = "ing";
                assertEquals(new IngredientPriceFormatException(ingName).getMessage(),
                        ezMealPlanException.getMessage());
                logger.info("Matching exception caught for check_invalidIngredientPriceFormat()");
            }
        }
    }

    private void check_negative_prices() {
        MealManager mealManager = new MealManager();
        String[] negativePrices = {"create /mname test /ing ing(-1.00)", "create /mname test /ing ing(-0.01)"
                , "create /mname test /ing ing(-0.52)"};
        for (String negativePrice : negativePrices) {
            Command command = new CreateCommand(negativePrice);
            try {
                command.execute(mealManager, ui);
            } catch (EZMealPlanException ezMealPlanException) {
                String ingName = "ing";
                assertEquals(new InvalidPriceException(ingName).getMessage(),
                        ezMealPlanException.getMessage());
                logger.info("Matching exception caught for check_negative_prices()");
            }
        }
    }

    private void checkPrices_cannotParsed_intoInteger() {
        MealManager mealManager = new MealManager();
        String[] invalidPrices = {"create /mname test /ing ing(jj)", "create /mname test /ing ing(.d)"
                , "create /mname test /ing ing(s)"};
        for (String invalidPrice : invalidPrices) {
            Command command = new CreateCommand(invalidPrice);
            try {
                command.execute(mealManager, ui);
            } catch (EZMealPlanException ezMealPlanException) {
                assertEquals(new InvalidIngredientFormatException().getMessage(), ezMealPlanException.getMessage());
                logger.info("Matching exception caught for checkPrices_cannotParsed_intoInteger()");
            }
        }
    }

    private void checkExpectedStrings(MealManager mealManager) {
        int zeroIndex = 0;
        Meal meal = mealManager.getRecipesList().getList().get(zeroIndex);
        String expectedMealString = "chicken rice ($5.50)";
        assertEquals(expectedMealString, meal.toString());
        String mealStringMsg = "Matching meal string.";
        logger.info(mealStringMsg);
        ArrayList<Ingredient> ingredientList = (ArrayList<Ingredient>) meal.getIngredientList();
        String[] expectedIngredientList = {"chicken breast ($2.50)", "cucumber ($1.00)", "egg ($0.50)", "rice ($1.50)"};
        for (int i = 0; i < expectedIngredientList.length; i++) {
            assertEquals(expectedIngredientList[i], ingredientList.get(i).toString());
            String ingStringMsg = "Matching ingredient string.";
            logger.info(ingStringMsg);

        }
    }

    private void duplicate_meal_catch() throws EZMealPlanException {
        MealManager mealManager = new MealManager();
        String userInput = "create /mname chicken rice /ing chicken breast(2.50)," +
                " rice(1.50), egg(0.50), cucumber(1.00)";
        Command command1 = new CreateCommand(userInput);
        command1.execute(mealManager, ui);

        String fineMsg = "Running duplicate_meal_catch().";
        logger.fine(fineMsg);
        try {
            Command command2 = new CreateCommand(userInput);
            command2.execute(mealManager, ui);
        } catch (EZMealPlanException ezMealPlanException) {
            String chickenRice = "chicken rice";
            String listName = mealManager.getRecipesList().getMealListName();
            assertEquals(new DuplicateMealException(chickenRice, listName).getMessage()
                    , ezMealPlanException.getMessage());
            String dupMealMsg = "Duplicate meal caught!";
            logger.info(dupMealMsg);
        }
    }

    private void duplicate_ingredient_catch() {
        MealManager mealManager = new MealManager();
        String userInput = "create /mname chicken breast /ing chicken breast(2.50)," +
                           " chicken breast(1.50)";
        Command command = new CreateCommand(userInput);

        String fineMsg = "Running duplicate_ingredient_catch().";
        logger.fine(fineMsg);
        try {
            command.execute(mealManager, ui);
            fail();
        } catch (EZMealPlanException ezMealPlanException) {
            String chickenBreast = "chicken breast";
            assertEquals(new DuplicateIngredientException(chickenBreast, chickenBreast).getMessage()
                    , ezMealPlanException.getMessage());
            String dupIngMsg = "Duplicate ingredients caught!";
            logger.info(dupIngMsg);
        }
    }
}
