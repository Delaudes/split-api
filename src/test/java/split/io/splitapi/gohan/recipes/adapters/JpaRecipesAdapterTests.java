package split.io.splitapi.gohan.recipes.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.recipes.dao.FakeRecipesRepository;
import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JpaRecipesAdapterTests {

    private JpaRecipesAdapter adapter;
    private FakeRecipesRepository fakeRecipesRepository;

    @BeforeEach
    void setUp() {
        fakeRecipesRepository = new FakeRecipesRepository();
        adapter = new JpaRecipesAdapter(fakeRecipesRepository);
    }

    @Test
    void shouldFetchAllRecipesByDevice() {
        // Given
        String deviceId = "fake-device-id";
        fakeRecipesRepository.recipesToReturn = List.of(
                new RecipeEntity("recipe-1", deviceId, "Curry", true, false),
                new RecipeEntity("recipe-2", deviceId, "Ramen", false, true)
        );
        List<Recipe> expectedRecipes = List.of(
                new Recipe("recipe-1", deviceId, "Curry", true, false),
                new Recipe("recipe-2", deviceId, "Ramen", false, true)
        );

        // When
        List<Recipe> recipes = adapter.fetchAllByDevice(deviceId);

        // Then
        assertEquals(expectedRecipes, recipes);
        assertEquals(deviceId, fakeRecipesRepository.findByDeviceIdParam);
    }
}
