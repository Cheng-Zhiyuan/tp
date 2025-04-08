package ezmealplan.command;

import ezmealplan.command.checkers.RecommendChecker;
import ezmealplan.exceptions.EZMealPlanException;
import ezmealplan.logic.MealManager;
import ezmealplan.ui.UserInterface;
import ezmealplan.food.Meal;
import ezmealplan.food.Ingredient;
import ezmealplan.food.list.Inventory;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RecommendCommand extends Command {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String RECOMMEND = "recommend";
    private static final String ING = "/ing";

    /**
     * Constructs a RecommendCommand.
     * Expected user input format: "recommend /ing Chicken"
     */
    public RecommendCommand(String userInput) {
        validUserInput = userInput.trim();
    }

    /**
     * Executes the Recommend command.
     *
     * @param mealManager the MealManager providing access to the lists.
     * @param ui          the UserInterface for printing messages.
     */
    @Override
    public void execute(MealManager mealManager, UserInterface ui) throws EZMealPlanException {
        // Validate input using RecommendChecker.
        RecommendChecker checker = new RecommendChecker(validUserInput);
        checker.check();
        if (!checker.isPassed()) {
            logger.severe("Invalid recommend command input detected.");
            return;
        }

        // Extract the ingredient keyword.
        // Expected input format: "recommend /ing Chicken"
        int afterRecommendIndex = validUserInput.toLowerCase().indexOf(RECOMMEND) + RECOMMEND.length();
        String args = validUserInput.substring(afterRecommendIndex).trim();
        int ingIndex = args.toLowerCase().indexOf(ING);
        int invalidIndex = -1;
        if (ingIndex == invalidIndex) {
            logger.severe("Ingredient marker '/ing' not found in input.");
            ui.printMessage("Missing ingredient marker '/ing'.");
            return;
        }
        int afterIngIndex = ingIndex + ING.length();
        String extractedKeyword = args.substring(afterIngIndex).trim();
        if (extractedKeyword.isEmpty()) {
            logger.severe("No ingredient specified after '/ing'.");
            ui.printMessage("No ingredient specified.");
            return;
        }

        // Proceed with recommendation logic.
        Inventory inventory = mealManager.getInventory();

        // First, filter the user meal list (wishlist) by the ingredient keyword.
        List<Meal> wishList = mealManager.getWishList().getList();
        List<Meal> candidateMeals = filterMealsByIngredient(wishList, extractedKeyword);

        // If no matching meals in the wishlist, try the recipes list.
        if (candidateMeals.isEmpty()) {
            List<Meal> recipesList = mealManager.getRecipesList().getList();
            candidateMeals = filterMealsByIngredient(recipesList, extractedKeyword);
        }

        if (candidateMeals.isEmpty()) {
            ui.printMessage("No meal found containing ingredient: " + extractedKeyword);
            return;
        }

        // Randomly select a meal from the candidate meals.
        Random random = new Random();
        Meal selectedMeal = candidateMeals.get(random.nextInt(candidateMeals.size()));

        // Build the output message.
        StringBuilder sb = new StringBuilder();
        sb.append("Recommended Meal: ").append(selectedMeal.getName())
                .append(" (").append(selectedMeal).append(")")
                .append(System.lineSeparator());
        sb.append("Ingredients:").append(System.lineSeparator());

        List<Ingredient> mealIngredients = selectedMeal.getIngredientList();
        for (int i = 0; i < mealIngredients.size(); i++) {
            Ingredient ing = mealIngredients.get(i);
            sb.append("   ").append(i + 1).append(". ").append(ing.toString())
                    .append(System.lineSeparator());
        }

        // Determine which ingredients are missing from the inventory.
        List<String> missingIngredients = mealIngredients.stream()
                .filter(ing -> !inventory.hasIngredient(ing))
                .map(Ingredient::getName)
                .collect(Collectors.toList());
        if (missingIngredients.isEmpty()) {
            sb.append("You have all the necessary ingredients for this meal.");
        } else {
            sb.append("Missing Ingredients: ").append(String.join(", ", missingIngredients));
        }

        ui.printMessage(sb.toString());
    }

    /**
     * Filters a list of meals by checking if any ingredient name contains the given keyword.
     */
    private List<Meal> filterMealsByIngredient(List<Meal> meals, String keyword) {
        return meals.stream()
                .filter(meal -> meal.getIngredientList().stream()
                        .anyMatch(ing -> ing.getName().toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());
    }
}
