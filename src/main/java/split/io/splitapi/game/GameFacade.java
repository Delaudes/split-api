package split.io.splitapi.game;

import lombok.RequiredArgsConstructor;
import split.io.splitapi.game.models.Game;
import split.io.splitapi.game.models.GameView;
import split.io.splitapi.game.models.inputs.PlayRequest;
import split.io.splitapi.game.models.outputs.CreateGameResponse;
import split.io.splitapi.game.models.outputs.FetchGameResponse;
import split.io.splitapi.game.models.outputs.JoinGameResponse;
import split.io.splitapi.uuid.UuidGenerator;

@RequiredArgsConstructor
public class GameFacade {

    private final GameService gameService;
    private final GameMapper gameMapper;
    private final UuidGenerator uuidGenerator;

    public CreateGameResponse create() {
        String gameId = uuidGenerator.generate();
        String playerId = uuidGenerator.generate();
        Game game = gameMapper.toGame(gameId, playerId);
        gameService.createGame(game);
        return gameMapper.toCreateGameResponse(game);
    }

    public JoinGameResponse join(String gameId) {
        String opponentId = uuidGenerator.generate();
        gameService.joinGame(gameId, opponentId);
        return gameMapper.toJoinGameResponse(gameId, opponentId);
    }

    public void play(String gameId, PlayRequest request) {
        String actionId = uuidGenerator.generate();
        gameService.play(gameId, actionId, request.playerId(), request.x(), request.y());
    }

    public FetchGameResponse fetchGameForPlayer(String gameId, String playerId) {
        GameView gameView = gameService.fetchGameForPlayer(gameId, playerId);
        return gameMapper.toFetchGameResponse(gameView);
    }
}
