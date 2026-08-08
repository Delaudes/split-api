package split.io.splitapi.gohan.shopping;

import split.io.splitapi.gohan.shopping.models.ShoppingItem;
import split.io.splitapi.gohan.shopping.models.outputs.ShoppingItemResponse;
import split.io.splitapi.gohan.shopping.models.outputs.ShoppingListResponse;

import java.util.List;

public class ShoppingMapper {

    public ShoppingListResponse toShoppingListResponse(List<ShoppingItem> items) {
        return new ShoppingListResponse(items.stream().map(this::toShoppingItemResponse).toList());
    }

    public ShoppingItemResponse toShoppingItemResponse(ShoppingItem item) {
        return new ShoppingItemResponse(item.id(), item.recipeId(), item.recipeName(), item.name(), item.bought());
    }
}
