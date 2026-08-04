package split.io.splitapi.gohan.recipes.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
