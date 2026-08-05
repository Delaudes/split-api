package split.io.splitapi.gohan.recipes.dao;

import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;

import java.util.ArrayList;
import java.util.List;

public class FakeIngredientsLookupRepository implements IngredientsLookupRepository {

    public List<IngredientEntity> ingredientsToReturn = new ArrayList<>();
    public List<String> findAllByIdParam;

    @Override
    public List<IngredientEntity> findAllById(List<String> ids) {
        this.findAllByIdParam = ids;
        return ingredientsToReturn;
    }
}
