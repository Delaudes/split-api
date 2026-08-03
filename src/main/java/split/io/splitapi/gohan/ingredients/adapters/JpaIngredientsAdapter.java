package split.io.splitapi.gohan.ingredients.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import split.io.splitapi.gohan.ingredients.IngredientsPort;
import split.io.splitapi.gohan.ingredients.dao.IngredientsRepository;
import split.io.splitapi.gohan.ingredients.models.Ingredient;
import split.io.splitapi.gohan.ingredients.models.entities.IngredientEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaIngredientsAdapter implements IngredientsPort {

    private final IngredientsRepository ingredientsRepository;

    @Override
    public List<Ingredient> fetchAllByDevice(String deviceId) {
        return ingredientsRepository.findByDeviceId(deviceId).stream()
                .map(this::mapToIngredient)
                .toList();
    }

    private Ingredient mapToIngredient(IngredientEntity entity) {
        return new Ingredient(entity.getId(), entity.getDeviceId(), entity.getName(), entity.isInShoppingList(), entity.isBought());
    }
}
