package ezmealplan.exceptions;

//General exception for commands that requires a meal index input, but it is not found.
public class MissingMealIndexException extends EZMealPlanException {
    String command;

    public MissingMealIndexException(String command) {
        this.command = command;
    }

    @Override
    public String getMessage() {
        return "The input index cannot be missing from the '" + command + "' command.\n";}
}
