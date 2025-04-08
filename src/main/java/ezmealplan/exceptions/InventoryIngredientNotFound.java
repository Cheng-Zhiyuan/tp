package ezmealplan.exceptions;

public class InventoryIngredientNotFound extends EZMealPlanException {
    String ingredientName;

    public InventoryIngredientNotFound(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    @Override
    public String getMessage() {
        return ingredientName + " not found in Inventory";
    }
}
