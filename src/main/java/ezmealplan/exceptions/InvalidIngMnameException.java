package ezmealplan.exceptions;

public class InvalidIngMnameException extends EZMealPlanException {
    @Override
    public String getMessage() {
        return """
                "/ing" keyword must appear after "/mname" keyword in the "create" command.
                """;
    }
}
