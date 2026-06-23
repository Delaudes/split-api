package split.io.splitapi.game.models.outputs;

import java.util.List;

public record FetchGameResponse(List<ActionResponse> playerActions, List<ActionResponse> opponentActions) {
}
