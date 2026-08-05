package split.io.splitapi.gohan.recipes.dao;

public interface RecipeIngredientsRepository {
    boolean existsByIngredient_Id(String ingredientId);
    void resetBoughtByRecipeId(String recipeId);
}
