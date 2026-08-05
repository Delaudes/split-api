package split.io.splitapi.gohan.recipes;

import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.RecipeDetail;

import java.util.List;

public interface RecipesPort {
    List<Recipe> fetchAllByDevice(String deviceId);
    RecipeDetail fetchById(String id);
    void save(Recipe recipe);
    void update(RecipeDetail recipeDetail);
    void resetIngredientsBought(String recipeId);
    void attachIngredient(String recipeId, String ingredientId, String recipeIngredientId);
}
