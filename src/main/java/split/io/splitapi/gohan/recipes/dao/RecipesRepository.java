package split.io.splitapi.gohan.recipes.dao;

import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;

import java.util.List;
import java.util.Optional;

public interface RecipesRepository {
    List<RecipeEntity> findByDeviceId(String deviceId);
    Optional<RecipeEntity> findById(String id);
    void save(RecipeEntity recipeEntity);
    void updateFields(String id, String name, boolean inMealsList, boolean done);
}
