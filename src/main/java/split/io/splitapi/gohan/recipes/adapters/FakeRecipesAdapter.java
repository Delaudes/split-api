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
    public Recipe savedRecipe;
    public RecipeDetail updatedRecipeDetail;
    public String resetIngredientsBoughtParam;
    public String attachIngredientRecipeIdParam;
    public String attachIngredientIngredientIdParam;
    public String attachIngredientRecipeIngredientIdParam;

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

    @Override
    public void save(Recipe recipe) {
        this.savedRecipe = recipe;
    }

    @Override
    public void update(RecipeDetail recipeDetail) {
        this.updatedRecipeDetail = recipeDetail;
    }

    @Override
    public void resetIngredientsBought(String recipeId) {
        this.resetIngredientsBoughtParam = recipeId;
    }

    @Override
    public void attachIngredient(String recipeId, String ingredientId, String recipeIngredientId) {
        this.attachIngredientRecipeIdParam = recipeId;
        this.attachIngredientIngredientIdParam = ingredientId;
        this.attachIngredientRecipeIngredientIdParam = recipeIngredientId;
    }
}
