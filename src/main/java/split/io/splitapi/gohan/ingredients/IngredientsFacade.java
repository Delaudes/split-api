package split.io.splitapi.gohan.ingredients;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.gohan.ingredients.models.Ingredient;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientsListResponse;

import java.util.List;

@RequiredArgsConstructor
public class IngredientsFacade {

    private final IngredientsService ingredientsService;
    private final IngredientsMapper ingredientsMapper;

    public IngredientsListResponse fetchAllByDevice(String deviceId) {
        List<Ingredient> ingredients = ingredientsService.fetchAllByDevice(deviceId);
        return ingredientsMapper.toIngredientsListResponse(ingredients);
    }
}
