package split.io.splitapi.gohan.shopping;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.gohan.shopping.models.ShoppingItem;
import split.io.splitapi.gohan.shopping.models.outputs.ShoppingListResponse;

import java.util.List;

@RequiredArgsConstructor
public class ShoppingFacade {

    private final ShoppingService shoppingService;
    private final ShoppingMapper shoppingMapper;

    public ShoppingListResponse fetchAllByDevice(String deviceId) {
        List<ShoppingItem> items = shoppingService.fetchAllByDevice(deviceId);
        return shoppingMapper.toShoppingListResponse(items);
    }
}
