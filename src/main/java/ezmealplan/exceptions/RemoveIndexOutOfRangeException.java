package ezmealplan.exceptions;

public class RemoveIndexOutOfRangeException extends EZMealPlanException {
    int inputIndex;
    int listSize;

    public RemoveIndexOutOfRangeException(int inputIndex, int listSize) {
        this.inputIndex = inputIndex;
        this.listSize = listSize;
    }

    @Override
    public String getMessage() {
        return "The index provided (" + inputIndex + ") is out of range. It must be between 1 and " + listSize + ".\n";
    }
}
