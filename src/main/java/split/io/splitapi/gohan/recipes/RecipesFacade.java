package split.io.splitapi.gohan.recipes;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.RecipeDetail;
import split.io.splitapi.gohan.recipes.models.inputs.CreateRecipeRequest;
import split.io.splitapi.gohan.recipes.models.inputs.PatchRecipeIngredientRequest;
import split.io.splitapi.gohan.recipes.models.inputs.PatchRecipeRequest;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeDetailResponse;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeResponse;
import split.io.splitapi.gohan.recipes.models.outputs.RecipesListResponse;
import split.io.splitapi.uuid.UuidGenerator;

import java.util.List;

@RequiredArgsConstructor
public class RecipesFacade {

    private final RecipesService recipesService;
    private final RecipesMapper recipesMapper;
    private final UuidGenerator uuidGenerator;

    public RecipesListResponse fetchAllByDevice(String deviceId) {
        List<Recipe> recipes = recipesService.fetchAllByDevice(deviceId);
        return recipesMapper.toRecipesListResponse(recipes);
    }

    public RecipeDetailResponse fetchById(String id) {
        RecipeDetail recipeDetail = recipesService.fetchById(id);
        return recipesMapper.toRecipeDetailResponse(recipeDetail);
    }

    public RecipeResponse create(CreateRecipeRequest request, String deviceId) {
        String id = uuidGenerator.generate();
        Recipe recipe = recipesMapper.toRecipe(request, id, deviceId);
        recipesService.create(recipe);
        return recipesMapper.toRecipeResponse(recipe);
    }

    public RecipeDetailResponse update(String id, PatchRecipeRequest request) {
        RecipeDetail updated = recipesService.update(id, request.name(), request.inMealsList(), request.done());
        return recipesMapper.toRecipeDetailResponse(updated);
    }

    public RecipeDetailResponse createRecipeIngredient(String recipeId, String ingredientId) {
        String recipeIngredientId = uuidGenerator.generate();
        RecipeDetail updated = recipesService.createRecipeIngredient(recipeId, ingredientId, recipeIngredientId);
        return recipesMapper.toRecipeDetailResponse(updated);
    }

    public RecipeDetailResponse updateRecipeIngredient(String recipeId, String ingredientId, PatchRecipeIngredientRequest request) {
        RecipeDetail updated = recipesService.updateRecipeIngredient(recipeId, ingredientId, request.bought());
        return recipesMapper.toRecipeDetailResponse(updated);
    }

    public void deleteRecipeIngredient(String recipeId, String ingredientId) {
        recipesService.deleteRecipeIngredient(recipeId, ingredientId);
    }

    public void delete(String id) {
        recipesService.delete(id);
    }
}
