package split.io.splitapi.gohan.recipes.models.inputs;

import jakarta.validation.constraints.NotNull;

public record PatchRecipeIngredientRequest(
        @NotNull Boolean bought
) {}
