package split.io.splitapi.gohan.ingredients;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.ingredients.adapters.FakeIngredientsAdapter;
import split.io.splitapi.gohan.ingredients.models.Ingredient;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientResponse;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientsListResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IngredientsFacadeTests {

    private IngredientsFacade ingredientsFacade;
    private FakeIngredientsAdapter fakeIngredientsAdapter;

    @BeforeEach
    void setUp() {
        fakeIngredientsAdapter = new FakeIngredientsAdapter();
        IngredientsService ingredientsService = new IngredientsService(fakeIngredientsAdapter);
        IngredientsMapper ingredientsMapper = new IngredientsMapper();
        ingredientsFacade = new IngredientsFacade(ingredientsService, ingredientsMapper);
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
}
