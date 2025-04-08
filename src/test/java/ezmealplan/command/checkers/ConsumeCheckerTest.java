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
import ezmealplan.exceptions.MissingIngKeywordException;
import ezmealplan.exceptions.MissingIngredientException;

public class ConsumeCheckerTest {

    private static final Logger logger = Logger.getLogger(ConsumeCheckerTest.class.getName());

    @BeforeAll
    public static void setupLogger() {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler("ConsumeCheckerTest.log", true);
            fileHandler.setLevel(Level.FINE);
            logger.addHandler(fileHandler);
        } catch (IOException ioException) {
            logger.log(Level.SEVERE, "Failed to set up file logger", ioException);
        }
    }

    @Test
    public void consumeChecker_validInput_success() throws EZMealPlanException {
        logger.fine("Running consumeChecker_validInput_success()");
        ConsumeChecker checker = new ConsumeChecker("consume /ing Bread, Milk");
        checker.check();
        logger.info("consumeChecker_validInput_success passed");
    }

    @Test
    public void consumeChecker_missingIngKeyword_throwsMissingIngKeywordException() {
        logger.fine("Running consumeChecker_missingIngKeyword_throwsMissingIngKeywordException()");
        ConsumeChecker checker = new ConsumeChecker("consume Milk, Bread");
        assertThrows(MissingIngKeywordException.class, checker::check);
        logger.info("consumeChecker_missingIngKeyword_throwsMissingIngKeywordException passed");
    }

    @Test
    public void consumeChecker_noIngredients_throwsMissingIngredientException() {
        logger.fine("Running consumeChecker_noIngredients_throwsMissingIngredientException()");
        ConsumeChecker checker = new ConsumeChecker("consume /ing");
        assertThrows(MissingIngredientException.class, checker::check);
        logger.info("consumeChecker_noIngredients_throwsMissingIngredientException passed");
    }

    @Test
    public void consumeChecker_extraWhitespace_success() throws EZMealPlanException {
        logger.fine("Running consumeChecker_extraWhitespace_success()");
        ConsumeChecker checker = new ConsumeChecker("   consume   /ing   Eggs,   Cheese  ");
        checker.check();
        logger.info("consumeChecker_extraWhitespace_success passed");
    }

    @Test
    public void consumeChecker_numericIngredientNames_success() throws EZMealPlanException {
        logger.fine("Running consumeChecker_numericIngredientNames_success()");
        ConsumeChecker checker = new ConsumeChecker("consume /ing 123, 456");
        checker.check();
        logger.info("consumeChecker_numericIngredientNames_success passed");
    }

    @Test
    public void consumeChecker_specialCharIngredients_success() throws EZMealPlanException {
        logger.fine("Running consumeChecker_specialCharIngredients_success()");
        ConsumeChecker checker = new ConsumeChecker("consume /ing @egg$, #tofu%, milk!");
        checker.check();
        logger.info("consumeChecker_specialCharIngredients_success passed");
    }

    @Test
    public void consumeChecker_duplicateIngredients_success() throws EZMealPlanException {
        logger.fine("Running consumeChecker_duplicateIngredients_success()");
        ConsumeChecker checker = new ConsumeChecker("consume /ing Bread, Bread, Bread");
        checker.check();
        logger.info("consumeChecker_duplicateIngredients_success passed");
    }

    @Test
    public void consumeChecker_newlineAndTab_success() throws EZMealPlanException {
        logger.fine("Running consumeChecker_newlineAndTab_success()");
        ConsumeChecker checker = new ConsumeChecker("consume /ing Bread,\n\tMilk");
        checker.check();
        logger.info("consumeChecker_newlineAndTab_success passed");
    }

    @Test
    public void consumeChecker_singleIngredientOnly_success() throws EZMealPlanException {
        logger.fine("Running consumeChecker_singleIngredientOnly_success()");
        ConsumeChecker checker = new ConsumeChecker("consume /ing Water");
        checker.check();
        logger.info("consumeChecker_singleIngredientOnly_success passed");
    }

    @Test
    public void consumeChecker_caseInsensitiveKeyword_success() throws EZMealPlanException {
        logger.fine("Running consumeChecker_caseInsensitiveKeyword_success()");
        ConsumeChecker checker = new ConsumeChecker("CONSUME /ING Milk");
        checker.check();
        logger.info("consumeChecker_caseInsensitiveKeyword_success passed");
    }
}
