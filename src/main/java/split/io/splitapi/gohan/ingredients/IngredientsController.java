package split.io.splitapi.gohan.ingredients;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import split.io.splitapi.gohan.ingredients.models.inputs.CreateIngredientRequest;
import split.io.splitapi.gohan.ingredients.models.inputs.PatchIngredientRequest;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientResponse;
import split.io.splitapi.gohan.ingredients.models.outputs.IngredientsListResponse;

@RestController
@RequestMapping("/gohan/ingredients")
@RequiredArgsConstructor
@Validated
public class IngredientsController {

    private final IngredientsFacade ingredientsFacade;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public IngredientsListResponse fetchAllByDevice(@RequestHeader("X-Device-Id") @NotBlank String deviceId) {
        return ingredientsFacade.fetchAllByDevice(deviceId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientResponse create(@RequestHeader("X-Device-Id") @NotBlank String deviceId, @Valid @RequestBody CreateIngredientRequest request) {
        return ingredientsFacade.create(request, deviceId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse update(@PathVariable @NotBlank String id, @RequestBody PatchIngredientRequest request) {
        return ingredientsFacade.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotBlank String id) {
        ingredientsFacade.delete(id);
    }

    @ExceptionHandler(IngredientInUseException.class)
    public ProblemDetail handleIngredientInUse(IngredientInUseException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }
}