package ezmealplan.exceptions;

public class InvalidIngredientFormatException extends EZMealPlanException {
    @Override
    public String getMessage() {
        return """
                The ingredient format is invalid. It should be as such:
                Ingredient_1(cost_of_ingredient_1), Ingredient_2(cost_of_ingredient_2), ... etc.
                fish(6.50), ...
                Both the ingredient and its cost must be present with the cost being enclosed within "()",
                followed by a "," to separate the ingredients.
                The cost must have at least 1 digit before the '.' example: 0.05 instead of .05.
                """;
    }
}
