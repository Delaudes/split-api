package split.io.splitapi.room.models;

import jakarta.validation.constraints.NotNull;

public record AddPayerRequest(
        @NotNull String roomId,
        @NotNull String payerName
) {}
