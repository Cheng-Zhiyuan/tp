package ezmealplan.exceptions;

public class MissingIngredientException extends EZMealPlanException {
    String command;

    public MissingIngredientException(String command) {
        this.command = command;
    }

    @Override
    public String getMessage() {
        return "The ingredient (s) cannot be missing from the " + command + " command.\n" +
                "It must be present after the '/ing' keyword.\n";
    }
}
