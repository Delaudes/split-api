package split.io.splitapi.room.models;

import jakarta.validation.constraints.NotNull;

public record CreateRoomRequest(
        @NotNull String name
) {}
