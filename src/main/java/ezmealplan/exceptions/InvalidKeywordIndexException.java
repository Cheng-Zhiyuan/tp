package ezmealplan.exceptions;

public class InvalidKeywordIndexException extends EZMealPlanException {
    String recipeOrWishlist;

    public InvalidKeywordIndexException(String recipeOrWishlist) {
        this.recipeOrWishlist = recipeOrWishlist;
    }

    @Override
    public String getMessage() {
        return "'" + recipeOrWishlist + "' must be present after the 'view' keyword in the 'view' command.\n";
    }
}
