package split.io.splitapi.gohan.ingredients.dao;

import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;

import java.util.List;

public interface IngredientsRepository {
    List<IngredientEntity> findByDeviceId(String deviceId);
}
