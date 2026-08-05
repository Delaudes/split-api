package split.io.splitapi.gohan.recipes.dao;

import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;

import java.util.List;

public interface IngredientsLookupRepository {
    List<IngredientEntity> findAllById(List<String> ids);
}
