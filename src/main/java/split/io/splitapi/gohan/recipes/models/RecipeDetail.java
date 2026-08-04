package split.io.splitapi.gohan.recipes.models;

import java.util.List;

public record RecipeDetail(String id, String name, boolean inMealsList, boolean done, List<RecipeIngredient> ingredients) {
}
