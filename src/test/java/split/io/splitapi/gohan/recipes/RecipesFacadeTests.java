package split.io.splitapi.gohan.recipes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import split.io.splitapi.gohan.recipes.adapters.FakeRecipesAdapter;
import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.RecipeDetail;
import split.io.splitapi.gohan.recipes.models.RecipeIngredient;
import split.io.splitapi.gohan.recipes.models.inputs.CreateRecipeRequest;
import split.io.splitapi.gohan.recipes.models.inputs.PatchRecipeIngredientRequest;
import split.io.splitapi.gohan.recipes.models.inputs.PatchRecipeRequest;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeDetailResponse;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeIngredientResponse;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeResponse;
import split.io.splitapi.gohan.recipes.models.outputs.RecipesListResponse;
import split.io.splitapi.uuid.FakeUuidGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipesFacadeTests {

    private RecipesFacade recipesFacade;
    private FakeRecipesAdapter fakeRecipesAdapter;
    private FakeUuidGenerator fakeUuidGenerator;

    @BeforeEach
    void setUp() {
        fakeRecipesAdapter = new FakeRecipesAdapter();
        fakeUuidGenerator = new FakeUuidGenerator();
        RecipesService recipesService = new RecipesService(fakeRecipesAdapter);
        RecipesMapper recipesMapper = new RecipesMapper();
        recipesFacade = new RecipesFacade(recipesService, recipesMapper, fakeUuidGenerator);
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

    @Test
    void shouldFetchRecipeDetailById() {
        // Given
        String recipeId = "fake-recipe-id";
        fakeRecipesAdapter.recipeDetailToReturn = new RecipeDetail(recipeId, "Curry", true, false, List.of(
                new RecipeIngredient("ingredient-1", "Riz", true),
                new RecipeIngredient("ingredient-2", "Poulet", false)
        ));
        RecipeDetailResponse expectedResponse = new RecipeDetailResponse(recipeId, "Curry", true, false, List.of(
                new RecipeIngredientResponse("ingredient-1", "Riz", true),
                new RecipeIngredientResponse("ingredient-2", "Poulet", false)
        ));

        // When
        RecipeDetailResponse response = recipesFacade.fetchById(recipeId);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(recipeId, fakeRecipesAdapter.fetchByIdParam);
    }

    @Test
    void shouldCreateRecipeWithGeneratedId() {
        // Given
        String deviceId = "fake-device-id";
        String recipeName = "Curry";
        CreateRecipeRequest request = new CreateRecipeRequest(recipeName);
        Recipe expectedRecipe = new Recipe(fakeUuidGenerator.uuid, deviceId, recipeName, false, false);
        RecipeResponse expectedResponse = new RecipeResponse(fakeUuidGenerator.uuid, recipeName, false, false);

        // When
        RecipeResponse response = recipesFacade.create(request, deviceId);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(expectedRecipe, fakeRecipesAdapter.savedRecipe);
    }

    @Test
    void shouldNotChangeAnythingWhenNoFieldsProvided() {
        // Given
        String recipeId = "fake-recipe-id";
        fakeRecipesAdapter.recipeDetailToReturn = new RecipeDetail(recipeId, "Curry", true, false, List.of(
                new RecipeIngredient("ingredient-1", "Riz", true)
        ));
        PatchRecipeRequest request = new PatchRecipeRequest(null, null, null);
        RecipeDetailResponse expectedResponse = new RecipeDetailResponse(recipeId, "Curry", true, false, List.of(
                new RecipeIngredientResponse("ingredient-1", "Riz", true)
        ));

        // When
        RecipeDetailResponse response = recipesFacade.update(recipeId, request);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(fakeRecipesAdapter.recipeDetailToReturn, fakeRecipesAdapter.updatedRecipeDetail);
    }

    @Test
    void shouldUpdateEveryField() {
        // Given
        String recipeId = "fake-recipe-id";
        fakeRecipesAdapter.recipeDetailToReturn = new RecipeDetail(recipeId, "Curry", true, true, List.of(
                new RecipeIngredient("ingredient-1", "Riz", false)
        ));
        PatchRecipeRequest request = new PatchRecipeRequest("Curry maison", false, false);
        RecipeDetailResponse expectedResponse = new RecipeDetailResponse(recipeId, "Curry maison", false, false, List.of(
                new RecipeIngredientResponse("ingredient-1", "Riz", false)
        ));

        // When
        RecipeDetailResponse response = recipesFacade.update(recipeId, request);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(recipeId, fakeRecipesAdapter.fetchByIdParam);
        assertEquals(fakeRecipesAdapter.recipeDetailToReturn, fakeRecipesAdapter.updatedRecipeDetail);
    }

    @Test
    void shouldResetIngredientsBoughtWhenInMealsListTrue() {
        // Given
        String recipeId = "fake-recipe-id";
        fakeRecipesAdapter.recipeDetailToReturn = new RecipeDetail(recipeId, "Curry", false, true, List.of(
                new RecipeIngredient("ingredient-1", "Riz", true),
                new RecipeIngredient("ingredient-2", "Poulet", false)
        ));
        PatchRecipeRequest request = new PatchRecipeRequest(null, true, null);
        RecipeDetailResponse expectedResponse = new RecipeDetailResponse(recipeId, "Curry", true, false, List.of(
                new RecipeIngredientResponse("ingredient-1", "Riz", false),
                new RecipeIngredientResponse("ingredient-2", "Poulet", false)
        ));

        // When
        RecipeDetailResponse response = recipesFacade.update(recipeId, request);

        // Then
        assertEquals(expectedResponse, response);
        assertTrue(fakeRecipesAdapter.updatedRecipeDetail.ingredients().stream().noneMatch(RecipeIngredient::bought));
    }

    @Test
    void shouldNotChangeIngredientsBoughtWhenInMealsListFalse() {
        // Given
        String recipeId = "fake-recipe-id";
        fakeRecipesAdapter.recipeDetailToReturn = new RecipeDetail(recipeId, "Curry", true, false, List.of(
                new RecipeIngredient("ingredient-1", "Riz", true)
        ));
        PatchRecipeRequest request = new PatchRecipeRequest(null, false, null);

        // When
        recipesFacade.update(recipeId, request);

        // Then
        assertTrue(fakeRecipesAdapter.updatedRecipeDetail.ingredients().getFirst().bought());
    }

    @Test
    void shouldCreateRecipeIngredientWithGeneratedId() {
        // Given
        String recipeId = "fake-recipe-id";
        String ingredientId = "fake-ingredient-id";
        fakeRecipesAdapter.recipeDetailToReturn = new RecipeDetail(recipeId, "Curry", false, false, List.of(
                new RecipeIngredient(ingredientId, "Riz", false)
        ));
        RecipeDetailResponse expectedResponse = new RecipeDetailResponse(recipeId, "Curry", false, false, List.of(
                new RecipeIngredientResponse(ingredientId, "Riz", false)
        ));

        // When
        RecipeDetailResponse response = recipesFacade.createRecipeIngredient(recipeId, ingredientId);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(recipeId, fakeRecipesAdapter.saveRecipeIngredientRecipeIdParam);
        assertEquals(ingredientId, fakeRecipesAdapter.saveRecipeIngredientIngredientIdParam);
        assertEquals(fakeUuidGenerator.uuid, fakeRecipesAdapter.saveRecipeIngredientRecipeIngredientIdParam);
    }

    @Test
    void shouldUpdateRecipeIngredient() {
        // Given
        String recipeId = "fake-recipe-id";
        String ingredientId = "fake-ingredient-id";
        fakeRecipesAdapter.recipeDetailToReturn = new RecipeDetail(recipeId, "Curry", false, false, List.of(
                new RecipeIngredient(ingredientId, "Riz", false),
                new RecipeIngredient("other-ingredient-id", "Poulet", false)
        ));
        PatchRecipeIngredientRequest request = new PatchRecipeIngredientRequest(true);
        RecipeDetailResponse expectedResponse = new RecipeDetailResponse(recipeId, "Curry", false, false, List.of(
                new RecipeIngredientResponse(ingredientId, "Riz", true),
                new RecipeIngredientResponse("other-ingredient-id", "Poulet", false)
        ));

        // When
        RecipeDetailResponse response = recipesFacade.updateRecipeIngredient(recipeId, ingredientId, request);

        // Then
        assertEquals(expectedResponse, response);
        assertEquals(recipeId, fakeRecipesAdapter.fetchByIdParam);
        assertEquals(expectedResponse.ingredients(), fakeRecipesAdapter.updatedRecipeDetail.ingredients().stream()
                .map(ri -> new RecipeIngredientResponse(ri.id(), ri.name(), ri.bought()))
                .toList());
    }

    @Test
    void shouldDeleteRecipeIngredient() {
        // Given
        String recipeId = "fake-recipe-id";
        String ingredientId = "fake-ingredient-id";

        // When
        recipesFacade.deleteRecipeIngredient(recipeId, ingredientId);

        // Then
        assertEquals(recipeId, fakeRecipesAdapter.deleteRecipeIngredientRecipeIdParam);
        assertEquals(ingredientId, fakeRecipesAdapter.deleteRecipeIngredientIngredientIdParam);
    }

    @Test
    void shouldDeleteRecipeWhenNotInMealsList() {
        // Given
        String recipeId = "fake-recipe-id";
        fakeRecipesAdapter.recipeDetailToReturn = new RecipeDetail(recipeId, "Curry", false, false, List.of());

        // When
        recipesFacade.delete(recipeId);

        // Then
        assertEquals(recipeId, fakeRecipesAdapter.deletedRecipeId);
    }

    @Test
    void shouldThrowConflictWhenDeletingRecipeInMealsList() {
        // Given
        String recipeId = "fake-recipe-id";
        fakeRecipesAdapter.recipeDetailToReturn = new RecipeDetail(recipeId, "Curry", true, false, List.of());

        // Then
        assertThrows(RecipeInMealsListException.class, () -> recipesFacade.delete(recipeId));
        assertNull(fakeRecipesAdapter.deletedRecipeId);
    }
}
