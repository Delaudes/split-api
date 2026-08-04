package split.io.splitapi.gohan.ingredients.models.inputs;

public record PatchIngredientRequest(
        String name,
        Boolean inShoppingList,
        Boolean bought
) {}
