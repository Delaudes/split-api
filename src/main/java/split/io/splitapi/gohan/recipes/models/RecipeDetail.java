package split.io.splitapi.gohan.recipes.models;

import java.util.List;

public record RecipeDetail(String id, String name, boolean inMealsList, boolean done, List<RecipeIngredient> ingredients) {

    public RecipeDetail applyPatch(String name, Boolean inMealsList, Boolean done) {
        String newName = name != null ? name : this.name;
        boolean newInMealsList = inMealsList != null ? inMealsList : this.inMealsList;
        boolean newDone = done != null ? done : this.done;
        List<RecipeIngredient> newIngredients = this.ingredients;
        if (Boolean.TRUE.equals(inMealsList)) {
            newDone = false;
            newIngredients = this.ingredients.stream()
                    .map(ingredient -> new RecipeIngredient(ingredient.id(), ingredient.name(), false))
                    .toList();
        }
        return new RecipeDetail(id, newName, newInMealsList, newDone, newIngredients);
    }

    public boolean hasIngredientsBought() {
        return ingredients.stream().anyMatch(RecipeIngredient::bought);
    }
}
