package split.io.splitapi.game;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.game.models.Action;
import split.io.splitapi.game.models.Game;
import split.io.splitapi.game.models.GameView;

@RequiredArgsConstructor
public class GameService {

    private final GamePort gamePort;

    public void createGame(Game game) {
        gamePort.create(game);
    }

    public void joinGame(String gameId, String opponentId) {
        Game game = gamePort.fetchGame(gameId);
        if (game.hasOpponent()) {
            throw new RuntimeException("Game already has an opponent");
        }
        gamePort.saveOpponentId(gameId, opponentId);
    }

    public void play(Action action) {
        Game game = gamePort.fetchGame(action.gameId());
        if (game.isNotPlayer(action.playerId())) {
            throw new RuntimeException("Player not part of this game");
        }
        if (game.isInvalidAction(action)) {
            throw new RuntimeException("Invalid action for this round");
        }
        gamePort.addAction(action);
    }

    public GameView fetchGameForPlayer(String gameId, String playerId) {
        Game game = gamePort.fetchGame(gameId);
        if (game.isNotPlayer(playerId)) {
            throw new RuntimeException("Player not part of this game");
        }
        return game.buildPlayerView(playerId);
    }
}
