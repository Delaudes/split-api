package split.io.splitapi.gohan.recipes.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import split.io.splitapi.gohan.ingredients.dao.IngredientsRepository;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;
import split.io.splitapi.gohan.recipes.RecipesPort;
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
    private final IngredientsRepository ingredientsRepository;

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

        List<RecipeIngredient> ingredients = entity.getRecipeIngredients().stream()
                .map(recipeIngredientEntity -> new RecipeIngredient(
                        recipeIngredientEntity.getIngredientId(),
                        recipeIngredientEntity.getIngredient().getName(),
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
        RecipeEntity entity = recipesRepository.findById(recipeDetail.id())
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeDetail.id()));
        entity.setName(recipeDetail.name());
        entity.setInMealsList(recipeDetail.inMealsList());
        entity.setDone(recipeDetail.done());

        Map<String, Boolean> boughtByIngredientId = recipeDetail.ingredients().stream()
                .collect(Collectors.toMap(RecipeIngredient::id, RecipeIngredient::bought));
        entity.getRecipeIngredients().forEach(recipeIngredientEntity -> {
            Boolean bought = boughtByIngredientId.get(recipeIngredientEntity.getIngredientId());
            if (bought != null) {
                recipeIngredientEntity.setBought(bought);
            }
        });

        recipesRepository.save(entity);
    }

    @Override
    public void saveRecipeIngredient(String recipeId, String ingredientId, String recipeIngredientId) {
        RecipeIngredientEntity entity = new RecipeIngredientEntity(recipeIngredientId, recipeId, ingredientId, false);
        IngredientEntity ingredient = ingredientsRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + ingredientId));
        // ingredientId alone would insert fine, but the subsequent fetchById in this same request reuses this
        // managed instance instead of reloading from DB, so the lazy ingredient relation must be set here too.
        entity.setIngredient(ingredient);
        recipeIngredientsRepository.save(entity);
    }

    @Override
    public void deleteRecipeIngredient(String recipeId, String ingredientId) {
        recipeIngredientsRepository.deleteByRecipeIdAndIngredientId(recipeId, ingredientId);
    }

    @Override
    public void delete(String id) {
        recipesRepository.deleteById(id);
    }

    private Recipe mapToRecipe(RecipeEntity entity) {
        return new Recipe(entity.getId(), entity.getDeviceId(), entity.getName(), entity.isInMealsList(), entity.isDone());
    }
}
