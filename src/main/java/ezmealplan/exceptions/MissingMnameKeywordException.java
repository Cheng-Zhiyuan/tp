package ezmealplan.exceptions;

public class MissingMnameKeywordException extends EZMealPlanException {
    @Override
    public String getMessage() {
        return """
                "/mname" keyword cannot be missing from the "create" command.
                """;
    }
}
