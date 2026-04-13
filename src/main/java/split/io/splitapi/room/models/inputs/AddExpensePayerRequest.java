package split.io.splitapi.room.models.inputs;

import jakarta.validation.constraints.NotNull;

public record AddExpensePayerRequest(
        @NotNull String expenseId,
        @NotNull String payerId
) {}
