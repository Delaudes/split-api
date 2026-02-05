package split.io.splitapi.room.models.inputs;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AddExpenseRequest(
        @NotNull String payerId,
        @NotNull String expenseDescription,
        @NotNull BigDecimal expenseAmount
) {}
