package ezmealplan.exceptions;

import ezmealplan.food.list.MealList;

public class ViewIndexOutOfRangeException extends EZMealPlanException {
    int inputIndex;
    int listSize;
    String listName;

    public ViewIndexOutOfRangeException(int inputIndex, MealList mealList) {
        this.inputIndex = inputIndex;
        this.listSize = mealList.size();
        this.listName = mealList.getMealListName();
    }

    @Override
    public String getMessage() {
        return "The index provided for the " + listName + " (" + inputIndex + ") is out of range.\n" +
                "It must be between 1 and " + listSize + ".\n";
    }
}
