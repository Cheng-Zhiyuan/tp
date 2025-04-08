package ezmealplan.exceptions;

public class MissingMealNameException extends EZMealPlanException {
    String command;

    public MissingMealNameException(String command) {
        this.command = command;
    }

    public String getMessage() {
        String mainMessage = "The meal name cannot be missing from the '" + command + "' command.\n" +
                "It must be present after the '/mname' keyword";
        String subMessage = ".\n";
        String create = "create";
        if (command.equals(create)) {
            subMessage = " and before '/ing' keyword.\n";
        }
        return mainMessage + subMessage;
    }
}
