package split.io.splitapi.room.models;

import java.math.BigDecimal;

public record FetchExpenseResponse(String id, String description, BigDecimal amount) {
}
