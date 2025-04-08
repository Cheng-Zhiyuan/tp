package ezmealplan.exceptions;

public class InvalidSelectIndexException extends EZMealPlanException {
    @Override
    public String getMessage() {
        return "The 'select' index input must be parsable into an integer and the resulting integer value " +
                "must not be out of bounds.\n";
    }
}
