package ezmealplan.command.checkers;

import ezmealplan.exceptions.EZMealPlanException;

public abstract class Checker {
    protected String userInput;
    protected String lowerCaseInput;
    protected boolean isPassed = false;

    /**
     * Checks the userInput based on each checker's internal checks.
     */
    public abstract void check() throws EZMealPlanException;

    public boolean isPassed() {
        return isPassed;
    }

    protected void setPassed(boolean passed) {
        isPassed = passed;
    }
}
