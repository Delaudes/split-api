package split.io.splitapi.gohan.ingredients;

import split.io.splitapi.gohan.ingredients.models.Ingredient;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientResponse;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientsListResponse;

import java.util.List;

public class IngredientsMapper {

    public IngredientsListResponse toIngredientsListResponse(List<Ingredient> ingredients) {
        List<IngredientResponse> responses = ingredients.stream()
                .map(i -> new IngredientResponse(i.id(), i.name(), i.inShoppingList(), i.bought()))
                .toList();
        return new IngredientsListResponse(responses);
    }
}
