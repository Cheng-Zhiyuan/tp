package ezmealplan.exceptions;

public class InvalidMnameIndexException extends EZMealPlanException {
    String filterOrSelect;

    public InvalidMnameIndexException(String filterOrSelect) {
        this.filterOrSelect = filterOrSelect;
    }

    @Override
    public String getMessage() {
        return "The /mname keyword must appear after the '" + filterOrSelect + "' keyword command.\n";
    }
}
