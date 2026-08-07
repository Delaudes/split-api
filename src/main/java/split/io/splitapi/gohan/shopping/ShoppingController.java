package split.io.splitapi.gohan.shopping;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import split.io.splitapi.gohan.shopping.models.outputs.ShoppingListResponse;

@RestController
@RequestMapping("/gohan/shopping")
@RequiredArgsConstructor
@Validated
public class ShoppingController {

    private final ShoppingFacade shoppingFacade;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListResponse fetchAllByDevice(@RequestHeader("X-Device-Id") @NotBlank String deviceId) {
        return shoppingFacade.fetchAllByDevice(deviceId);
    }
}
