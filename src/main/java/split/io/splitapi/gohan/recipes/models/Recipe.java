package split.io.splitapi.gohan.recipes.models;

public record Recipe(String id, String deviceId, String name, boolean inMealsList, boolean done) {
}
