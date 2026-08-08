package split.io.splitapi.gohan.shopping.models.outputs;

import java.util.List;

public record ShoppingListResponse(List<ShoppingItemResponse> items) {
}
