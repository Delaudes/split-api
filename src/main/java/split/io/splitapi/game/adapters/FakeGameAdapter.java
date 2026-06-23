package split.io.splitapi.game.adapters;

import split.io.splitapi.game.GamePort;
import split.io.splitapi.game.models.Action;
import split.io.splitapi.game.models.Game;

import java.util.ArrayList;

public class FakeGameAdapter implements GamePort {

    public Game game = new Game("fake-game-id", "fake-player-id", null, new ArrayList<>());
    public String savedOpponentId;
    public String savedOpponentGameId;
    public Action savedAction;

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

    @Override
    public void addAction(Action action) {
        this.savedAction = action;
    }
}
