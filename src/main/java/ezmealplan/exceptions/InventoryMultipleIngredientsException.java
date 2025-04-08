package ezmealplan.exceptions;

import ezmealplan.food.Ingredient;

import java.util.List;

public class InventoryMultipleIngredientsException extends EZMealPlanException {
    List<Ingredient> ingredients;

    public InventoryMultipleIngredientsException(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    private String getIngredientsString() {
        String ingredientsString = "";
        for (Ingredient ingredient : ingredients) {
            ingredientsString = ingredientsString.concat(ingredient.toString() + "\n");
        }
        return ingredientsString;
    }

    @Override
    public String getMessage() {
        return "There are multiple ingredients with the same name present in the Inventory: \n" +
                getIngredientsString() + "Please refine your arguments.";
    }
}
