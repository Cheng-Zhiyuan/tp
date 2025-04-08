package ezmealplan.exceptions;

public class InvalidPriceException extends EZMealPlanException {
    String productName;

    public InvalidPriceException(String productName) {
        this.productName = productName;
    }

    public String getMessage() {
        return "The price of " + this.productName + " must be between 0.00 and 9999999999999.99"
               +" (both sides inclusive).\n";
    }
}
