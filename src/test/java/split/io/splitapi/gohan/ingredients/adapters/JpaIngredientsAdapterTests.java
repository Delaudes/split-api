package split.io.splitapi.gohan.ingredients.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.ingredients.dao.FakeIngredientsRepository;
import split.io.splitapi.gohan.ingredients.models.Ingredient;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;
import split.io.splitapi.gohan.recipes.dao.FakeRecipeIngredientsRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JpaIngredientsAdapterTests {

    private JpaIngredientsAdapter adapter;
    private FakeIngredientsRepository fakeIngredientsRepository;
    private FakeRecipeIngredientsRepository fakeRecipeIngredientsRepository;

    @BeforeEach
    void setUp() {
        fakeIngredientsRepository = new FakeIngredientsRepository();
        fakeRecipeIngredientsRepository = new FakeRecipeIngredientsRepository();
        adapter = new JpaIngredientsAdapter(fakeIngredientsRepository, fakeRecipeIngredientsRepository);
    }

    @Test
    void shouldFetchAllIngredientsByDevice() {
        // Given
        String deviceId = "fake-device-id";
        fakeIngredientsRepository.ingredientsToReturn = List.of(
                new IngredientEntity("ingredient-1", deviceId, "Tomate", true, false),
                new IngredientEntity("ingredient-2", deviceId, "Riz", false, true)
        );
        List<Ingredient> expectedIngredients = List.of(
                new Ingredient("ingredient-1", deviceId, "Tomate", true, false),
                new Ingredient("ingredient-2", deviceId, "Riz", false, true)
        );

        // When
        List<Ingredient> ingredients = adapter.fetchAllByDevice(deviceId);

        // Then
        assertEquals(expectedIngredients, ingredients);
        assertEquals(deviceId, fakeIngredientsRepository.findByDeviceIdParam);
    }

    @Test
    void shouldSaveIngredient() {
        // Given
        String deviceId = "fake-device-id";
        Ingredient ingredient = new Ingredient("ingredient-1", deviceId, "Tomate", true, false);

        // When
        adapter.save(ingredient);

        // Then
        assertEquals(ingredient.id(), fakeIngredientsRepository.savedIngredient.getId());
        assertEquals(ingredient.deviceId(), fakeIngredientsRepository.savedIngredient.getDeviceId());
        assertEquals(ingredient.name(), fakeIngredientsRepository.savedIngredient.getName());
        assertEquals(ingredient.inShoppingList(), fakeIngredientsRepository.savedIngredient.isInShoppingList());
        assertEquals(ingredient.bought(), fakeIngredientsRepository.savedIngredient.isBought());
    }

    @Test
    void shouldFetchIngredientById() {
        // Given
        String ingredientId = "fake-ingredient-id";
        fakeIngredientsRepository.ingredientToReturn = new IngredientEntity(ingredientId, "fake-device-id", "Tomate", true, false);
        Ingredient expectedIngredient = new Ingredient(ingredientId, "fake-device-id", "Tomate", true, false);

        // When
        Ingredient ingredient = adapter.fetchById(ingredientId);

        // Then
        assertEquals(expectedIngredient, ingredient);
        assertEquals(ingredientId, fakeIngredientsRepository.findByIdParam);
    }

    @Test
    void shouldThrowWhenIngredientToFetchNotFound() {
        // Given
        String ingredientId = "unknown-ingredient-id";

        // Then
        assertThrows(RuntimeException.class, () -> adapter.fetchById(ingredientId));
    }

    @Test
    void shouldReturnWhetherIngredientIsUsedInRecipe() {
        // Given
        String ingredientId = "fake-ingredient-id";
        fakeRecipeIngredientsRepository.existsByIngredientIdResult = true;

        // When
        boolean isUsed = adapter.isUsedInRecipe(ingredientId);

        // Then
        assertTrue(isUsed);
        assertEquals(ingredientId, fakeRecipeIngredientsRepository.existsByIngredientIdParam);
    }

    @Test
    void shouldDeleteIngredient() {
        // Given
        String ingredientId = "fake-ingredient-id";

        // When
        adapter.delete(ingredientId);

        // Then
        assertEquals(ingredientId, fakeIngredientsRepository.deletedIngredientId);
    }
}
