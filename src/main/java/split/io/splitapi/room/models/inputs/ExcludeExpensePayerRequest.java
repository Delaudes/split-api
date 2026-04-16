package split.io.splitapi.room.models.inputs;

import jakarta.validation.constraints.NotNull;

public record ExcludeExpensePayerRequest(
        @NotNull String expenseId,
        @NotNull String payerId
) {}
