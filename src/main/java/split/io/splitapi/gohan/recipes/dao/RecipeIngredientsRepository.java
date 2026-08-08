package split.io.splitapi.gohan.recipes.dao;

import split.io.splitapi.gohan.recipes.models.entities.RecipeIngredientEntity;

public interface RecipeIngredientsRepository {
    boolean existsByIngredientId(String ingredientId);
    void save(RecipeIngredientEntity recipeIngredientEntity);
    void deleteByRecipeIdAndIngredientId(String recipeId, String ingredientId);
}
