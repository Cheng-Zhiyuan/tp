package ezmealplan.command.checkers;

import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.exceptions.InvalidIngIndexException;
import ezmealplan.exceptions.InvalidMcostIndexException;
import ezmealplan.exceptions.InvalidMnameIndexException;
import ezmealplan.exceptions.MissingIngredientException;
import ezmealplan.exceptions.MissingMealCostException;
import ezmealplan.exceptions.MissingMealNameException;

import java.util.logging.Logger;

public abstract class FilterSelectChecker extends Checker {
    protected static final String ING = "/ing";
    protected static final String MNAME = "/mname";
    protected static final String MCOST = "/mcost";
    protected static final String BY_ING = "byIng";
    protected static final String BY_MNAME = "byMname";
    protected static final String BY_MCOST = "byMcost";
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    String filterOrSelect;
    String filterMethod;


    @Override
    public void check() throws EZMealPlanException {
        logger.fine("Checking '" + userInput + "' for errors.");
        checkFilterMethodFormat();
        setPassed(true);
    }

    private void checkFilterMethodFormat() throws EZMealPlanException {
        switch (filterMethod) {
        case BY_ING:
            checkIngIndex();
            checkIngFormat();
            break;
        case BY_MNAME:
            checkMnameIndex();
            checkMnameFormat();
            break;
        case BY_MCOST:
            checkMcostIndex();
            checkMcostFormat();
            break;
        default:
            break;
        }
    }

    private void checkMnameIndex() throws EZMealPlanException {
        int commandIndex = lowerCaseInput.indexOf(filterOrSelect);
        int mnameIndex = lowerCaseInput.indexOf(MNAME);
        if (commandIndex >= mnameIndex) {
            throw new InvalidMnameIndexException(filterOrSelect);
        }
    }

    private void checkIngIndex() throws EZMealPlanException {
        int commandIndex = lowerCaseInput.indexOf(filterOrSelect);
        int ingIndex = lowerCaseInput.indexOf(ING);
        if (commandIndex >= ingIndex) {
            throw new InvalidIngIndexException(filterOrSelect);
        }
    }

    private void checkMcostIndex() throws EZMealPlanException {
        int commandIndex = lowerCaseInput.indexOf(filterOrSelect);
        int mcostIndex = lowerCaseInput.indexOf(MCOST);
        if (commandIndex >= mcostIndex) {
            throw new InvalidMcostIndexException(filterOrSelect);
        }
    }

    private void checkMcostFormat() throws EZMealPlanException {
        int afterMcostIndex = lowerCaseInput.indexOf(MCOST) + MCOST.length();
        String afterMcost = this.userInput.substring(afterMcostIndex);
        if (afterMcost.isEmpty()) {
            throw new MissingMealCostException(filterOrSelect);
        }
    }

    private void checkMnameFormat() throws EZMealPlanException {
        int afterMnameIndex = lowerCaseInput.indexOf(MNAME) + MNAME.length();
        String afterMname = this.userInput.substring(afterMnameIndex);
        if (afterMname.isEmpty()) {
            throw new MissingMealNameException(filterOrSelect);
        }
    }

    private void checkIngFormat() throws EZMealPlanException {
        int afterIngIndex = lowerCaseInput.indexOf(ING) + ING.length();
        String afterIng = this.userInput.substring(afterIngIndex);
        if (afterIng.isEmpty()) {
            throw new MissingIngredientException(filterOrSelect);
        }
    }
}

