package split.io.splitapi.gohan.shopping.models.outputs;

public record ShoppingItemResponse(String id, String recipeId, String recipeName, String name, boolean bought) {
}
