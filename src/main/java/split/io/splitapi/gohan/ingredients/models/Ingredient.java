package split.io.splitapi.gohan.ingredients.models;

public record Ingredient(String id, String deviceId, String name, boolean inShoppingList, boolean bought) {
}
