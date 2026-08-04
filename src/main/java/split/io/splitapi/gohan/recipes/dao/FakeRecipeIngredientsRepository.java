package split.io.splitapi.gohan.recipes.dao;

public class FakeRecipeIngredientsRepository implements RecipeIngredientsRepository {

    public boolean existsByIngredientIdResult = false;
    public String existsByIngredientIdParam;

    @Override
    public boolean existsByIngredient_Id(String ingredientId) {
        this.existsByIngredientIdParam = ingredientId;
        return existsByIngredientIdResult;
    }
}
