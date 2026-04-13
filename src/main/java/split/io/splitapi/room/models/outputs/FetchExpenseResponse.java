package split.io.splitapi.room.models.outputs;

import java.math.BigDecimal;
import java.util.List;

public record FetchExpenseResponse(String id, String description, BigDecimal amount, List<String> excludedPayersId) {
}
