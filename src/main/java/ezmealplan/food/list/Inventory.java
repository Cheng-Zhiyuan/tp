package ezmealplan.food.list;

import ezmealplan.exceptions.InventoryIngredientNotFound;
import ezmealplan.exceptions.InventoryMultipleIngredientsException;
import ezmealplan.food.Ingredient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Inventory {
    private final HashMap<Ingredient, Integer> ingredients;
    private final ArrayList<Ingredient> uniqueSortedIngredients;

    public Inventory() {
        ingredients = new HashMap<>();
        uniqueSortedIngredients = new ArrayList<>();
    }

    /**
     * Adds an Ingredient to the Inventory HashMap.
     * If an equal Ingredient already exists in the Inventory, then its quantity in the Inventory is increased.
     */
    public void addIngredient(Ingredient ingredient) {
        if (isIngredientInInventory(ingredient)) {
            Ingredient ingredientInInventory = getIngredientInInventory(ingredient);
            ingredients.put(ingredientInInventory, ingredients.get(ingredientInInventory) + 1);
        } else {
            ingredients.put(ingredient, 1);
            uniqueSortedIngredients.add(ingredient);
            uniqueSortedIngredients.sort(Comparator.comparing(Ingredient::getName,String.CASE_INSENSITIVE_ORDER).
                    thenComparing(Ingredient::getPrice));
        }
    }

    /**
     * Adds an Ingredient to the Inventory HashMap with a specified quantity.
     */
    public void addIngredient(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; i++) {
            addIngredient(ingredient);
        }
    }

    private boolean isIngredientInInventory(Ingredient ingredient) {
        return uniqueSortedIngredients.contains(ingredient);
    }

    private Ingredient getIngredientInInventory(Ingredient ingredient) {
        int index = uniqueSortedIngredients.indexOf(ingredient);
        return uniqueSortedIngredients.get(index);
    }

    /**
     * Removes an Ingredient from the Inventory HashMap. If the quantity of the Ingredient is more than 1, then its
     * quantity is reduced by 1.
     *
     * @param ingredientNameToBeRemoved The name of the Ingredient to be removed, as a String.
     * @return The Ingredient removed.
     * @throws InventoryMultipleIngredientsException If multiple unequal Ingredients matching the specified name
     *     is found.
     * @throws InventoryIngredientNotFound If an Ingredient with a matching name is not found in the Inventory.
     */
    public Ingredient removeIngredient(String ingredientNameToBeRemoved) throws InventoryMultipleIngredientsException,
            InventoryIngredientNotFound {
        ArrayList<Ingredient> ingredientsToRemove = findIngredientsFromString(ingredientNameToBeRemoved);
        if (hasMultipleIngredientsWithSameName(ingredientsToRemove)) {
            throw new InventoryMultipleIngredientsException(ingredientsToRemove);
        } else if (hasOnlyOneIngredient(ingredientsToRemove)) {
            Ingredient ingredientToBeRemoved = ingredientsToRemove.get(0);
            int ingredientQuantity = ingredients.get(ingredientToBeRemoved);
            if (ingredientQuantity > 1) {
                ingredients.put(ingredientToBeRemoved, ingredientQuantity - 1);
            } else {
                ingredients.remove(ingredientToBeRemoved);
                uniqueSortedIngredients.remove(ingredientToBeRemoved);
            }
            return ingredientToBeRemoved;
        } else {
            throw new InventoryIngredientNotFound(ingredientNameToBeRemoved);
        }
    }

    /**
     * Removes an Ingredient from the Inventory HashMap. If the quantity of the Ingredient is more than 1, then its
     * quantity is reduced by 1.
     *
     * @param ingredient An Ingredient object to be removed.
     * @throws InventoryIngredientNotFound If the Ingredient is not found in the Inventory.
     */
    public void removeIngredient(Ingredient ingredient) throws InventoryIngredientNotFound {
        if (uniqueSortedIngredients.contains(ingredient)) {
            int indexOfIngredient = uniqueSortedIngredients.indexOf(ingredient);
            Ingredient ingredientToBeRemoved = uniqueSortedIngredients.get(indexOfIngredient);
            int ingredientQuantity = ingredients.get(ingredientToBeRemoved);
            if (ingredientQuantity > 1) {
                ingredients.put(ingredientToBeRemoved, ingredientQuantity - 1);
            } else {
                ingredients.remove(ingredientToBeRemoved);
                uniqueSortedIngredients.remove(ingredientToBeRemoved);
            }
        } else {
            throw new InventoryIngredientNotFound(ingredient.toString());
        }
    }

    private ArrayList<Ingredient> findIngredientsFromString(String ingredientString) {
        ArrayList<Ingredient> ingredientsFound = new ArrayList<>();
        for (Ingredient uniqueIngredient : uniqueSortedIngredients) {
            if (uniqueIngredient.getName().equalsIgnoreCase(ingredientString)) {
                ingredientsFound.add(uniqueIngredient);
            }
        }
        return ingredientsFound;
    }

    private boolean hasMultipleIngredientsWithSameName(ArrayList<Ingredient> ingredients) {
        return ingredients.size() > 1;
    }

    private boolean hasOnlyOneIngredient(ArrayList<Ingredient> ingredients) {
        return ingredients.size() == 1;
    }

    /**
     * Converts the Inventory and all its Ingredients to a String.
     */
    public String toString() {
        int count = 0;
        StringBuilder outputString = new StringBuilder();
        for (Ingredient ingredient : uniqueSortedIngredients) {
            count++;
            outputString.append("    ");
            outputString.append(count);
            outputString.append(". ");
            outputString.append(ingredient);
            outputString.append(": ");
            outputString.append(ingredients.get(ingredient));
            outputString.append(System.lineSeparator());
        }
        return outputString.toString();
    }

    /**
     * Serialises the Inventory into an ArrayList of Strings for data storage.
     * The format used for each entry is "name | price | quantity".
     */
    public ArrayList<String> toDataArray() {
        ArrayList<String> outputDataArray = new ArrayList<>();
        for (Ingredient ingredient : uniqueSortedIngredients) {
            outputDataArray.add(ingredient.toDataString() + " | " + getIngredientAmount(ingredient));
        }
        return outputDataArray;
    }

    /**
     * Returns a List of unique Ingredient objects in the Inventory.
     */
    public List<Ingredient> getUniqueIngredients() {
        return uniqueSortedIngredients;
    }

    /**
     * Tests whether an Ingredient with the specified ingredientName exists in the Inventory.
     */
    public boolean hasIngredient(Ingredient ingredient) {
        return uniqueSortedIngredients.contains(ingredient);
    }

    private int getIngredientAmount(Ingredient ingredient) {
        return ingredients.getOrDefault(ingredient, 0);
    }
}
