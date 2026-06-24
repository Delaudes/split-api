package split.io.splitapi.game;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.game.models.Action;
import split.io.splitapi.game.models.ActionType;
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

    public void play(String gameId, String actionId, String playerId, int x, int y) {
        Game game = gamePort.fetchGame(gameId);
        if (game.isNotPlayer(playerId)) {
            throw new RuntimeException("Player not part of this game");
        }
        int round = game.getNextRound(playerId);
        ActionType type = game.getNextActionType(playerId);
        gamePort.addAction(new Action(actionId, gameId, playerId, x, y, type, round));
    }

    public GameView fetchGameForPlayer(String gameId, String playerId) {
        Game game = gamePort.fetchGame(gameId);
        if (game.isNotPlayer(playerId)) {
            throw new RuntimeException("Player not part of this game");
        }
        return game.buildPlayerView(playerId);
    }
}
