package split.io.splitapi.gohan.ingredients.dao;

import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeIngredientsRepository implements IngredientsRepository {

    public List<IngredientEntity> ingredientsToReturn = new ArrayList<>();
    public String findByDeviceIdParam;
    public IngredientEntity savedIngredient;
    public String findByIdParam;
    public IngredientEntity ingredientToReturn;
    public String deletedIngredientId;

    @Override
    public List<IngredientEntity> findByDeviceId(String deviceId) {
        this.findByDeviceIdParam = deviceId;
        return ingredientsToReturn;
    }

    @Override
    public void save(IngredientEntity ingredientEntity) {
        this.savedIngredient = ingredientEntity;
    }

    @Override
    public Optional<IngredientEntity> findById(String id) {
        this.findByIdParam = id;
        return Optional.ofNullable(ingredientToReturn);
    }

    @Override
    public void deleteById(String id) {
        this.deletedIngredientId = id;
    }
}
