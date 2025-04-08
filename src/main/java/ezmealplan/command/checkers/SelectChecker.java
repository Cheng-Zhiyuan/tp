package ezmealplan.command.checkers;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.MissingMealIndexException;

public class SelectChecker extends FilterSelectChecker {
    public SelectChecker(String userInputText, String filterMethod) {
        this.userInput = userInputText.trim();
        this.lowerCaseInput = userInput.toLowerCase();
        this.filterOrSelect = "select";
        this.filterMethod = filterMethod;
    }

    @Override
    public void check() throws EZMealPlanException {
        super.check();
        setPassed(false);
        indexStringCheck();
        setPassed(true);
    }

    private void indexStringCheck() throws EZMealPlanException {
        String indexString = getIndexString().trim();
        if (indexString.isEmpty()) {
            throw new MissingMealIndexException(filterOrSelect);
        }
    }

    private String getIndexString() {
        String keyword = "";
        int afterSelectIndex = lowerCaseInput.indexOf(filterOrSelect) + filterOrSelect.length();
        keyword = switch (filterMethod) {
        case BY_ING -> ING;
        case BY_MCOST -> MCOST;
        case BY_MNAME -> MNAME;
        default -> keyword;
        };
        if (keyword.isEmpty()) {
            return userInput.substring(afterSelectIndex);
        }
        int keywordIndex = lowerCaseInput.indexOf(keyword);
        return userInput.substring(afterSelectIndex, keywordIndex);
    }
}
