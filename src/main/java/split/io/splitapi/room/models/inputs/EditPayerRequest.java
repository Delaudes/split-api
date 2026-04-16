package split.io.splitapi.room.models.inputs;

import jakarta.validation.constraints.NotNull;

public record EditPayerRequest(
        @NotNull String name
) {}
