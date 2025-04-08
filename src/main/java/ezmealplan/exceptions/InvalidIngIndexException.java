package ezmealplan.exceptions;

public class InvalidIngIndexException extends EZMealPlanException {
    String filterOrSelect;

    public InvalidIngIndexException(String filterOrSelect) {
        this.filterOrSelect = filterOrSelect;
    }

    @Override
    public String getMessage() {
        return "The /ing keyword must appear after the '" + filterOrSelect + "' keyword command.\n";
    }
}
