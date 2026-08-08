package split.io.splitapi.gohan.shopping;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.gohan.shopping.models.ShoppingItem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ShoppingService {

    private final ShoppingPort shoppingPort;

    public List<ShoppingItem> fetchAllByDevice(String deviceId) {
        List<ShoppingItem> shoppingListItems = shoppingPort.fetchIngredientsInShoppingList(deviceId);
        List<ShoppingItem> mealIngredients = shoppingPort.fetchMealIngredients(deviceId);

        return Stream.concat(shoppingListItems.stream(), mealIngredients.stream())
                .collect(Collectors.groupingBy(ShoppingItem::id, LinkedHashMap::new, Collectors.toList()))
                .values().stream()
                .flatMap(List::stream)
                .toList();
    }
}
