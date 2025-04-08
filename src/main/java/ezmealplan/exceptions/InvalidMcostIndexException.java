package ezmealplan.exceptions;

public class InvalidMcostIndexException extends EZMealPlanException {
    String filterOrSelect;

    public InvalidMcostIndexException(String filterOrSelect) {
        this.filterOrSelect = filterOrSelect;
    }

    @Override
    public String getMessage() {
        return "The /mcost keyword must appear after the '" + filterOrSelect + "' keyword command.\n";
    }
}
