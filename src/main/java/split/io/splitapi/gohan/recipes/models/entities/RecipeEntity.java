package split.io.splitapi.gohan.recipes.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "recipes")
public class RecipeEntity {

    @Id
    private String id;

    private String deviceId;

    @Setter
    private String name;

    @Setter
    private boolean inMealsList;

    @Setter
    private boolean done;

    @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private List<RecipeIngredientEntity> recipeIngredients = new ArrayList<>();

    public RecipeEntity() {
    }

    public RecipeEntity(String id, String deviceId, String name, boolean inMealsList, boolean done) {
        this.id = id;
        this.deviceId = deviceId;
        this.name = name;
        this.inMealsList = inMealsList;
        this.done = done;
    }
}
