package split.io.splitapi.gohan.recipes.dao;

import org.springframework.data.repository.Repository;
import split.io.splitapi.gohan.recipes.models.entities.RecipeIngredientEntity;

public interface JpaRecipeIngredientsRepository extends Repository<RecipeIngredientEntity, String>, RecipeIngredientsRepository {
}
