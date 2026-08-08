package split.io.splitapi.gohan.recipes.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;
import split.io.splitapi.gohan.recipes.dao.FakeRecipeIngredientsRepository;
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
    private FakeRecipeIngredientsRepository fakeRecipeIngredientsRepository;

    @BeforeEach
    void setUp() {
        fakeRecipesRepository = new FakeRecipesRepository();
        fakeRecipeIngredientsRepository = new FakeRecipeIngredientsRepository();
        adapter = new JpaRecipesAdapter(fakeRecipesRepository, fakeRecipeIngredientsRepository);
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
        RecipeIngredientEntity recipeIngredient1 = new RecipeIngredientEntity("recipe-ingredient-1", recipeId, "ingredient-1", true);
        recipeIngredient1.setIngredient(new IngredientEntity("ingredient-1", deviceId, "Riz", false, false));
        RecipeIngredientEntity recipeIngredient2 = new RecipeIngredientEntity("recipe-ingredient-2", recipeId, "ingredient-2", false);
        recipeIngredient2.setIngredient(new IngredientEntity("ingredient-2", deviceId, "Poulet", false, true));
        recipeEntity.getRecipeIngredients().add(recipeIngredient1);
        recipeEntity.getRecipeIngredients().add(recipeIngredient2);
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
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adapter.fetchById(recipeId));
        assertEquals("Recipe not found with id: " + recipeId, exception.getMessage());
    }

    @Test
    void shouldSaveRecipe() {
        // Given
        String deviceId = "fake-device-id";
        Recipe recipe = new Recipe("recipe-1", deviceId, "Curry", true, false);

        // When
        adapter.save(recipe);

        // Then
        assertEquals(recipe.id(), fakeRecipesRepository.savedRecipe.getId());
        assertEquals(recipe.deviceId(), fakeRecipesRepository.savedRecipe.getDeviceId());
        assertEquals(recipe.name(), fakeRecipesRepository.savedRecipe.getName());
        assertEquals(recipe.inMealsList(), fakeRecipesRepository.savedRecipe.isInMealsList());
        assertEquals(recipe.done(), fakeRecipesRepository.savedRecipe.isDone());
    }

    @Test
    void shouldUpdateRecipeScalarFields() {
        // Given
        String recipeId = "fake-recipe-id";
        fakeRecipesRepository.recipeToReturn = new RecipeEntity(recipeId, "fake-device-id", "Curry", false, true);
        RecipeDetail recipeDetail = new RecipeDetail(recipeId, "Curry maison", true, false, List.of());

        // When
        adapter.update(recipeDetail);

        // Then
        assertEquals(recipeId, fakeRecipesRepository.findByIdParam);
        assertEquals("Curry maison", fakeRecipesRepository.savedRecipe.getName());
        assertTrue(fakeRecipesRepository.savedRecipe.isInMealsList());
        assertFalse(fakeRecipesRepository.savedRecipe.isDone());
    }

    @Test
    void shouldSyncIngredientsBoughtFromRecipeDetailOnUpdate() {
        // Given
        String recipeId = "fake-recipe-id";
        String deviceId = "fake-device-id";
        RecipeEntity recipeEntity = new RecipeEntity(recipeId, deviceId, "Curry", true, false);
        RecipeIngredientEntity recipeIngredient1 = new RecipeIngredientEntity("recipe-ingredient-1", recipeId, "ingredient-1", true);
        RecipeIngredientEntity recipeIngredient2 = new RecipeIngredientEntity("recipe-ingredient-2", recipeId, "ingredient-2", false);
        recipeEntity.getRecipeIngredients().add(recipeIngredient1);
        recipeEntity.getRecipeIngredients().add(recipeIngredient2);
        fakeRecipesRepository.recipeToReturn = recipeEntity;

        RecipeDetail recipeDetail = new RecipeDetail(recipeId, "Curry", true, false, List.of(
                new RecipeIngredient("ingredient-1", "Riz", false),
                new RecipeIngredient("ingredient-2", "Poulet", true)
        ));

        // When
        adapter.update(recipeDetail);

        // Then
        assertFalse(recipeIngredient1.isBought());
        assertTrue(recipeIngredient2.isBought());
        assertSame(recipeEntity, fakeRecipesRepository.savedRecipe);
    }

    @Test
    void shouldThrowWhenRecipeToUpdateNotFound() {
        // Given
        String recipeId = "unknown-recipe-id";
        RecipeDetail recipeDetail = new RecipeDetail(recipeId, "Curry maison", true, false, List.of());

        // Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adapter.update(recipeDetail));
        assertEquals("Recipe not found with id: " + recipeId, exception.getMessage());
    }

    @Test
    void shouldSaveRecipeIngredient() {
        // Given
        String recipeId = "fake-recipe-id";
        String ingredientId = "fake-ingredient-id";
        String recipeIngredientId = "fake-recipe-ingredient-id";

        // When
        adapter.saveRecipeIngredient(recipeId, ingredientId, recipeIngredientId);

        // Then
        assertEquals(recipeIngredientId, fakeRecipeIngredientsRepository.savedRecipeIngredient.getId());
        assertEquals(recipeId, fakeRecipeIngredientsRepository.savedRecipeIngredient.getRecipeId());
        assertEquals(ingredientId, fakeRecipeIngredientsRepository.savedRecipeIngredient.getIngredientId());
        assertFalse(fakeRecipeIngredientsRepository.savedRecipeIngredient.isBought());
    }

    @Test
    void shouldDeleteRecipeIngredient() {
        // Given
        String recipeId = "fake-recipe-id";
        String ingredientId = "fake-ingredient-id";

        // When
        adapter.deleteRecipeIngredient(recipeId, ingredientId);

        // Then
        assertEquals(recipeId, fakeRecipeIngredientsRepository.deleteRecipeIdParam);
        assertEquals(ingredientId, fakeRecipeIngredientsRepository.deleteIngredientIdParam);
    }

    @Test
    void shouldDeleteRecipe() {
        // Given
        String recipeId = "fake-recipe-id";

        // When
        adapter.delete(recipeId);

        // Then
        assertEquals(recipeId, fakeRecipesRepository.deletedRecipeId);
    }
}
