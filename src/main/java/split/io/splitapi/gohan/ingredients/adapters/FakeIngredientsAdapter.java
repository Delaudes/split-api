package split.io.splitapi.gohan.ingredients.adapters;

import split.io.splitapi.gohan.ingredients.IngredientsPort;
import split.io.splitapi.gohan.ingredients.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class FakeIngredientsAdapter implements IngredientsPort {

    public List<Ingredient> ingredients = new ArrayList<>();
    public String deviceId;

    @Override
    public List<Ingredient> fetchAllByDevice(String deviceId) {
        this.deviceId = deviceId;
        return ingredients;
    }
}
