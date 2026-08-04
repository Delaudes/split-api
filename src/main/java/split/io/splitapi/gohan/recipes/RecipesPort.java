package split.io.splitapi.gohan.recipes;

import split.io.splitapi.gohan.recipes.models.Recipe;

import java.util.List;

public interface RecipesPort {
    List<Recipe> fetchAllByDevice(String deviceId);
}
