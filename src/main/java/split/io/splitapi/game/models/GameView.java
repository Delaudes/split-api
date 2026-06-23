package split.io.splitapi.game.models;

import java.util.List;

public record GameView(List<Action> playerActions, List<Action> opponentActions) {
}
