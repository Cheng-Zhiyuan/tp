package ezmealplan.command.checkers;

import ezmealplan.exceptions.EZMealPlanException;

public class FilterChecker extends FilterSelectChecker {
    public FilterChecker(String userInputText,String filterMethod) {
        this.userInput = userInputText.trim();
        this.lowerCaseInput = userInput.toLowerCase();
        this.filterOrSelect = "filter";
        this.filterMethod = filterMethod;
    }

    @Override
    public void check() throws EZMealPlanException {
        super.check();
    }
}
