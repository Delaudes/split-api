package split.io.splitapi.gohan.ingredients;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.gohan.ingredients.models.Ingredient;
import split.io.splitapi.gohan.ingredients.models.inputs.CreateIngredientRequest;
import split.io.splitapi.gohan.ingredients.models.inputs.PatchIngredientRequest;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientResponse;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientsListResponse;
import split.io.splitapi.uuid.UuidGenerator;

import java.util.List;

@RequiredArgsConstructor
public class IngredientsFacade {

    private final IngredientsService ingredientsService;
    private final IngredientsMapper ingredientsMapper;
    private final UuidGenerator uuidGenerator;

    public IngredientsListResponse fetchAllByDevice(String deviceId) {
        List<Ingredient> ingredients = ingredientsService.fetchAllByDevice(deviceId);
        return ingredientsMapper.toIngredientsListResponse(ingredients);
    }

    public IngredientResponse create(CreateIngredientRequest request, String deviceId) {
        String id = uuidGenerator.generate();
        Ingredient ingredient = ingredientsMapper.toIngredient(request, id, deviceId);
        ingredientsService.createIngredient(ingredient);
        return ingredientsMapper.toIngredientResponse(ingredient);
    }

    public IngredientResponse update(String id, PatchIngredientRequest request) {
        Ingredient updated = ingredientsService.updateIngredient(id, request.name(), request.inShoppingList(), request.bought());
        return ingredientsMapper.toIngredientResponse(updated);
    }
}
