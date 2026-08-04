package split.io.splitapi.gohan.ingredients;

import split.io.splitapi.gohan.ingredients.models.Ingredient;

import java.util.List;

public interface IngredientsPort {
    List<Ingredient> fetchAllByDevice(String deviceId);
    void save(Ingredient ingredient);
    Ingredient fetchById(String id);
}
