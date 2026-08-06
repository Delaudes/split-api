package split.io.splitapi.gohan.recipes.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;
import split.io.splitapi.gohan.recipes.dao.FakeIngredientsLookupRepository;
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
    private FakeIngredientsLookupRepository fakeIngredientsLookupRepository;

    @BeforeEach
    void setUp() {
        fakeRecipesRepository = new FakeRecipesRepository();
        fakeRecipeIngredientsRepository = new FakeRecipeIngredientsRepository();
        fakeIngredientsLookupRepository = new FakeIngredientsLookupRepository();
        adapter = new JpaRecipesAdapter(fakeRecipesRepository, fakeRecipeIngredientsRepository, fakeIngredientsLookupRepository);
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
        recipeEntity.getRecipeIngredients().add(new RecipeIngredientEntity("recipe-ingredient-1", recipeId, "ingredient-1", true));
        recipeEntity.getRecipeIngredients().add(new RecipeIngredientEntity("recipe-ingredient-2", recipeId, "ingredient-2", false));
        fakeRecipesRepository.recipeToReturn = recipeEntity;
        fakeIngredientsLookupRepository.ingredientsToReturn = List.of(
                new IngredientEntity("ingredient-1", deviceId, "Riz", false, false),
                new IngredientEntity("ingredient-2", deviceId, "Poulet", false, true)
        );

        RecipeDetail expectedDetail = new RecipeDetail(recipeId, "Curry", true, false, List.of(
                new RecipeIngredient("ingredient-1", "Riz", true),
                new RecipeIngredient("ingredient-2", "Poulet", false)
        ));

        // When
        RecipeDetail detail = adapter.fetchById(recipeId);

        // Then
        assertEquals(expectedDetail, detail);
        assertEquals(recipeId, fakeRecipesRepository.findByIdParam);
        assertEquals(List.of("ingredient-1", "ingredient-2"), fakeIngredientsLookupRepository.findAllByIdParam);
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

    @Test
    void shouldUpdateRecipeFields() {
        // Given
        String recipeId = "fake-recipe-id";
        RecipeDetail recipeDetail = new RecipeDetail(recipeId, "Curry maison", true, false, List.of());

        // When
        adapter.update(recipeDetail);

        // Then
        assertEquals(recipeId, fakeRecipesRepository.updateFieldsId);
        assertEquals("Curry maison", fakeRecipesRepository.updateFieldsName);
        assertTrue(fakeRecipesRepository.updateFieldsInMealsList);
        assertFalse(fakeRecipesRepository.updateFieldsDone);
    }

    @Test
    void shouldResetIngredientsBoughtForRecipe() {
        // Given
        String recipeId = "fake-recipe-id";

        // When
        adapter.resetIngredientsBought(recipeId);

        // Then
        assertEquals(recipeId, fakeRecipeIngredientsRepository.resetBoughtByRecipeIdParam);
    }

    @Test
    void shouldAttachIngredientToRecipe() {
        // Given
        String recipeId = "fake-recipe-id";
        String ingredientId = "fake-ingredient-id";
        String recipeIngredientId = "fake-recipe-ingredient-id";

        // When
        adapter.attachIngredient(recipeId, ingredientId, recipeIngredientId);

        // Then
        assertEquals(recipeIngredientId, fakeRecipeIngredientsRepository.savedRecipeIngredient.getId());
        assertEquals(recipeId, fakeRecipeIngredientsRepository.savedRecipeIngredient.getRecipeId());
        assertEquals(ingredientId, fakeRecipeIngredientsRepository.savedRecipeIngredient.getIngredientId());
        assertFalse(fakeRecipeIngredientsRepository.savedRecipeIngredient.isBought());
    }

    @Test
    void shouldUpdateIngredientBoughtForRecipe() {
        // Given
        String recipeId = "fake-recipe-id";
        String ingredientId = "fake-ingredient-id";

        // When
        adapter.updateIngredientBought(recipeId, ingredientId, true);

        // Then
        assertEquals(recipeId, fakeRecipeIngredientsRepository.updateBoughtRecipeIdParam);
        assertEquals(ingredientId, fakeRecipeIngredientsRepository.updateBoughtIngredientIdParam);
        assertTrue(fakeRecipeIngredientsRepository.updateBoughtParam);
    }

    @Test
    void shouldDetachIngredientFromRecipe() {
        // Given
        String recipeId = "fake-recipe-id";
        String ingredientId = "fake-ingredient-id";

        // When
        adapter.detachIngredient(recipeId, ingredientId);

        // Then
        assertEquals(recipeId, fakeRecipeIngredientsRepository.deleteRecipeIdParam);
        assertEquals(ingredientId, fakeRecipeIngredientsRepository.deleteIngredientIdParam);
    }
}
