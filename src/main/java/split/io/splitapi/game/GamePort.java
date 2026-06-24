package split.io.splitapi.game;

import split.io.splitapi.game.models.Action;
import split.io.splitapi.game.models.Game;

public interface GamePort {

    void create(Game game);
    Game fetchGame(String id);
    void saveOpponentId(String gameId, String opponentId);
    void addAction(Action action);
}
