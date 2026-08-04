package split.io.splitapi.gohan.recipes;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.RecipeDetail;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeDetailResponse;
import split.io.splitapi.gohan.recipes.models.outputs.RecipesListResponse;

import java.util.List;

@RequiredArgsConstructor
public class RecipesFacade {

    private final RecipesService recipesService;
    private final RecipesMapper recipesMapper;

    public RecipesListResponse fetchAllByDevice(String deviceId) {
        List<Recipe> recipes = recipesService.fetchAllByDevice(deviceId);
        return recipesMapper.toRecipesListResponse(recipes);
    }

    public RecipeDetailResponse fetchById(String id) {
        RecipeDetail recipeDetail = recipesService.fetchById(id);
        return recipesMapper.toRecipeDetailResponse(recipeDetail);
    }
}
