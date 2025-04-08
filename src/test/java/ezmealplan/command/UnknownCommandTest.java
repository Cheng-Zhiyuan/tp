package ezmealplan.command;


import ezmealplan.exceptions.EZMealPlanException;
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UnknownCommandTest {
    private static final Logger logger = Logger.getLogger(UnknownCommandTest.class.getName());
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final MealManager mealManager = new MealManager();
    private final UserInterface ui = new UserInterface();

    private final String ls = System.lineSeparator();

    public UnknownCommandTest() {
        String fileName = "UnknownCommandTest.log";
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
    public void testExecute_unknownCommand_printsCorrectMessage() throws EZMealPlanException {
        logger.fine("Running testExecute_unknownCommand_printsCorrectMessage()");
        String userInput = "wrong command";
        Command command = new UnknownCommand(userInput);

        command.execute(mealManager, ui);

        String expectedOutput = "Invalid command: wrong command" + ls + "me no understand what you talking." + ls;
        assertEquals(expectedOutput, outContent.toString());
        logger.info("Correct unknown command printed");
    }
}
