package ezmealplan.exceptions;

public class InvalidViewIndexException extends EZMealPlanException {
    public InvalidViewIndexException() {}

    @Override
    public String getMessage() {
        return "The meal list index in the 'view' command must be parsable into an integer.\n";
    }
}
