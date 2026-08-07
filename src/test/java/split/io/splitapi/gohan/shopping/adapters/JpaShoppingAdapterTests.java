package split.io.splitapi.gohan.shopping.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.ingredients.dao.FakeIngredientsRepository;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;
import split.io.splitapi.gohan.recipes.dao.FakeRecipesRepository;
import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;
import split.io.splitapi.gohan.recipes.models.entities.RecipeIngredientEntity;
import split.io.splitapi.gohan.shopping.models.ShoppingItem;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaShoppingAdapterTests {

    private JpaShoppingAdapter adapter;
    private FakeIngredientsRepository fakeIngredientsRepository;
    private FakeRecipesRepository fakeRecipesRepository;

    @BeforeEach
    void setUp() {
        fakeIngredientsRepository = new FakeIngredientsRepository();
        fakeRecipesRepository = new FakeRecipesRepository();
        adapter = new JpaShoppingAdapter(fakeIngredientsRepository, fakeRecipesRepository);
    }

    @Test
    void shouldFetchIngredientsInShoppingList() {
        // Given
        String deviceId = "fake-device-id";
        fakeIngredientsRepository.shoppingListIngredientsToReturn = List.of(
                new IngredientEntity("ingredient-1", deviceId, "Tomate", true, false),
                new IngredientEntity("ingredient-2", deviceId, "Riz", true, true)
        );
        List<ShoppingItem> expectedItems = List.of(
                new ShoppingItem("ingredient-1", null, null, "Tomate", false),
                new ShoppingItem("ingredient-2", null, null, "Riz", true)
        );

        // When
        List<ShoppingItem> items = adapter.fetchIngredientsInShoppingList(deviceId);

        // Then
        assertEquals(expectedItems, items);
        assertEquals(deviceId, fakeIngredientsRepository.findByDeviceIdAndInShoppingListTrueParam);
    }

    @Test
    void shouldFetchMealIngredients() {
        // Given
        String deviceId = "fake-device-id";
        RecipeEntity recipeEntity = new RecipeEntity("recipe-1", deviceId, "Curry", true, false);
        RecipeIngredientEntity recipeIngredient1 = new RecipeIngredientEntity("recipe-ingredient-1", "recipe-1", "ingredient-1", true);
        recipeIngredient1.setIngredient(new IngredientEntity("ingredient-1", deviceId, "Tomate", false, false));
        RecipeIngredientEntity recipeIngredient2 = new RecipeIngredientEntity("recipe-ingredient-2", "recipe-1", "ingredient-2", false);
        recipeIngredient2.setIngredient(new IngredientEntity("ingredient-2", deviceId, "Poulet", false, false));
        recipeEntity.getRecipeIngredients().add(recipeIngredient1);
        recipeEntity.getRecipeIngredients().add(recipeIngredient2);
        fakeRecipesRepository.mealRecipesToReturn = List.of(recipeEntity);

        List<ShoppingItem> expectedItems = List.of(
                new ShoppingItem("ingredient-1", "recipe-1", "Curry", "Tomate", true),
                new ShoppingItem("ingredient-2", "recipe-1", "Curry", "Poulet", false)
        );

        // When
        List<ShoppingItem> items = adapter.fetchMealIngredients(deviceId);

        // Then
        assertEquals(expectedItems, items);
        assertEquals(deviceId, fakeRecipesRepository.findByDeviceIdAndInMealsListTrueParam);
    }
}
