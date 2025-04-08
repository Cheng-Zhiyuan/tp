package ezmealplan.command;

import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpCommand extends Command {
    private static final String BYE = "bye";
    private static final String CREATE = "create";
    private static final String FILTER = "filter";
    private static final String SELECT = "select";
    private static final String WISHLIST = "wishlist";
    private static final String RECIPES = "recipes";
    private static final String CLEAR = "clear";
    private static final String HELP = "help";
    private static final String REMOVE = "remove";
    private static final String VIEW = "view";
    private static final String DELETE = "delete";
    private static final String RECOMMEND = "recommend";
    private static final String CONSUME = "consume";
    private static final String BUY = "buy";
    private static final String INVENTORY = "inventory";
    String commandDescription;

    public HelpCommand(String userInput) {
        String pattern = "(?i)^help\\s+(\\S+)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(userInput.trim());

        if (matcher.find()) {
            this.commandDescription = matcher.group(1).toLowerCase();
        } else {
            this.commandDescription = "";
        }
    }

    /**
     * Executes the Help command.
     *
     * @param mealManager the MealManager.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) {
        if (commandDescription.isEmpty()) {
            ui.printGeneralHelp();
            return;
        }
        switch(commandDescription) {
        case RECIPES:
            ui.printRecipesCommandHelp();
            break;
        case WISHLIST:
            ui.printWishlistCommandHelp();
            break;
        case FILTER:
            ui.printFilterCommandHelp();
            break;
        case SELECT:
            ui.printSelectCommandHelp();
            break;
        case REMOVE:
            ui.printRemoveCommandHelp();
            break;
        case CREATE:
            ui.printCreateCommandHelp();
            break;
        case DELETE:
            ui.printDeleteCommandHelp();
            break;
        case VIEW:
            ui.printViewCommandHelp();
            break;
        case CLEAR:
            ui.printClearCommandHelp();
            break;
        case BYE:
            ui.printByeCommandHelp();
            break;
        case HELP:
            ui.printHelpCommandHelp();
            break;
        case RECOMMEND:
            ui.printRecommendCommandHelp();
            break;
        case CONSUME:
            ui.printConsumeCommandHelp();
            break;
        case BUY:
            ui.printBuyCommandHelp();
            break;
        case INVENTORY:
            ui.printInventoryCommandHelp();
            break;
        default:
            ui.printUnknownCommand(commandDescription);
            break;
        }
    }
}

