package split.io.splitapi.gohan.recipes;

import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeResponse;
import split.io.splitapi.gohan.recipes.models.outputs.RecipesListResponse;

import java.util.List;

public class RecipesMapper {

    public RecipesListResponse toRecipesListResponse(List<Recipe> recipes) {
        List<RecipeResponse> responses = recipes.stream()
                .map(this::toRecipeResponse)
                .toList();
        return new RecipesListResponse(responses);
    }

    public RecipeResponse toRecipeResponse(Recipe recipe) {
        return new RecipeResponse(recipe.id(), recipe.name(), recipe.inMealsList(), recipe.done());
    }
}
