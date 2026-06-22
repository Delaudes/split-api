package split.io.splitapi.game.adapters;

import split.io.splitapi.game.GamePort;
import split.io.splitapi.game.models.Game;

public class FakeGameAdapter implements GamePort {

    public Game game = new Game("fake-game-id", "fake-player-id", null);
    public String savedOpponentId;
    public String savedOpponentGameId;

    @Override
    public void create(Game game) {
        this.game = game;
    }

    @Override
    public Game fetchGame(String id) {
        return game;
    }

    @Override
    public void saveOpponentId(String gameId, String opponentId) {
        this.savedOpponentGameId = gameId;
        this.savedOpponentId = opponentId;
    }
}
