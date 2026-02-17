package split.io.splitapi.room.models.inputs;

import jakarta.validation.constraints.NotNull;

public record EditRoomRequest(
        @NotNull String name
) {}
