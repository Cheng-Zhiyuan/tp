package ezmealplan.ui;

import ezmealplan.food.Ingredient;
import ezmealplan.food.Meal;
import ezmealplan.food.list.MealList;

import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;

    public UserInterface() {
        scanner = new Scanner(System.in);
    }

    public void printMessage(String s) {
        System.out.println(s);
    }

    /**
     * Reads the user's input from the command line.
     *
     * @return The user's input as a string.
     */
    public String readInput() {
        String userCmd = "";
        if (scanner.hasNextLine()) {
            userCmd = scanner.nextLine();
            userCmd = userCmd.trim();
        }
        return userCmd;
    }

    public void printGreetingMessage() {
        System.out.println("Hello! This is EzMealPlan");
        System.out.println("Let me help you in planning your meals.");
    }

    public void printGoodbye() {
        scanner.close();
        System.out.print("Bye. Hope to see you again soon!");
    }

    public void printUnknownCommand(String userInput) {
        System.out.println("Invalid command: " + userInput);
        System.out.println("me no understand what you talking.");
    }

    public void printMistypedCommand(String userInput, String actualCommand) {
        System.out.println("Invalid command: " + userInput);
        System.out.println("Did you mean: " + actualCommand + "?");
    }

    public void printErrorMessage(Exception exception) {
        System.out.println(exception.getMessage());
    }

    public void printAddMealMessage(Meal meal, MealList mealList) {
        String mealListName = mealList.getMealListName();
        String successAddMealMessage = "You have successfully added a meal: " + meal + " into " + mealListName + ".";
        System.out.println(successAddMealMessage);
        List<Meal> meals = mealList.getList();
        printMealList(meals, mealListName);
        String totalMealsMessage = "Currently, you have " + meals.size() +
                                   " meals in " + mealListName + ".";
        System.out.println(totalMealsMessage);
    }

    public void printIngredientList(Meal meal) {
        List<Ingredient> ingredientList = meal.getIngredientList();
        System.out.println("Here are the ingredients for " + meal + ":");
        int count = 0;
        for (Ingredient ingredient : ingredientList) {
            count++;
            System.out.println("    " + count + ". " + ingredient);
        }
        System.out.println();
    }

    public void printMealList(List<Meal> meals, String mealListName) {
        if (meals.isEmpty()) {
            System.out.println("No meals found in " + mealListName + ".\n");
            return;
        }

        System.out.println("Here are the meals in " + mealListName + ":");
        int count = 0;
        for (Meal meal : meals) {
            count++;
            System.out.println("    " + count + ". " + meal);
        }
        System.out.println();
    }

    public void printRemovedMessage(Meal meal, int size) {
        System.out.println(meal + " has been removed from your meal list!");
        System.out.printf("You have %d meals in your meal list.\n", size);
    }

    public void printDeletedMessage(Meal meal, int size) {
        System.out.println(meal + " has been removed from the recipes list!");
        System.out.printf("There are now %d meals in the recipes list.\n", size);
    }

    public void prompt() {
        System.out.println("How may I help you?");
    }

    public void printClearedList() {
        printMessage("All meals cleared from your wishlist!");
    }

    public void printByeCommandHelp() {
        System.out.println("Entering the bye command will gracefully exits the software");
        System.out.println("Sample input: bye");
        System.out.println("Sample output: Bye. Hope to see you again soon!");
    }

    public void printGeneralHelp() {
        System.out.println("you have not entered any command line options");
    }

    public void printWishlistCommandHelp() {
        System.out.println("Entering the wishlist command will list out all the meals you " +
                           "have selected from the recipes list.");
        System.out.println("Sample input: wishlist");
        System.out.println("Sample output:");
        System.out.println("               1. Chicken Rice");
        System.out.println("               2. Fish Ball Noodles");
    }

    public void printRecipesCommandHelp() {
        System.out.println("Entering the recipes command will list out all the meals from the recipes list.");
        System.out.println("Sample input: recipes");
        System.out.println("Sample output:");
        System.out.println("               1. Chicken Rice");
        System.out.println("               2. Fish Ball Noodles");
    }

    public void printCreateCommandHelp() {
        System.out.println("Entering the create command will create a new meal");
        System.out.println("Sample input: create /mname Hokkien Mee /ing yellow noodle (1), thick " +
                           "bee hoon (1), prawn (1.2), egg (0.5), pork lard (0.2), squid (1.5), lime (0.1)");
        System.out.println("Sample output:");
        System.out.println("               1. Chicken Rice ($3.00)");
        System.out.println("               2. Fish Ball Noodles ($3.00)");
        System.out.println("               3. Hokkien Mee ($5.50)");
    }

    public void printRemoveCommandHelp() {
        System.out.println("Entering the remove command will remove a meal in the wishlist");
        System.out.println("Sample input: remove 1 ");
        System.out.println("Sample output:");
        System.out.println("               OK.  Chicken Rice ($3.00) have been removed from the wishlist.");
    }

    public void printSelectCommandHelp() {
        System.out.println("Entering the select command will add the selected meal from the filtered or unfiltered " +
                           "recipes list into the wishlist");
        System.out.println("Sample input (filtered by ingredient(s)): select 1 /ing yellow noodle, fish");
        System.out.println("Sample input (filtered by meal cost): select 1 /mcost 2");
        System.out.println("Sample input (filtered by meal name(s)): select 1 /mname fish, ball");
        System.out.println("Sample input: select 1");
        System.out.println("Sample output based on the sample input 'select 1':");
        System.out.println("               OK.  Chicken Rice ($3.00) have been added to the wishlist.");
    }

    public void printFilterCommandHelp() {
        System.out.println("Entering the filter command will filter a meal in the recipes list");
        System.out.println("There are three filter option by cost, by ingredient or by meal name");
        System.out.println("Sample input: filter /mcost 5.50");
        System.out.println("Sample output:");
        System.out.println("               1. Chicken Rice ($5.50)");
        System.out.println("Sample input: filter /ing  Fish Ball");
        System.out.println("Sample output:");
        System.out.println("               1. Chicken Rice ($5.50)");
        System.out.println("Sample input: filter /mname Hokkien Mee");
        System.out.println("Sample output:");
        System.out.println("               1. Hokkien Mee ($5.50)");
    }

    public void printDeleteCommandHelp() {
        System.out.println("Entering the delete command will delete a meal in the recipes list");
        System.out.println("Sample input: delete 1 ");
        System.out.println("Sample output:");
        System.out.println("               OK.  Chicken Rice ($3.00) have been deleted from the recipes list.");
    }

    public void printViewCommandHelp() {
        System.out.println("Entering the view command will give a view of all the ingredients of the selected meal");
        System.out.println("Sample input: view 1 ");
        System.out.println("Sample output:");
        System.out.println("               1. yellow noodle");
        System.out.println("               2. bee hoon");
        System.out.println("               3. prawn");
        System.out.println("               4. egg");
        System.out.println("               5. pork lard");
        System.out.println("               6. squid");
        System.out.println("               7. lime");
    }

    public void printClearCommandHelp() {
        System.out.println("Entering the clear command will clear all the meals in the wishlist");
        System.out.println("Sample input: clear");
        System.out.println("Sample output:");
        System.out.println("               The wishlist has been cleared.");
    }

    public void printHelpCommandHelp() {
        System.out.println("Entering the help command followed by the command that requires help " +
                           "will give brief explanation of the command");
        System.out.println("Sample input: help bye");
        System.out.println("Sample output:");
        System.out.println("                Entering the bye command will gracefully exits the software");
        System.out.println("                    Sample input: bye");
        System.out.println("                    Sample output: Bye. Hope to see you again soon!");
    }

    public void printConsumed(String ingredientName) {
        System.out.println(ingredientName + " consumed");
    }

    public void printBought(Ingredient ingredient) {
        System.out.println(ingredient + " bought");
    }

    public void printInventory(String inventoryString) {
        if (inventoryString.isEmpty()) {
            System.out.println("No ingredients found in your inventory.\n");
            return;
        }

        System.out.println("Here are the ingredients that you own:");
        System.out.print(inventoryString);
    }

    public void printRecommendCommandHelp() {
        System.out.println("Entering the recommend command with an ingredient keyword will suggest a meal");
        System.out.println("based on that ingredient from your wishlist or the recipe list.");
        System.out.println("Sample input: recommend /ing chicken");
        System.out.println("Sample output:");
        System.out.println("                Recommended Meal: Kung Pao Chicken (Kung Pao Chicken ($3.60))");
        System.out.println("                Ingredients:");
        System.out.println("                   1. Chicken ($2.00)");
        System.out.println("                   2. Chilli ($0.50)");
        System.out.println("                   3. Peanuts ($0.70)");
        System.out.println("                   4. Sichuan Pepper ($0.40)");
        System.out.println("                Missing Ingredients: Chicken, Chilli, Peanuts, Sichuan Pepper");
    }

    public void printConsumeCommandHelp() {
        System.out.println("Entering the consume command will consume an ingredient from your inventory.");
        System.out.println("You must specify the ingredient name using the /ing prefix.");
        System.out.println("Sample input: consume /ing egg");
        System.out.println("Sample output:");
        System.out.println("                egg consumed");
    }

    public void printBuyCommandHelp() {
        System.out.println("Entering the buy command will add an ingredient to your inventory.");
        System.out.println("You must specify the ingredient names using the /ing prefix " +
                "followed by their prices ");
        System.out.println("Sample input: buy /ing egg (1.00)");
        System.out.println("Sample output:");
        System.out.println("                egg ($1.00) bought");
    }

    public void printInventoryCommandHelp() {
        System.out.println("Entering the inventory command will show all ingredients currently in your inventory.");
        System.out.println("Sample input: inventory");
        System.out.println("Sample output:");
        System.out.println("                Inventory List:");
        System.out.println("                   1. egg ($1.00)");
        System.out.println("                   2. chicken breast ($3.25)");
        System.out.println("                   3. rice ($1.00)");
    }
}
