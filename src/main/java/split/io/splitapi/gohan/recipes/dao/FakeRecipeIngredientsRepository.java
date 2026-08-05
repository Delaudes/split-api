package split.io.splitapi.gohan.recipes.dao;

public class FakeRecipeIngredientsRepository implements RecipeIngredientsRepository {

    public boolean existsByIngredientIdResult = false;
    public String existsByIngredientIdParam;
    public String resetBoughtByRecipeIdParam;

    @Override
    public boolean existsByIngredient_Id(String ingredientId) {
        this.existsByIngredientIdParam = ingredientId;
        return existsByIngredientIdResult;
    }

    @Override
    public void resetBoughtByRecipeId(String recipeId) {
        this.resetBoughtByRecipeIdParam = recipeId;
    }
}
