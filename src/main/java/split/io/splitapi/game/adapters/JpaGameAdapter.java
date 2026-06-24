package split.io.splitapi.game.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import split.io.splitapi.game.GamePort;
import split.io.splitapi.game.dao.ActionRepository;
import split.io.splitapi.game.dao.GameRepository;
import split.io.splitapi.game.models.Action;
import split.io.splitapi.game.models.Game;
import split.io.splitapi.game.models.entities.ActionEntity;
import split.io.splitapi.game.models.entities.GameEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaGameAdapter implements GamePort {

    private final GameRepository gameRepository;
    private final ActionRepository actionRepository;

    @Override
    public void create(Game game) {
        GameEntity gameEntity = new GameEntity(game.id(), game.playerId());
        gameRepository.save(gameEntity);
    }

    @Override
    public Game fetchGame(String id) {
        GameEntity entity = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + id));
        return mapToGame(entity);
    }

    @Override
    public void saveOpponentId(String gameId, String opponentId) {
        GameEntity entity = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));
        entity.setOpponentId(opponentId);
        gameRepository.save(entity);
    }

    @Override
    public void addAction(Action action) {
        ActionEntity entity = new ActionEntity(
                action.id(), action.gameId(), action.playerId(),
                action.x(), action.y(), action.type(), action.round()
        );
        actionRepository.save(entity);
    }

    private Game mapToGame(GameEntity entity) {
        ArrayList<Action> actions = entity.getActions().stream()
                .map(a -> new Action(a.getId(), a.getGameId(), a.getPlayerId(), a.getX(), a.getY(), a.getType(), a.getRound()))
                .collect(Collectors.toCollection(ArrayList::new));
        return new Game(entity.getId(), entity.getPlayerId(), entity.getOpponentId(), actions);
    }
}
