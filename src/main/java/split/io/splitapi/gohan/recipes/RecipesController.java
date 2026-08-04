package split.io.splitapi.gohan.recipes;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import split.io.splitapi.gohan.recipes.models.outputs.RecipeDetailResponse;
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
}
