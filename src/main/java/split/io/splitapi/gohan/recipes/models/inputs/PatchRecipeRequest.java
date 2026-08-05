package split.io.splitapi.gohan.recipes.models.inputs;

public record PatchRecipeRequest(
        String name,
        Boolean inMealsList,
        Boolean done
) {}
