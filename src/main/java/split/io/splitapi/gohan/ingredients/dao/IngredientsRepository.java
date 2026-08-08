package split.io.splitapi.gohan.ingredients.dao;

import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;

import java.util.List;
import java.util.Optional;

public interface IngredientsRepository {
    List<IngredientEntity> findByDeviceId(String deviceId);
    List<IngredientEntity> findByDeviceIdAndInShoppingListTrue(String deviceId);
    void save(IngredientEntity ingredientEntity);
    Optional<IngredientEntity> findById(String id);
    void deleteById(String id);
}
