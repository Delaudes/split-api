package split.io.splitapi.gohan.recipes.adapters;

import split.io.splitapi.gohan.recipes.RecipesPort;
import split.io.splitapi.gohan.recipes.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class FakeRecipesAdapter implements RecipesPort {

    public List<Recipe> recipes = new ArrayList<>();
    public String deviceId;

    @Override
    public List<Recipe> fetchAllByDevice(String deviceId) {
        this.deviceId = deviceId;
        return recipes;
    }
}
