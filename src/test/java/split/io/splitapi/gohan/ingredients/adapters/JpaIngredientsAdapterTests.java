package split.io.splitapi.gohan.ingredients.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.ingredients.dao.FakeIngredientsRepository;
import split.io.splitapi.gohan.ingredients.models.Ingredient;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JpaIngredientsAdapterTests {

    private JpaIngredientsAdapter adapter;
    private FakeIngredientsRepository fakeIngredientsRepository;

    @BeforeEach
    void setUp() {
        fakeIngredientsRepository = new FakeIngredientsRepository();
        adapter = new JpaIngredientsAdapter(fakeIngredientsRepository);
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
    void shouldCreateIngredient() {
        // Given
        String deviceId = "fake-device-id";
        Ingredient ingredient = new Ingredient("ingredient-1", deviceId, "Tomate", false, false);

        // When
        adapter.create(ingredient);

        // Then
        assertEquals(ingredient.id(), fakeIngredientsRepository.savedIngredient.getId());
        assertEquals(ingredient.deviceId(), fakeIngredientsRepository.savedIngredient.getDeviceId());
        assertEquals(ingredient.name(), fakeIngredientsRepository.savedIngredient.getName());
        assertEquals(ingredient.inShoppingList(), fakeIngredientsRepository.savedIngredient.isInShoppingList());
        assertEquals(ingredient.bought(), fakeIngredientsRepository.savedIngredient.isBought());
    }
}
