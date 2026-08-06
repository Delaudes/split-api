package split.io.splitapi.gohan.recipes.dao;

import split.io.splitapi.gohan.recipes.models.entities.RecipeIngredientEntity;

public class FakeRecipeIngredientsRepository implements RecipeIngredientsRepository {

    public boolean existsByIngredientIdResult = false;
    public String existsByIngredientIdParam;
    public String resetBoughtByRecipeIdParam;
    public RecipeIngredientEntity savedRecipeIngredient;
    public String updateBoughtRecipeIdParam;
    public String updateBoughtIngredientIdParam;
    public Boolean updateBoughtParam;
    public String deleteRecipeIdParam;
    public String deleteIngredientIdParam;

    @Override
    public boolean existsByIngredientId(String ingredientId) {
        this.existsByIngredientIdParam = ingredientId;
        return existsByIngredientIdResult;
    }

    @Override
    public void resetBoughtByRecipeId(String recipeId) {
        this.resetBoughtByRecipeIdParam = recipeId;
    }

    @Override
    public void save(RecipeIngredientEntity recipeIngredientEntity) {
        this.savedRecipeIngredient = recipeIngredientEntity;
    }

    @Override
    public void updateBought(String recipeId, String ingredientId, boolean bought) {
        this.updateBoughtRecipeIdParam = recipeId;
        this.updateBoughtIngredientIdParam = ingredientId;
        this.updateBoughtParam = bought;
    }

    @Override
    public void deleteByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        this.deleteRecipeIdParam = recipeId;
        this.deleteIngredientIdParam = ingredientId;
    }
}
