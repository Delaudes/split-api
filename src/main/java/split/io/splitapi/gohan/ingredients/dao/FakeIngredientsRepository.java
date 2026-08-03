package split.io.splitapi.gohan.ingredients.dao;

import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;

import java.util.ArrayList;
import java.util.List;

public class FakeIngredientsRepository implements IngredientsRepository {

    public List<IngredientEntity> ingredientsToReturn = new ArrayList<>();
    public String findByDeviceIdParam;

    @Override
    public List<IngredientEntity> findByDeviceId(String deviceId) {
        this.findByDeviceIdParam = deviceId;
        return ingredientsToReturn;
    }
}
