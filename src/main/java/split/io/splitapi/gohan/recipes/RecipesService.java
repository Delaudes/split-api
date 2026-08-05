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

    public void createRecipe(Recipe recipe) {
        recipesPort.save(recipe);
    }

    public RecipeDetail updateRecipe(String id, String name, Boolean inMealsList, Boolean done) {
        RecipeDetail current = recipesPort.fetchById(id);
        RecipeDetail updated = current.applyPatch(name, inMealsList, done);
        recipesPort.update(updated);
        if (Boolean.TRUE.equals(inMealsList) && current.hasIngredientsBought()) {
            recipesPort.resetIngredientsBought(id);
        }
        return updated;
    }
}
