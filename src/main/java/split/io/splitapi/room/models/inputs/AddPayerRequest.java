package split.io.splitapi.room.models.inputs;

import jakarta.validation.constraints.NotNull;

public record AddPayerRequest(
        @NotNull String roomId,
        @NotNull String payerName
) {}
