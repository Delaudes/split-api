package split.io.splitapi.gohan.ingredients.dao;

import org.springframework.data.repository.Repository;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;

public interface JpaIngredientsRepository extends Repository<IngredientEntity, String>, IngredientsRepository {
}
