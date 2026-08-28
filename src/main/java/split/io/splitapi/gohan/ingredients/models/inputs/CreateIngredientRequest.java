package split.io.splitapi.gohan.ingredients.models.inputs;

import jakarta.validation.constraints.NotBlank;

public record CreateIngredientRequest(
        @NotBlank String name,
        Boolean inShoppingList
) {}
