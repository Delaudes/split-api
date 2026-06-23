package split.io.splitapi.game;

import split.io.splitapi.game.models.Action;
import split.io.splitapi.game.models.Game;
import split.io.splitapi.game.models.inputs.PlayRequest;
import split.io.splitapi.game.models.outputs.CreateGameResponse;
import split.io.splitapi.game.models.outputs.JoinGameResponse;

import java.util.ArrayList;

public class GameMapper {

    public Game toGame(String gameId, String playerId) {
        return new Game(gameId, playerId, null, new ArrayList<>());
    }

    public CreateGameResponse toCreateGameResponse(Game game) {
        return new CreateGameResponse(game.id(), game.playerId());
    }

    public JoinGameResponse toJoinGameResponse(String gameId, String opponentId) {
        return new JoinGameResponse(gameId, opponentId);
    }

    public Action toAction(String gameId, String actionId, PlayRequest request) {
        return new Action(actionId, gameId, request.playerId(), request.x(), request.y(), request.actionType(), request.round());
    }
}
