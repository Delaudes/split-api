package split.io.splitapi.game.models;

public record Action(String id, String gameId, String playerId, int x, int y, ActionType type, int round) {
}
