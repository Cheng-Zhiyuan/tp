package seedu.parser;

import seedu.command.ByeCommand;
import seedu.command.Command;
import seedu.command.CreateCommand;
import seedu.command.UnknownCommand;

public class Parser {
    public static Command parse(String userInput) {
        String bye = "bye";
        String create = "create";
        userInput = userInput.toLowerCase().trim();
        if (userInput.contains(bye)) {
            return new ByeCommand();
        } else if (userInput.contains(create)) {
            return new CreateCommand(userInput);
        }
        return new UnknownCommand(userInput);
    }
}
