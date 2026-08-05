package split.io.splitapi.gohan.recipes.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;
import split.io.splitapi.gohan.recipes.RecipesPort;
import split.io.splitapi.gohan.recipes.dao.IngredientsLookupRepository;
import split.io.splitapi.gohan.recipes.dao.RecipeIngredientsRepository;
import split.io.splitapi.gohan.recipes.dao.RecipesRepository;
import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.RecipeDetail;
import split.io.splitapi.gohan.recipes.models.RecipeIngredient;
import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;
import split.io.splitapi.gohan.recipes.models.entities.RecipeIngredientEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaRecipesAdapter implements RecipesPort {

    private final RecipesRepository recipesRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;
    private final IngredientsLookupRepository ingredientsLookupRepository;

    @Override
    public List<Recipe> fetchAllByDevice(String deviceId) {
        return recipesRepository.findByDeviceId(deviceId).stream()
                .map(this::mapToRecipe)
                .toList();
    }

    @Override
    public RecipeDetail fetchById(String id) {
        RecipeEntity entity = recipesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));

        List<String> ingredientIds = entity.getRecipeIngredients().stream()
                .map(RecipeIngredientEntity::getIngredientId)
                .toList();
        Map<String, String> nameByIngredientId = ingredientsLookupRepository.findAllById(ingredientIds).stream()
                .collect(Collectors.toMap(IngredientEntity::getId, IngredientEntity::getName));

        List<RecipeIngredient> ingredients = entity.getRecipeIngredients().stream()
                .map(recipeIngredientEntity -> new RecipeIngredient(
                        recipeIngredientEntity.getIngredientId(),
                        nameByIngredientId.get(recipeIngredientEntity.getIngredientId()),
                        recipeIngredientEntity.isBought()))
                .toList();
        return new RecipeDetail(entity.getId(), entity.getName(), entity.isInMealsList(), entity.isDone(), ingredients);
    }

    @Override
    public void save(Recipe recipe) {
        RecipeEntity entity = new RecipeEntity(recipe.id(), recipe.deviceId(), recipe.name(), recipe.inMealsList(), recipe.done());
        recipesRepository.save(entity);
    }

    @Override
    public void update(RecipeDetail recipeDetail) {
        recipesRepository.updateFields(recipeDetail.id(), recipeDetail.name(), recipeDetail.inMealsList(), recipeDetail.done());
    }

    @Override
    public void resetIngredientsBought(String recipeId) {
        recipeIngredientsRepository.resetBoughtByRecipeId(recipeId);
    }

    @Override
    public void attachIngredient(String recipeId, String ingredientId, String recipeIngredientId) {
        RecipeIngredientEntity entity = new RecipeIngredientEntity(recipeIngredientId, recipeId, ingredientId, false);
        recipeIngredientsRepository.save(entity);
    }

    private Recipe mapToRecipe(RecipeEntity entity) {
        return new Recipe(entity.getId(), entity.getDeviceId(), entity.getName(), entity.isInMealsList(), entity.isDone());
    }
}
