package split.io.splitapi.gohan.recipes;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.RecipeDetail;

import java.util.List;

@RequiredArgsConstructor
public class RecipesService {

    private final RecipesPort recipesPort;

    public List<Recipe> fetchAllByDevice(String deviceId) {
        return recipesPort.fetchAllByDevice(deviceId);
    }

    public RecipeDetail fetchById(String id) {
        return recipesPort.fetchById(id);
    }

    public void create(Recipe recipe) {
        recipesPort.save(recipe);
    }

    public RecipeDetail update(String id, String name, Boolean inMealsList, Boolean done) {
        RecipeDetail current = recipesPort.fetchById(id);
        RecipeDetail updated = current.applyPatch(name, inMealsList, done);
        recipesPort.update(updated);
        return updated;
    }

    public RecipeDetail createRecipeIngredient(String recipeId, String ingredientId, String recipeIngredientId) {
        recipesPort.saveRecipeIngredient(recipeId, ingredientId, recipeIngredientId);
        return recipesPort.fetchById(recipeId);
    }

    public RecipeDetail updateRecipeIngredient(String recipeId, String ingredientId, boolean bought) {
        RecipeDetail current = recipesPort.fetchById(recipeId);
        RecipeDetail updated = current.applyPatch(ingredientId, bought);
        recipesPort.update(updated);
        return updated;
    }

    public void deleteRecipeIngredient(String recipeId, String ingredientId) {
        recipesPort.deleteRecipeIngredient(recipeId, ingredientId);
    }

    public void delete(String id) {
        RecipeDetail recipe = recipesPort.fetchById(id);
        if (recipe.inMealsList()) {
            throw new RecipeInMealsListException("Recipe is in the meals list: " + id);
        }
        recipesPort.delete(id);
    }
}
