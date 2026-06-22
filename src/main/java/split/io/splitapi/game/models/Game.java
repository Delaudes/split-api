package split.io.splitapi.game.models;

public record Game(String id, String playerId, String opponentId) {

    public boolean hasOpponent() {
        return opponentId != null;
    }
}
