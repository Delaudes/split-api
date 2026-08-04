package split.io.splitapi.gohan.recipes.adapters;

import split.io.splitapi.gohan.recipes.RecipesPort;
import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.RecipeDetail;

import java.util.ArrayList;
import java.util.List;

public class FakeRecipesAdapter implements RecipesPort {

    public List<Recipe> recipes = new ArrayList<>();
    public String deviceId;
    public RecipeDetail recipeDetailToReturn;
    public String fetchByIdParam;

    @Override
    public List<Recipe> fetchAllByDevice(String deviceId) {
        this.deviceId = deviceId;
        return recipes;
    }

    @Override
    public RecipeDetail fetchById(String id) {
        this.fetchByIdParam = id;
        return recipeDetailToReturn;
    }
}
