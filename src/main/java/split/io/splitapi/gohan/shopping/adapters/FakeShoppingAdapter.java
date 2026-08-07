package split.io.splitapi.gohan.shopping.adapters;

import split.io.splitapi.gohan.shopping.ShoppingPort;
import split.io.splitapi.gohan.shopping.models.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

public class FakeShoppingAdapter implements ShoppingPort {

    public List<ShoppingItem> shoppingListIngredients = new ArrayList<>();
    public List<ShoppingItem> mealIngredients = new ArrayList<>();
    public String fetchIngredientsInShoppingListParam;
    public String fetchMealIngredientsParam;

    @Override
    public List<ShoppingItem> fetchIngredientsInShoppingList(String deviceId) {
        this.fetchIngredientsInShoppingListParam = deviceId;
        return shoppingListIngredients;
    }

    @Override
    public List<ShoppingItem> fetchMealIngredients(String deviceId) {
        this.fetchMealIngredientsParam = deviceId;
        return mealIngredients;
    }
}
