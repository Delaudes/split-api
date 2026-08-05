package split.io.splitapi.gohan.recipes.dao;

import split.io.splitapi.gohan.recipes.models.entities.RecipeIngredientEntity;

public class FakeRecipeIngredientsRepository implements RecipeIngredientsRepository {

    public boolean existsByIngredientIdResult = false;
    public String existsByIngredientIdParam;
    public String resetBoughtByRecipeIdParam;
    public RecipeIngredientEntity savedRecipeIngredient;

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
}
