package split.io.splitapi.gohan.recipes.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;

public interface JpaRecipesRepository extends Repository<RecipeEntity, String>, RecipesRepository {

    @Modifying
    @Transactional
    @Query("UPDATE RecipeEntity r SET r.name = :name, r.inMealsList = :inMealsList, r.done = :done WHERE r.id = :id")
    void updateFields(@Param("id") String id, @Param("name") String name, @Param("inMealsList") boolean inMealsList, @Param("done") boolean done);
}
