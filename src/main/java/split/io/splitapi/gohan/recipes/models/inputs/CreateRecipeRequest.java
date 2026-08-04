package split.io.splitapi.gohan.recipes.models.inputs;

import jakarta.validation.constraints.NotBlank;

public record CreateRecipeRequest(
        @NotBlank String name
) {}
