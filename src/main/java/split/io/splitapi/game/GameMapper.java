package split.io.splitapi.game;

import split.io.splitapi.game.models.Game;
import split.io.splitapi.game.models.outputs.CreateGameResponse;
import split.io.splitapi.game.models.outputs.JoinGameResponse;

public class GameMapper {

    public Game toGame(String gameId, String playerId) {
        return new Game(gameId, playerId, null);
    }

    public CreateGameResponse toCreateGameResponse(Game game) {
        return new CreateGameResponse(game.id(), game.playerId());
    }

    public JoinGameResponse toJoinGameResponse(String gameId, String opponentId) {
        return new JoinGameResponse(gameId, opponentId);
    }
}
