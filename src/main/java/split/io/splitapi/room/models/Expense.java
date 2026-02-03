package split.io.splitapi.room.models;

import java.math.BigDecimal;

public record Expense(String id, String description, BigDecimal amount) {

}
