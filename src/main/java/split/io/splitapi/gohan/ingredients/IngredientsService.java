package split.io.splitapi.gohan.ingredients;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.gohan.ingredients.models.Ingredient;

import java.util.List;

@RequiredArgsConstructor
public class IngredientsService {

    private final IngredientsPort ingredientsPort;

    public List<Ingredient> fetchAllByDevice(String deviceId) {
        return ingredientsPort.fetchAllByDevice(deviceId);
    }
}
