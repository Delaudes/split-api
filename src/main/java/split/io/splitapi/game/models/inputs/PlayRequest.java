package split.io.splitapi.game.models.inputs;

import jakarta.validation.constraints.NotBlank;

public record PlayRequest(
        @NotBlank String playerId,
        int x,
        int y
) {
}
