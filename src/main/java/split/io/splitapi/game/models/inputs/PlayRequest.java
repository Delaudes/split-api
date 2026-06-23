package split.io.splitapi.game.models.inputs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import split.io.splitapi.game.models.ActionType;

public record PlayRequest(
        @NotBlank String playerId,
        int x,
        int y,
        @NotNull ActionType actionType,
        int round
) {
}
