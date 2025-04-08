package ezmealplan.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelpCommandTest {
    private static final Logger logger = Logger.getLogger(HelpCommandTest.class.getName());
    private final MealManager mealManager = new MealManager();
    private UserInterface ui;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    public HelpCommandTest() {
        String fileName = "HelpCommandTest.log";
        setupLogger(fileName);
    }

    private static void setupLogger(String fileName) {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);
        try {
            FileHandler fileHandler = new FileHandler(fileName, true);
            fileHandler.setLevel(Level.FINE);
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "File logger is not working.", e);
        }
    }

    /**
     * A test implementation of the UserInterface to capture output messages.
     */
    private class TestUI extends UserInterface {
        @Override
        public void printGeneralHelp() {
            System.out.print("General Help");
        }

        @Override
        public void printRecipesCommandHelp() {
            System.out.print("Recipes Help");
        }

        @Override
        public void printWishlistCommandHelp() {
            System.out.print("Wishlist Help");
        }

        @Override
        public void printFilterCommandHelp() {
            System.out.print("Filter Help");
        }

        @Override
        public void printSelectCommandHelp() {
            System.out.print("Select Help");
        }

        @Override
        public void printRemoveCommandHelp() {
            System.out.print("Remove Help");
        }

        @Override
        public void printCreateCommandHelp() {
            System.out.print("Create Help");
        }

        @Override
        public void printDeleteCommandHelp() {
            System.out.print("Delete Help");
        }

        @Override
        public void printViewCommandHelp() {
            System.out.print("View Help");
        }

        @Override
        public void printClearCommandHelp() {
            System.out.print("Clear Help");
        }

        @Override
        public void printByeCommandHelp() {
            System.out.print("Bye Help");
        }

        @Override
        public void printHelpCommandHelp() {
            System.out.print("Help Help");
        }

        @Override
        public void printRecommendCommandHelp() {
            System.out.print("Recommend Help");
        }

        @Override
        public void printConsumeCommandHelp() {
            System.out.print("Consume Help");
        }

        @Override
        public void printBuyCommandHelp() {
            System.out.print("Buy Help");
        }

        @Override
        public void printInventoryCommandHelp() {
            System.out.print("Inventory Help");
        }

        @Override
        public void printUnknownCommand(String command) {
            System.out.print("Unknown command: " + command);
        }
    }

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture printed output for testing.
        System.setOut(new PrintStream(outContent));
        ui = new TestUI();
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out after each test and reset captured output.
        System.setOut(originalOut);
        outContent.reset();
    }

    // helpCommand_recipesInput_printsRecipesHelp
    @Test
    public void helpCommand_recipesInput_printsRecipesHelp() {
        logger.fine("running helpCommand_recipesInput_printsRecipesHelp");
        HelpCommand command = new HelpCommand("help recipes");
        command.execute(mealManager, ui);
        assertEquals("Recipes Help", outContent.toString());
        logger.fine("helpCommand_recipesInput_printsRecipesHelp() passed");
    }

    // helpCommand_wishlistInput_printsWishlistHelp
    @Test
    public void helpCommand_wishlistInput_printsWishlistHelp() {
        logger.fine("running helpCommand_wishlistInput_printsWishlistHelp");
        HelpCommand command = new HelpCommand("help wishlist");
        command.execute(mealManager, ui);
        assertEquals("Wishlist Help", outContent.toString());
        logger.fine("helpCommand_wishlistInput_printsWishlistHelp() passed");
    }

    // helpCommand_filterInput_printsFilterHelp
    @Test
    public void helpCommand_filterInput_printsFilterHelp() {
        logger.fine("running helpCommand_filterInput_printsFilterHelp");
        HelpCommand command = new HelpCommand("help filter");
        command.execute(mealManager, ui);
        assertEquals("Filter Help", outContent.toString());
        logger.fine("helpCommand_filterInput_printsFilterHelp() passed");
    }

    // helpCommand_selectInput_printsSelectHelp
    @Test
    public void helpCommand_selectInput_printsSelectHelp() {
        logger.fine("running helpCommand_selectInput_printsSelectHelp");
        HelpCommand command = new HelpCommand("help select");
        command.execute(mealManager, ui);
        assertEquals("Select Help", outContent.toString());
        logger.fine("helpCommand_selectInput_printsSelectHelp() passed");
    }

    // helpCommand_removeInput_printsRemoveHelp
    @Test
    public void helpCommand_removeInput_printsRemoveHelp() {
        logger.fine("running helpCommand_removeInput_printsRemoveHelp");
        HelpCommand command = new HelpCommand("help remove");
        command.execute(mealManager, ui);
        assertEquals("Remove Help", outContent.toString());
        logger.fine("helpCommand_removeInput_printsRemoveHelp() passed");
    }

    // helpCommand_createInput_printsCreateHelp
    @Test
    public void helpCommand_createInput_printsCreateHelp() {
        logger.fine("running helpCommand_createInput_printsCreateHelp");
        HelpCommand command = new HelpCommand("help create");
        command.execute(mealManager, ui);
        assertEquals("Create Help", outContent.toString());
        logger.fine("helpCommand_createInput_printsCreateHelp() passed");
    }

    // helpCommand_deleteInput_printsDeleteHelp
    @Test
    public void helpCommand_deleteInput_printsDeleteHelp() {
        logger.fine("running helpCommand_deleteInput_printsDeleteHelp");
        HelpCommand command = new HelpCommand("help delete");
        command.execute(mealManager, ui);
        assertEquals("Delete Help", outContent.toString());
        logger.fine("helpCommand_deleteInput_printsDeleteHelp() passed");
    }

    // helpCommand_viewInput_printsViewHelp
    @Test
    public void helpCommand_viewInput_printsViewHelp() {
        logger.fine("running helpCommand_viewInput_printsViewHelp");
        HelpCommand command = new HelpCommand("help view");
        command.execute(mealManager, ui);
        assertEquals("View Help", outContent.toString());
        logger.fine("helpCommand_viewInput_printsViewHelp() passed");
    }

    // helpCommand_clearInput_printsClearHelp
    @Test
    public void helpCommand_clearInput_printsClearHelp() {
        logger.fine("running helpCommand_clearInput_printsClearHelp");
        HelpCommand command = new HelpCommand("help clear");
        command.execute(mealManager, ui);
        assertEquals("Clear Help", outContent.toString());
        logger.fine("helpCommand_clearInput_printsClearHelp() passed");
    }

    // helpCommand_byeInput_printsByeHelp
    @Test
    public void helpCommand_byeInput_printsByeHelp() {
        logger.fine("running helpCommand_byeInput_printsByeHelp");
        HelpCommand command = new HelpCommand("help bye");
        command.execute(mealManager, ui);
        assertEquals("Bye Help", outContent.toString());
        logger.fine("helpCommand_byeInput_printsByeHelp() passed");
    }

    // helpCommand_helpInput_printsHelpHelp
    @Test
    public void helpCommand_helpInput_printsHelpHelp() {
        logger.fine("running helpCommand_helpInput_printsHelpHelp");
        HelpCommand command = new HelpCommand("help help");
        command.execute(mealManager, ui);
        assertEquals("Help Help", outContent.toString());
        logger.fine("helpCommand_helpInput_printsHelpHelp() passed");
    }

    // helpCommand_recommendInput_printsRecommendHelp
    @Test
    public void helpCommand_recommendInput_printsRecommendHelp() {
        logger.fine("running helpCommand_recommendInput_printsRecommendHelp");
        HelpCommand command = new HelpCommand("help recommend");
        command.execute(mealManager, ui);
        assertEquals("Recommend Help", outContent.toString());
        logger.fine("helpCommand_recommendInput_printsRecommendHelp() passed");
    }

    // helpCommand_consumeInput_printsConsumeHelp
    @Test
    public void helpCommand_consumeInput_printsConsumeHelp() {
        logger.fine("running helpCommand_consumeInput_printsConsumeHelp");
        HelpCommand command = new HelpCommand("help consume");
        command.execute(mealManager, ui);
        assertEquals("Consume Help", outContent.toString());
        logger.fine("helpCommand_consumeInput_printsConsumeHelp() passed");
    }

    // helpCommand_buyInput_printsBuyHelp
    @Test
    public void helpCommand_buyInput_printsBuyHelp() {
        logger.fine("running helpCommand_buyInput_printsBuyHelp");
        HelpCommand command = new HelpCommand("help buy");
        command.execute(mealManager, ui);
        assertEquals("Buy Help", outContent.toString());
        logger.fine("helpCommand_buyInput_printsBuyHelp() passed");
    }

    // helpCommand_inventoryInput_printsInventoryHelp
    @Test
    public void helpCommand_inventoryInput_printsInventoryHelp() {
        logger.fine("running helpCommand_inventoryInput_printsInventoryHelp");
        HelpCommand command = new HelpCommand("help inventory");
        command.execute(mealManager, ui);
        assertEquals("Inventory Help", outContent.toString());
        logger.fine("helpCommand_inventoryInput_printsInventoryHelp() passed");
    }

    // helpCommand_unknownInput_printsUnknownCommand
    @Test
    public void helpCommand_unknownInput_printsUnknownCommand() {
        logger.fine("running helpCommand_unknownInput_printsUnknownCommand");
        HelpCommand command = new HelpCommand("help notacommand");
        command.execute(mealManager, ui);
        assertEquals("Unknown command: notacommand", outContent.toString());
        logger.fine("helpCommand_unknownInput_printsUnknownCommand() passed");
    }
}
