package split.io.splitapi.gohan.recipes.dao;

import split.io.splitapi.gohan.recipes.models.entities.RecipeIngredientEntity;

public interface RecipeIngredientsRepository {
    boolean existsByIngredientId(String ingredientId);
    void resetBoughtByRecipeId(String recipeId);
    void save(RecipeIngredientEntity recipeIngredientEntity);
    void updateBought(String recipeId, String ingredientId, boolean bought);
}
