package ezmealplan.exceptions;

import ezmealplan.food.Meal;

public class MealNotFoundException extends EZMealPlanException {
    Meal notFoundMeal;

    public MealNotFoundException(Meal notFoundMeal) {
        this.notFoundMeal = notFoundMeal;
    }

    @Override
    public String getMessage() {
        return "The meal provided (" + notFoundMeal + ") cannot be found in the list.\n";
    }
}
