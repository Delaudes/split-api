package split.io.splitapi.gohan.recipes.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;

@Getter
@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredientEntity {

    @Id
    private String id;

    @Column(name = "recipe_id")
    private String recipeId;

    @Column(name = "ingredient_id")
    private String ingredientId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", insertable = false, updatable = false)
    private IngredientEntity ingredient;

    @Setter
    private boolean bought;

    public RecipeIngredientEntity() {
    }

    public RecipeIngredientEntity(String id, String recipeId, String ingredientId, boolean bought) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.bought = bought;
    }
}
