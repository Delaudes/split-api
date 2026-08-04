package split.io.splitapi.gohan.recipes;

import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.RecipeDetail;

import java.util.List;

public interface RecipesPort {
    List<Recipe> fetchAllByDevice(String deviceId);
    RecipeDetail fetchById(String id);
}
