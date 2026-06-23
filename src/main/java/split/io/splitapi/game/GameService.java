package split.io.splitapi.game;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.game.models.Action;
import split.io.splitapi.game.models.Game;

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
        gamePort.addAction(action);
    }
}
