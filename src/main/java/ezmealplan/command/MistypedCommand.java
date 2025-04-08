package ezmealplan.command;

import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

public class MistypedCommand extends Command {
    private final String actualCommand;

    public MistypedCommand(String userInputText, String actualCommand) {
        validUserInput = userInputText;
        this.actualCommand = actualCommand;
    }

    /**
     * Executes the Mistyped command.
     *
     * @param mealManager the MealManager.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) {
        ui.printMistypedCommand(validUserInput, actualCommand);
    }
}
