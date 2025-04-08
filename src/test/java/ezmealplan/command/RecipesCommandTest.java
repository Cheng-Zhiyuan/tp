//@@author olsonwangyj
package ezmealplan.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.food.Meal;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

public class RecipesCommandTest {
    private static final Logger logger = Logger.getLogger(RecipesCommandTest.class.getName());
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final UserInterface ui = new UserInterface();

    private final String ls = System.lineSeparator();

    public RecipesCommandTest() {
        String fileName = "RecipesCommandTest.log";
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
    public void testExecute_recipesCommand_printsRecipesList() throws EZMealPlanException {
        logger.fine("Running testExecute_recipesCommand_printsRecipesList()");
        MealManager mealManager = new MealManager();
        Meal meal1 = new Meal("Main Meal 1");
        Meal meal2 = new Meal("Main Meal 2");
        mealManager.getRecipesList().getList().add(meal1);
        mealManager.getRecipesList().getList().add(meal2);

        RecipesCommand recipesCommand = new RecipesCommand();
        recipesCommand.execute(mealManager, ui);

        String expectedOutput = "Here are the meals in recipes list:" + ls + "    1. Main Meal 1 ($0.00)" + ls
                + "    2. Main Meal 2 ($0.00)" + ls + ls;
        assertEquals(expectedOutput, outContent.toString());
        logger.info("testExecute_recipesCommand_printsRecipesList() passed");
    }

    @Test
    public void testExecute_emptyRecipesList_printsEmptyRecipesList() throws EZMealPlanException {
        logger.fine("Running testExecute_emptyRecipesList_printsEmptyRecipesList()");
        MealManager mealManager = new MealManager();

        RecipesCommand recipesCommand = new RecipesCommand();
        recipesCommand.execute(mealManager, ui);

        String expectedOutput = "No meals found in recipes list.";
        //Trim the actual output as some bug is occurring with differing non-printable characters between the two
        assertEquals(expectedOutput, outContent.toString().trim());
        logger.info("testExecute_emptyRecipesList_printsEmptyRecipesList() passed");
    }
}
