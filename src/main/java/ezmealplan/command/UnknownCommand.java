package ezmealplan.command;

import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;

public class UnknownCommand extends Command {
    public UnknownCommand(String userInputText) {
        validUserInput = userInputText;
    }

    /**
     * Executes the Unknown command.
     *
     * @param mealManager the MealManager.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) {
        ui.printUnknownCommand(validUserInput);
    }
}
