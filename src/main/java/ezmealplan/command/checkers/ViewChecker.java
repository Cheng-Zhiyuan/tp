package ezmealplan.command.checkers;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.InvalidKeywordIndexException;
import ezmealplan.exceptions.InvalidViewIndexException;
import ezmealplan.exceptions.MissingMealIndexException;

public class ViewChecker extends Checker {
    private static final String VIEW = "view";
    String recipeOrWishlist;

    public ViewChecker(String userInputText, String recipeOrWishlist) {
        this.userInput = userInputText.trim();
        this.lowerCaseInput = userInput.toLowerCase();
        this.recipeOrWishlist = recipeOrWishlist;
    }

    @Override
    public void check() throws EZMealPlanException {
        checkValidKeywordIndex();
        checkMissingMealIndex();
        checkParseMealIndex();
        setPassed(true);
    }

    private void checkParseMealIndex() throws EZMealPlanException {
        try {
            int afterKeywordIndex = lowerCaseInput.indexOf(recipeOrWishlist) + recipeOrWishlist.length();
            String afterKeyword = userInput.substring(afterKeywordIndex).trim();
            Integer.parseInt(afterKeyword);
        } catch (NumberFormatException numberFormatException) {
            throw new InvalidViewIndexException();
        }
    }

    private void checkValidKeywordIndex() throws EZMealPlanException {
        int commandIndex = lowerCaseInput.indexOf(VIEW);
        int keywordIndex = lowerCaseInput.indexOf(recipeOrWishlist);
        if (commandIndex >= keywordIndex) {
            throw new InvalidKeywordIndexException(recipeOrWishlist);
        }
    }

    private void checkMissingMealIndex() throws EZMealPlanException {
        int afterKeywordIndex = lowerCaseInput.indexOf(recipeOrWishlist) + recipeOrWishlist.length();
        String afterKeyword = userInput.substring(afterKeywordIndex).trim();
        if (afterKeyword.isEmpty()) {
            throw new MissingMealIndexException(VIEW);
        }
    }

}
