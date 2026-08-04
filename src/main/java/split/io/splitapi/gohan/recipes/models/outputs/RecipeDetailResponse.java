package split.io.splitapi.gohan.recipes.models.outputs;

import java.util.List;

public record RecipeDetailResponse(String id, String name, boolean inMealsList, boolean done, List<RecipeIngredientResponse> ingredients) {
}
