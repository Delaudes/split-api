package split.io.splitapi.gohan.recipes.dao;

import org.springframework.data.repository.Repository;
import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;

public interface JpaRecipesRepository extends Repository<RecipeEntity, String>, RecipesRepository {
}
