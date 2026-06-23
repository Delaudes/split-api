package split.io.splitapi.game.models.outputs;

import split.io.splitapi.game.models.ActionType;

public record ActionResponse(int round, ActionType type, int x, int y) {
}
