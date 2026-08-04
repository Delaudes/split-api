package split.io.splitapi.gohan.recipes;

import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.RecipeDetail;
import split.io.splitapi.gohan.recipes.models.RecipeIngredient;
import split.io.splitapi.gohan.recipes.models.inputs.CreateRecipeRequest;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeDetailResponse;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeIngredientResponse;
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

    public Recipe toRecipe(CreateRecipeRequest request, String id, String deviceId) {
        return new Recipe(id, deviceId, request.name(), false, false);
    }

    public RecipeDetailResponse toRecipeDetailResponse(RecipeDetail recipeDetail) {
        List<RecipeIngredientResponse> ingredients = recipeDetail.ingredients().stream()
                .map(this::toRecipeIngredientResponse)
                .toList();
        return new RecipeDetailResponse(recipeDetail.id(), recipeDetail.name(), recipeDetail.inMealsList(), recipeDetail.done(), ingredients);
    }

    private RecipeIngredientResponse toRecipeIngredientResponse(RecipeIngredient recipeIngredient) {
        return new RecipeIngredientResponse(recipeIngredient.id(), recipeIngredient.name(), recipeIngredient.bought());
    }
}
