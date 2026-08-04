package split.io.splitapi.gohan.recipes;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.gohan.recipes.models.Recipe;

import java.util.List;

@RequiredArgsConstructor
public class RecipesService {

    private final RecipesPort recipesPort;

    public List<Recipe> fetchAllByDevice(String deviceId) {
        return recipesPort.fetchAllByDevice(deviceId);
    }
}
