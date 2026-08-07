package split.io.splitapi.gohan.shopping.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import split.io.splitapi.gohan.ingredients.dao.IngredientsRepository;
import split.io.splitapi.gohan.recipes.dao.RecipesRepository;
import split.io.splitapi.gohan.shopping.ShoppingPort;
import split.io.splitapi.gohan.shopping.models.ShoppingItem;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaShoppingAdapter implements ShoppingPort {

    private final IngredientsRepository ingredientsRepository;
    private final RecipesRepository recipesRepository;

    @Override
    public List<ShoppingItem> fetchIngredientsInShoppingList(String deviceId) {
        return ingredientsRepository.findByDeviceIdAndInShoppingListTrue(deviceId).stream()
                .map(entity -> new ShoppingItem(entity.getId(), null, null, entity.getName(), entity.isBought()))
                .toList();
    }

    @Override
    public List<ShoppingItem> fetchMealIngredients(String deviceId) {
        return recipesRepository.findByDeviceIdAndInMealsListTrue(deviceId).stream()
                .flatMap(recipe -> recipe.getRecipeIngredients().stream()
                        .map(recipeIngredient -> new ShoppingItem(
                                recipeIngredient.getIngredientId(),
                                recipe.getId(),
                                recipe.getName(),
                                recipeIngredient.getIngredient().getName(),
                                recipeIngredient.isBought())))
                .toList();
    }
}
