package split.io.splitapi.gohan.recipes.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;

import java.util.Optional;

public interface JpaRecipesRepository extends Repository<RecipeEntity, String>, RecipesRepository {

    @Override
    @Query("SELECT r FROM RecipeEntity r LEFT JOIN FETCH r.recipeIngredients ri LEFT JOIN FETCH ri.ingredient WHERE r.id = :id")
    Optional<RecipeEntity> findById(@Param("id") String id);
}
