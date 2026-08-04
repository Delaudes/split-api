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

    @Override
    public void save(Ingredient ingredient) {
        IngredientEntity entity = new IngredientEntity(ingredient.id(), ingredient.deviceId(), ingredient.name(), ingredient.inShoppingList(), ingredient.bought());
        ingredientsRepository.save(entity);
    }

    @Override
    public Ingredient fetchById(String id) {
        IngredientEntity entity = ingredientsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));
        return mapToIngredient(entity);
    }

    private Ingredient mapToIngredient(IngredientEntity entity) {
        return new Ingredient(entity.getId(), entity.getDeviceId(), entity.getName(), entity.isInShoppingList(), entity.isBought());
    }
}
