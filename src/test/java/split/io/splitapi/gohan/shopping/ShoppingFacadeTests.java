package split.io.splitapi.gohan.shopping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.shopping.adapters.FakeShoppingAdapter;
import split.io.splitapi.gohan.shopping.models.ShoppingItem;
import split.io.splitapi.gohan.shopping.models.outputs.ShoppingItemResponse;
import split.io.splitapi.gohan.shopping.models.outputs.ShoppingListResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingFacadeTests {

    private ShoppingFacade shoppingFacade;
    private FakeShoppingAdapter fakeShoppingAdapter;

    @BeforeEach
    void setUp() {
        fakeShoppingAdapter = new FakeShoppingAdapter();
        ShoppingService shoppingService = new ShoppingService(fakeShoppingAdapter);
        ShoppingMapper shoppingMapper = new ShoppingMapper();
        shoppingFacade = new ShoppingFacade(shoppingService, shoppingMapper);
    }

    @Test
    void shouldGroupMealIngredientSharingIdWithShoppingListIngredientRightAfterIt() {
        // Given
        String deviceId = "fake-device-id";
        fakeShoppingAdapter.shoppingListIngredients = List.of(
                new ShoppingItem("ingredient-tomato", null, null, "Tomate", false),
                new ShoppingItem("ingredient-rice", null, null, "Riz", true)
        );
        fakeShoppingAdapter.mealIngredients = List.of(
                new ShoppingItem("ingredient-tomato", "recipe-1", "Curry", "Tomate", true)
        );
        ShoppingListResponse expectedResponse = new ShoppingListResponse(List.of(
                new ShoppingItemResponse("ingredient-tomato", null, null, "Tomate", false),
                new ShoppingItemResponse("ingredient-tomato", "recipe-1", "Curry", "Tomate", true),
                new ShoppingItemResponse("ingredient-rice", null, null, "Riz", true)
        ));

        // When
        ShoppingListResponse response = shoppingFacade.fetchAllByDevice(deviceId);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(deviceId, fakeShoppingAdapter.fetchIngredientsInShoppingListParam);
        assertEquals(deviceId, fakeShoppingAdapter.fetchMealIngredientsParam);
    }

    @Test
    void shouldIncludeMealIngredientWithNoMatchingShoppingListIngredient() {
        // Given
        String deviceId = "fake-device-id";
        fakeShoppingAdapter.mealIngredients = List.of(
                new ShoppingItem("ingredient-chicken", "recipe-1", "Curry", "Poulet", false)
        );
        ShoppingListResponse expectedResponse = new ShoppingListResponse(List.of(
                new ShoppingItemResponse("ingredient-chicken", "recipe-1", "Curry", "Poulet", false)
        ));

        // When
        ShoppingListResponse response = shoppingFacade.fetchAllByDevice(deviceId);

        // Then
        assertEquals(expectedResponse, response);
    }

    @Test
    void shouldFetchEmptyListWhenNothingToShop() {
        // Given
        String deviceId = "fake-device-id-without-shopping-items";

        // When
        ShoppingListResponse response = shoppingFacade.fetchAllByDevice(deviceId);

        // Then
        assertEquals(new ShoppingListResponse(List.of()), response);
    }
}
