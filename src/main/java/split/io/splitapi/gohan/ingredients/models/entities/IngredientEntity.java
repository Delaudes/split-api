package split.io.splitapi.gohan.ingredients.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "ingredients")
public class IngredientEntity {

    @Id
    private String id;

    private String deviceId;

    @Setter
    private String name;

    @Setter
    private boolean inShoppingList;

    @Setter
    private boolean bought;

    public IngredientEntity() {
    }

    public IngredientEntity(String id, String deviceId, String name, boolean inShoppingList, boolean bought) {
        this.id = id;
        this.deviceId = deviceId;
        this.name = name;
        this.inShoppingList = inShoppingList;
        this.bought = bought;
    }
}
