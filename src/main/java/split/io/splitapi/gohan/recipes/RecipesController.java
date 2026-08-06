package split.io.splitapi.gohan.recipes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import split.io.splitapi.gohan.recipes.models.inputs.CreateRecipeRequest;
import split.io.splitapi.gohan.recipes.models.inputs.PatchRecipeIngredientRequest;
import split.io.splitapi.gohan.recipes.models.inputs.PatchRecipeRequest;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeDetailResponse;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeResponse;
import split.io.splitapi.gohan.recipes.models.outputs.RecipesListResponse;

@RestController
@RequestMapping("/gohan/recipes")
@RequiredArgsConstructor
@Validated
public class RecipesController {

    private final RecipesFacade recipesFacade;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RecipesListResponse fetchAll(@RequestHeader("X-Device-Id") @NotBlank String deviceId) {
        return recipesFacade.fetchAllByDevice(deviceId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeDetailResponse fetch(@PathVariable @NotBlank String id) {
        return recipesFacade.fetchById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeResponse create(@RequestHeader("X-Device-Id") @NotBlank String deviceId, @Valid @RequestBody CreateRecipeRequest request) {
        return recipesFacade.create(request, deviceId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeDetailResponse update(@PathVariable @NotBlank String id, @RequestBody PatchRecipeRequest request) {
        return recipesFacade.update(id, request);
    }

    @PostMapping("/{id}/ingredients/{ingredientId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDetailResponse addIngredient(@PathVariable @NotBlank String id, @PathVariable @NotBlank String ingredientId) {
        return recipesFacade.addIngredient(id, ingredientId);
    }

    @PatchMapping("/{id}/ingredients/{ingredientId}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeDetailResponse updateIngredientBought(@PathVariable @NotBlank String id, @PathVariable @NotBlank String ingredientId, @Valid @RequestBody PatchRecipeIngredientRequest request) {
        return recipesFacade.updateIngredientBought(id, ingredientId, request);
    }
}
