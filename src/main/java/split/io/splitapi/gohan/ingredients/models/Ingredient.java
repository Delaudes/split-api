package split.io.splitapi.gohan.ingredients.models;

public record Ingredient(String id, String deviceId, String name, boolean inShoppingList, boolean bought) {

    public Ingredient applyPatch(String name, Boolean inShoppingList, Boolean bought) {
        String newName = name != null ? name : this.name;
        boolean newInShoppingList = inShoppingList != null ? inShoppingList : this.inShoppingList;
        boolean newBought = bought != null ? bought : this.bought;
        if (Boolean.TRUE.equals(inShoppingList)) {
            newBought = false;
        }
        return new Ingredient(id, deviceId, newName, newInShoppingList, newBought);
    }
}
