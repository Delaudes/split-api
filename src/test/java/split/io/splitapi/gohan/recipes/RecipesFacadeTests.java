package split.io.splitapi.gohan.recipes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.recipes.adapters.FakeRecipesAdapter;
import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeResponse;
import split.io.splitapi.gohan.recipes.models.outputs.RecipesListResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipesFacadeTests {

    private RecipesFacade recipesFacade;
    private FakeRecipesAdapter fakeRecipesAdapter;

    @BeforeEach
    void setUp() {
        fakeRecipesAdapter = new FakeRecipesAdapter();
        RecipesService recipesService = new RecipesService(fakeRecipesAdapter);
        RecipesMapper recipesMapper = new RecipesMapper();
        recipesFacade = new RecipesFacade(recipesService, recipesMapper);
    }

    @Test
    void shouldFetchAllRecipesByDevice() {
        // Given
        String deviceId = "fake-device-id";
        fakeRecipesAdapter.recipes = List.of(
                new Recipe("recipe-1", deviceId, "Curry", true, false),
                new Recipe("recipe-2", deviceId, "Ramen", false, true)
        );
        RecipesListResponse expectedResponse = new RecipesListResponse(List.of(
                new RecipeResponse("recipe-1", "Curry", true, false),
                new RecipeResponse("recipe-2", "Ramen", false, true)
        ));

        // When
        RecipesListResponse response = recipesFacade.fetchAllByDevice(deviceId);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(deviceId, fakeRecipesAdapter.deviceId);
    }

    @Test
    void shouldFetchEmptyListWhenNoRecipesForDevice() {
        // Given
        String deviceId = "fake-device-id-without-recipes";

        // When
        RecipesListResponse response = recipesFacade.fetchAllByDevice(deviceId);

        // Then
        assertEquals(new RecipesListResponse(List.of()), response);
        assertEquals(deviceId, fakeRecipesAdapter.deviceId);
    }
}
