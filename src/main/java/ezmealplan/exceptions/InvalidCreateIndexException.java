package ezmealplan.exceptions;

public class InvalidCreateIndexException extends EZMealPlanException {
    @Override
    public String getMessage() {
        return """
                "create" keyword must be at the front of the "create" command.
                """;
    }
}
