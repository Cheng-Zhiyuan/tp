package ezmealplan.exceptions;

public class InvalidFilterMethodException extends EZMealPlanException {
    @Override
    public String getMessage() {
        return """
                Only 1 of the following filter keywords is needed for the 'filter' and 'select' command:
                /ing, /mcost or /mname.
                
                The filter keyword is compulsory for the 'filter' command.
                
                For the 'select' command, if you intend to select a meal from the filtered meal list,
                then the filter keyword is mandatory.
                Otherwise, it is optional.
                """;

    }
}
