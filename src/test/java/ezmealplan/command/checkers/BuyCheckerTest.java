package ezmealplan.command.checkers;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.InvalidIngredientFormatException;
import ezmealplan.exceptions.MissingIngKeywordException;
import ezmealplan.exceptions.MissingIngredientException;

public class BuyCheckerTest {

    private static final Logger logger = Logger.getLogger(BuyCheckerTest.class.getName());

    @BeforeAll
    public static void setupLogger() {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler("BuyCheckerTest.log", true);
            fileHandler.setLevel(Level.FINE);
            logger.addHandler(fileHandler);
        } catch (IOException ioException) {
            logger.log(Level.SEVERE, "Failed to set up file logger", ioException);
        }
    }

    @Test
    public void buyChecker_singleIngredientInput_success() throws EZMealPlanException {
        logger.fine("Running buyChecker_singleIngredientInput_success()");
        BuyChecker checker = new BuyChecker("buy /ing Egg (1.00)");
        checker.check();
        logger.info("buyChecker_singleIngredientInput_success passed");
    }

    @Test
    public void buyChecker_multipleIngredientInput_success() throws EZMealPlanException {
        logger.fine("Running buyChecker_multipleIngredientInput_success()");
        BuyChecker checker = new BuyChecker("buy /ing Egg (1.00), Milk (2.50)");
        checker.check();
        logger.info("buyChecker_multipleIngredientInput_success passed");
    }

    @Test
    public void buyChecker_missingIngKeyword_throwsMissingIngKeywordException() {
        logger.fine("Running buyChecker_missingIngKeyword_throwsMissingIngKeywordException()");
        BuyChecker checker = new BuyChecker("buy Egg (1.00)");
        assertThrows(MissingIngKeywordException.class, checker::check);
        logger.info("buyChecker_missingIngKeyword_throwsMissingIngKeywordException passed");
    }

    @Test
    public void buyChecker_noIngredientsProvided_throwsMissingIngredientException() {
        logger.fine("Running buyChecker_noIngredientsProvided_throwsMissingIngredientException()");
        BuyChecker checker = new BuyChecker("buy /ing");
        assertThrows(MissingIngredientException.class, checker::check);
        logger.info("buyChecker_noIngredientsProvided_throwsMissingIngredientException passed");
    }

    @Test
    public void buyChecker_invalidIngredientFormat_throwsInvalidIngredientFormatException() {
        logger.fine("Running buyChecker_invalidIngredientFormat_throwsInvalidIngredientFormatException()");
        BuyChecker checker = new BuyChecker("buy /ing Egg=1.00");
        assertThrows(InvalidIngredientFormatException.class, checker::check);
        logger.info("buyChecker_invalidIngredientFormat_throwsInvalidIngredientFormatException passed");
    }

    @Test
    public void buyChecker_negativePriceAllowed_success() throws EZMealPlanException {
        logger.fine("Running buyChecker_negativePriceAllowed_success()");
        BuyChecker checker = new BuyChecker("buy /ing Spoiled Milk (-0.99)");
        checker.check();
        logger.info("buyChecker_negativePriceAllowed_success passed");
    }

    @Test
    public void buyChecker_extraSpacing_success() throws EZMealPlanException {
        logger.fine("Running buyChecker_extraSpacing_success()");
        BuyChecker checker = new BuyChecker("buy   /ing   Bread (0.50)  ,  Cheese (1.25)");
        checker.check();
        logger.info("buyChecker_extraSpacing_success passed");
    }
}
