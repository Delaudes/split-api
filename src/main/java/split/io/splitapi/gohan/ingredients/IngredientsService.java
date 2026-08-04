package split.io.splitapi.gohan.ingredients;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.gohan.ingredients.models.Ingredient;

import java.util.List;

@RequiredArgsConstructor
public class IngredientsService {

    private final IngredientsPort ingredientsPort;

    public List<Ingredient> fetchAllByDevice(String deviceId) {
        return ingredientsPort.fetchAllByDevice(deviceId);
    }

    public void createIngredient(Ingredient ingredient) {
        ingredientsPort.save(ingredient);
    }

    public Ingredient updateIngredient(String id, String name, Boolean inShoppingList, Boolean bought) {
        Ingredient current = ingredientsPort.fetchById(id);
        Ingredient updated = current.applyPatch(name, inShoppingList, bought);
        ingredientsPort.save(updated);
        return updated;
    }

    public void deleteIngredient(String id) {
        Ingredient ingredient = ingredientsPort.fetchById(id);
        if (ingredient.inShoppingList() || ingredientsPort.isUsedInRecipe(id)) {
            throw new IngredientInUseException("Ingredient is in the shopping list or used in a recipe: " + id);
        }
        ingredientsPort.delete(id);
    }
}
