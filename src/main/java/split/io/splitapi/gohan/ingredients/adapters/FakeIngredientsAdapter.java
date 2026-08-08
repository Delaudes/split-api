package split.io.splitapi.gohan.ingredients.adapters;

import split.io.splitapi.gohan.ingredients.IngredientsPort;
import split.io.splitapi.gohan.ingredients.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class FakeIngredientsAdapter implements IngredientsPort {

    public List<Ingredient> ingredients = new ArrayList<>();
    public String deviceId;
    public Ingredient savedIngredient;
    public Ingredient ingredientToReturn;
    public String fetchByIdParam;
    public boolean isUsedInRecipeResult = false;
    public String isUsedInRecipeParam;
    public String deletedIngredientId;

    @Override
    public List<Ingredient> fetchAllByDevice(String deviceId) {
        this.deviceId = deviceId;
        return ingredients;
    }

    @Override
    public void save(Ingredient ingredient) {
        this.savedIngredient = ingredient;
    }

    @Override
    public Ingredient fetchById(String id) {
        this.fetchByIdParam = id;
        return ingredientToReturn;
    }

    @Override
    public boolean isUsedInRecipe(String id) {
        this.isUsedInRecipeParam = id;
        return isUsedInRecipeResult;
    }

    @Override
    public void delete(String id) {
        this.deletedIngredientId = id;
    }
}
