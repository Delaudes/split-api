package split.io.splitapi.gohan.recipes.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;
import split.io.splitapi.gohan.recipes.RecipesPort;
import split.io.splitapi.gohan.recipes.dao.RecipesRepository;
import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.RecipeDetail;
import split.io.splitapi.gohan.recipes.models.RecipeIngredient;
import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;
import split.io.splitapi.gohan.recipes.models.entities.RecipeIngredientEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaRecipesAdapter implements RecipesPort {

    private final RecipesRepository recipesRepository;

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
                .map(this::mapToRecipeIngredient)
                .toList();
        return new RecipeDetail(entity.getId(), entity.getName(), entity.isInMealsList(), entity.isDone(), ingredients);
    }

    @Override
    public void save(Recipe recipe) {
        RecipeEntity entity = new RecipeEntity(recipe.id(), recipe.deviceId(), recipe.name(), recipe.inMealsList(), recipe.done());
        recipesRepository.save(entity);
    }

    private RecipeIngredient mapToRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity) {
        IngredientEntity ingredient = recipeIngredientEntity.getIngredient();
        return new RecipeIngredient(ingredient.getId(), ingredient.getName(), recipeIngredientEntity.isBought());
    }

    private Recipe mapToRecipe(RecipeEntity entity) {
        return new Recipe(entity.getId(), entity.getDeviceId(), entity.getName(), entity.isInMealsList(), entity.isDone());
    }
}
