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
    public String saveRecipeIngredientRecipeIdParam;
    public String saveRecipeIngredientIngredientIdParam;
    public String saveRecipeIngredientRecipeIngredientIdParam;
    public String deleteRecipeIngredientRecipeIdParam;
    public String deleteRecipeIngredientIngredientIdParam;
    public String deletedRecipeId;

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
    public void saveRecipeIngredient(String recipeId, String ingredientId, String recipeIngredientId) {
        this.saveRecipeIngredientRecipeIdParam = recipeId;
        this.saveRecipeIngredientIngredientIdParam = ingredientId;
        this.saveRecipeIngredientRecipeIngredientIdParam = recipeIngredientId;
    }

    @Override
    public void deleteRecipeIngredient(String recipeId, String ingredientId) {
        this.deleteRecipeIngredientRecipeIdParam = recipeId;
        this.deleteRecipeIngredientIngredientIdParam = ingredientId;
    }

    @Override
    public void delete(String id) {
        this.deletedRecipeId = id;
    }
}
