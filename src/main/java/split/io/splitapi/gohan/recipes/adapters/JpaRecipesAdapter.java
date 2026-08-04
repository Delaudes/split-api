package split.io.splitapi.gohan.recipes.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import split.io.splitapi.gohan.recipes.RecipesPort;
import split.io.splitapi.gohan.recipes.dao.RecipesRepository;
import split.io.splitapi.gohan.recipes.models.Recipe;
import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;

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

    private Recipe mapToRecipe(RecipeEntity entity) {
        return new Recipe(entity.getId(), entity.getDeviceId(), entity.getName(), entity.isInMealsList(), entity.isDone());
    }
}
