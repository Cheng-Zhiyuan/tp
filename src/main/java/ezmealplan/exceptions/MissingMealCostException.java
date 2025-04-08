package ezmealplan.exceptions;

public class MissingMealCostException extends EZMealPlanException {
    String filterOrSelect;

    public MissingMealCostException(String filterOrSelect) {
        this.filterOrSelect = filterOrSelect;
    }

    public String getMessage() {
        return "The meal cost cannot be missing from the '" + filterOrSelect + "' command.\n" +
                "It must be present after the '/mcost' keyword.\n";
    }
}
