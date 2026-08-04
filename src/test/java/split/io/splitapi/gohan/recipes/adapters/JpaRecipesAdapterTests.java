package split.io.splitapi.gohan.recipes.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;
import split.io.splitapi.gohan.recipes.dao.FakeRecipesRepository;
import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.RecipeDetail;
import split.io.splitapi.gohan.recipes.models.RecipeIngredient;
import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;
import split.io.splitapi.gohan.recipes.models.entities.RecipeIngredientEntity;

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

    @Test
    void shouldFetchRecipeDetailById() {
        // Given
        String recipeId = "fake-recipe-id";
        String deviceId = "fake-device-id";
        RecipeEntity recipeEntity = new RecipeEntity(recipeId, deviceId, "Curry", true, false);
        IngredientEntity ingredient1 = new IngredientEntity("ingredient-1", deviceId, "Riz", false, false);
        IngredientEntity ingredient2 = new IngredientEntity("ingredient-2", deviceId, "Poulet", false, true);
        recipeEntity.getRecipeIngredients().add(new RecipeIngredientEntity("recipe-ingredient-1", recipeId, ingredient1, true));
        recipeEntity.getRecipeIngredients().add(new RecipeIngredientEntity("recipe-ingredient-2", recipeId, ingredient2, false));
        fakeRecipesRepository.recipeToReturn = recipeEntity;

        RecipeDetail expectedDetail = new RecipeDetail(recipeId, "Curry", true, false, List.of(
                new RecipeIngredient("ingredient-1", "Riz", true),
                new RecipeIngredient("ingredient-2", "Poulet", false)
        ));

        // When
        RecipeDetail detail = adapter.fetchById(recipeId);

        // Then
        assertEquals(expectedDetail, detail);
        assertEquals(recipeId, fakeRecipesRepository.findByIdParam);
    }

    @Test
    void shouldThrowWhenRecipeToFetchNotFound() {
        // Given
        String recipeId = "unknown-recipe-id";

        // Then
        assertThrows(RuntimeException.class, () -> adapter.fetchById(recipeId));
    }

    @Test
    void shouldSaveRecipe() {
        // Given
        String deviceId = "fake-device-id";
        Recipe recipe = new Recipe("recipe-1", deviceId, "Curry", false, false);

        // When
        adapter.save(recipe);

        // Then
        assertEquals(recipe.id(), fakeRecipesRepository.savedRecipe.getId());
        assertEquals(recipe.deviceId(), fakeRecipesRepository.savedRecipe.getDeviceId());
        assertEquals(recipe.name(), fakeRecipesRepository.savedRecipe.getName());
        assertEquals(recipe.inMealsList(), fakeRecipesRepository.savedRecipe.isInMealsList());
        assertEquals(recipe.done(), fakeRecipesRepository.savedRecipe.isDone());
    }
}
