package split.io.splitapi.gohan.recipes.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import split.io.splitapi.gohan.recipes.models.entities.RecipeIngredientEntity;

public interface JpaRecipeIngredientsRepository extends Repository<RecipeIngredientEntity, String>, RecipeIngredientsRepository {

    @Modifying
    @Transactional
    @Query("UPDATE RecipeIngredientEntity ri SET ri.bought = false WHERE ri.recipeId = :recipeId")
    void resetBoughtByRecipeId(@Param("recipeId") String recipeId);

    @Modifying
    @Transactional
    @Query("UPDATE RecipeIngredientEntity ri SET ri.bought = :bought WHERE ri.recipeId = :recipeId AND ri.ingredientId = :ingredientId")
    void updateBought(@Param("recipeId") String recipeId, @Param("ingredientId") String ingredientId, @Param("bought") boolean bought);

    @Modifying
    @Transactional
    @Query("DELETE FROM RecipeIngredientEntity ri WHERE ri.recipeId = :recipeId AND ri.ingredientId = :ingredientId")
    void deleteByRecipeIdAndIngredientId(@Param("recipeId") String recipeId, @Param("ingredientId") String ingredientId);
}
