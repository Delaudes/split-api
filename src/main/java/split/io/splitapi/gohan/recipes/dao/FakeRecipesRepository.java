package split.io.splitapi.gohan.recipes.dao;

import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeRecipesRepository implements RecipesRepository {

    public List<RecipeEntity> recipesToReturn = new ArrayList<>();
    public String findByDeviceIdParam;
    public RecipeEntity recipeToReturn;
    public String findByIdParam;
    public RecipeEntity savedRecipe;
    public String updateFieldsId;
    public String updateFieldsName;
    public Boolean updateFieldsInMealsList;
    public Boolean updateFieldsDone;

    @Override
    public List<RecipeEntity> findByDeviceId(String deviceId) {
        this.findByDeviceIdParam = deviceId;
        return recipesToReturn;
    }

    @Override
    public Optional<RecipeEntity> findById(String id) {
        this.findByIdParam = id;
        return Optional.ofNullable(recipeToReturn);
    }

    @Override
    public void save(RecipeEntity recipeEntity) {
        this.savedRecipe = recipeEntity;
    }

    @Override
    public void updateFields(String id, String name, boolean inMealsList, boolean done) {
        this.updateFieldsId = id;
        this.updateFieldsName = name;
        this.updateFieldsInMealsList = inMealsList;
        this.updateFieldsDone = done;
    }
}
