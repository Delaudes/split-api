package split.io.splitapi.gohan.ingredients;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.ingredients.adapters.FakeIngredientsAdapter;
import split.io.splitapi.gohan.ingredients.models.Ingredient;
import split.io.splitapi.gohan.ingredients.models.inputs.CreateIngredientRequest;
import split.io.splitapi.gohan.ingredients.models.inputs.PatchIngredientRequest;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientResponse;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientsListResponse;
import split.io.splitapi.uuid.FakeUuidGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IngredientsFacadeTests {

    private IngredientsFacade ingredientsFacade;
    private FakeIngredientsAdapter fakeIngredientsAdapter;
    private FakeUuidGenerator fakeUuidGenerator;

    @BeforeEach
    void setUp() {
        fakeIngredientsAdapter = new FakeIngredientsAdapter();
        fakeUuidGenerator = new FakeUuidGenerator();
        IngredientsService ingredientsService = new IngredientsService(fakeIngredientsAdapter);
        IngredientsMapper ingredientsMapper = new IngredientsMapper();
        ingredientsFacade = new IngredientsFacade(ingredientsService, ingredientsMapper, fakeUuidGenerator);
    }

    @Test
    void shouldFetchAllIngredientsByDevice() {
        // Given
        String deviceId = "fake-device-id";
        fakeIngredientsAdapter.ingredients = List.of(
                new Ingredient("ingredient-1", deviceId, "Tomate", true, false),
                new Ingredient("ingredient-2", deviceId, "Riz", false, true)
        );
        IngredientsListResponse expectedResponse = new IngredientsListResponse(List.of(
                new IngredientResponse("ingredient-1", "Tomate", true, false),
                new IngredientResponse("ingredient-2", "Riz", false, true)
        ));

        // When
        IngredientsListResponse response = ingredientsFacade.fetchAllByDevice(deviceId);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(deviceId, fakeIngredientsAdapter.deviceId);
    }

    @Test
    void shouldFetchEmptyListWhenNoIngredientsForDevice() {
        // Given
        String deviceId = "fake-device-id-without-ingredients";

        // When
        IngredientsListResponse response = ingredientsFacade.fetchAllByDevice(deviceId);

        // Then
        assertEquals(new IngredientsListResponse(List.of()), response);
        assertEquals(deviceId, fakeIngredientsAdapter.deviceId);
    }

    @Test
    void shouldCreateIngredientWithGeneratedId() {
        // Given
        String deviceId = "fake-device-id";
        String ingredientName = "Tomate";
        CreateIngredientRequest request = new CreateIngredientRequest(ingredientName);
        Ingredient expectedIngredient = new Ingredient(fakeUuidGenerator.uuid, deviceId, ingredientName, false, false);
        IngredientResponse expectedResponse = new IngredientResponse(fakeUuidGenerator.uuid, ingredientName, false, false);

        // When
        IngredientResponse response = ingredientsFacade.create(request, deviceId);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(expectedIngredient, fakeIngredientsAdapter.savedIngredient);
    }

    @Test
    void shouldNotChangeAnythingWhenNoFieldsProvided() {
        // Given
        String ingredientId = "fake-ingredient-id";
        fakeIngredientsAdapter.ingredientToReturn = new Ingredient(ingredientId, "fake-device-id", "Tomate", true, false);
        PatchIngredientRequest request = new PatchIngredientRequest(null, null, null);
        IngredientResponse expectedResponse = new IngredientResponse(ingredientId, "Tomate", true, false);

        // When
        IngredientResponse response = ingredientsFacade.update(ingredientId, request);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(fakeIngredientsAdapter.ingredientToReturn, fakeIngredientsAdapter.savedIngredient);
    }

    @Test
    void shouldUpdateEveryField() {
        // Given
        String ingredientId = "fake-ingredient-id";
        fakeIngredientsAdapter.ingredientToReturn = new Ingredient(ingredientId, "fake-device-id", "Tomate", true, false);
        PatchIngredientRequest request = new PatchIngredientRequest("Tomate cerise", false, true);
        IngredientResponse expectedResponse = new IngredientResponse(ingredientId, "Tomate cerise", false, true);

        // When
        IngredientResponse response = ingredientsFacade.update(ingredientId, request);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(ingredientId, fakeIngredientsAdapter.fetchByIdParam);
        assertEquals(expectedResponse, ingredientsMapperResponseOf(fakeIngredientsAdapter.savedIngredient));
    }

    @Test
    void shouldForceBoughtFalseWhenInShoppingListSetToTrue() {
        // Given
        String ingredientId = "fake-ingredient-id";
        fakeIngredientsAdapter.ingredientToReturn = new Ingredient(ingredientId, "fake-device-id", "Tomate", false, true);
        PatchIngredientRequest request = new PatchIngredientRequest(null, true, null);

        // When
        IngredientResponse response = ingredientsFacade.update(ingredientId, request);

        // Then
        assertFalse(response.bought());
        assertTrue(response.inShoppingList());
    }

    @Test
    void shouldForceBoughtFalseWhenInShoppingListSetToTrueEvenIfBoughtExplicitlyRequestedTrue() {
        // Given
        String ingredientId = "fake-ingredient-id";
        fakeIngredientsAdapter.ingredientToReturn = new Ingredient(ingredientId, "fake-device-id", "Tomate", false, false);
        PatchIngredientRequest request = new PatchIngredientRequest(null, true, true);

        // When
        IngredientResponse response = ingredientsFacade.update(ingredientId, request);

        // Then
        assertTrue(response.inShoppingList());
        assertFalse(response.bought());
    }

    @Test
    void shouldNotForceBoughtWhenInShoppingListSetToFalse() {
        // Given
        String ingredientId = "fake-ingredient-id";
        fakeIngredientsAdapter.ingredientToReturn = new Ingredient(ingredientId, "fake-device-id", "Tomate", true, false);
        PatchIngredientRequest request = new PatchIngredientRequest(null, false, true);

        // When
        IngredientResponse response = ingredientsFacade.update(ingredientId, request);

        // Then
        assertFalse(response.inShoppingList());
        assertTrue(response.bought());
    }

    @Test
    void shouldDeleteIngredientWhenNotInShoppingListAndNotUsedInRecipe() {
        // Given
        String ingredientId = "fake-ingredient-id";
        fakeIngredientsAdapter.ingredientToReturn = new Ingredient(ingredientId, "fake-device-id", "Tomate", false, false);
        fakeIngredientsAdapter.isUsedInRecipeResult = false;

        // When
        ingredientsFacade.delete(ingredientId);

        // Then
        assertEquals(ingredientId, fakeIngredientsAdapter.deletedIngredientId);
    }

    @Test
    void shouldThrowConflictWhenDeletingIngredientInShoppingList() {
        // Given
        String ingredientId = "fake-ingredient-id";
        fakeIngredientsAdapter.ingredientToReturn = new Ingredient(ingredientId, "fake-device-id", "Tomate", true, false);

        // Then
        assertThrows(IngredientInUseException.class, () -> ingredientsFacade.delete(ingredientId));
        assertNull(fakeIngredientsAdapter.deletedIngredientId);
    }

    @Test
    void shouldThrowConflictWhenDeletingIngredientUsedInRecipe() {
        // Given
        String ingredientId = "fake-ingredient-id";
        fakeIngredientsAdapter.ingredientToReturn = new Ingredient(ingredientId, "fake-device-id", "Tomate", false, false);
        fakeIngredientsAdapter.isUsedInRecipeResult = true;

        // Then
        assertThrows(IngredientInUseException.class, () -> ingredientsFacade.delete(ingredientId));
        assertNull(fakeIngredientsAdapter.deletedIngredientId);
    }

    private static IngredientResponse ingredientsMapperResponseOf(Ingredient ingredient) {
        return new IngredientResponse(ingredient.id(), ingredient.name(), ingredient.inShoppingList(), ingredient.bought());
    }
}
