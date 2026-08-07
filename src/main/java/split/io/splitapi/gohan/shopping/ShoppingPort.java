package split.io.splitapi.gohan.shopping;

import split.io.splitapi.gohan.shopping.models.ShoppingItem;

import java.util.List;

public interface ShoppingPort {
    List<ShoppingItem> fetchIngredientsInShoppingList(String deviceId);
    List<ShoppingItem> fetchMealIngredients(String deviceId);
}
