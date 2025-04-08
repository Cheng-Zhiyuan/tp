package ezmealplan.exceptions;

public class InvalidViewKeywordException extends EZMealPlanException {
    public InvalidViewKeywordException() {}

    @Override
    public String getMessage() {
        return "Only 1 of the keywords '/r' (recipes list) or '/w' (wishlist) is allowed" +
                " and must be present in the 'view' command.\n";
    }
}
