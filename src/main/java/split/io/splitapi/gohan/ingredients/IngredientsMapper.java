package split.io.splitapi.gohan.ingredients;

import split.io.splitapi.gohan.ingredients.models.Ingredient;
import split.io.splitapi.gohan.ingredients.models.inputs.CreateIngredientRequest;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientResponse;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientsListResponse;

import java.util.List;

public class IngredientsMapper {

    public IngredientsListResponse toIngredientsListResponse(List<Ingredient> ingredients) {
        List<IngredientResponse> responses = ingredients.stream()
                .map(this::toIngredientResponse)
                .toList();
        return new IngredientsListResponse(responses);
    }

    public Ingredient toIngredient(CreateIngredientRequest request, String id, String deviceId) {
        boolean inShoppingList = Boolean.TRUE.equals(request.inShoppingList());
        return new Ingredient(id, deviceId, request.name(), inShoppingList, false);
    }

    public IngredientResponse toIngredientResponse(Ingredient ingredient) {
        return new IngredientResponse(ingredient.id(), ingredient.name(), ingredient.inShoppingList(), ingredient.bought());
    }
}
