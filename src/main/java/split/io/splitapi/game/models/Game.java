package split.io.splitapi.game.models;

import java.util.List;

public record Game(String id, String playerId, String opponentId, List<Action> actions) {

    public boolean hasOpponent() {
        return opponentId != null;
    }
}
