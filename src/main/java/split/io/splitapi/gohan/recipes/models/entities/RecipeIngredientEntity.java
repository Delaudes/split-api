package split.io.splitapi.gohan.recipes.models.entities;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredient;

    @Setter
    private boolean bought;

    public RecipeIngredientEntity() {
    }

    public RecipeIngredientEntity(String id, String recipeId, IngredientEntity ingredient, boolean bought) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredient = ingredient;
        this.bought = bought;
    }
}
