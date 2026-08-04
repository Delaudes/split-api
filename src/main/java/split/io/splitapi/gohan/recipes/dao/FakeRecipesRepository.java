package split.io.splitapi.gohan.recipes.dao;

import split.io.splitapi.gohan.recipes.models.entities.RecipeEntity;

import java.util.ArrayList;
import java.util.List;

public class FakeRecipesRepository implements RecipesRepository {

    public List<RecipeEntity> recipesToReturn = new ArrayList<>();
    public String findByDeviceIdParam;

    @Override
    public List<RecipeEntity> findByDeviceId(String deviceId) {
        this.findByDeviceIdParam = deviceId;
        return recipesToReturn;
    }
}
