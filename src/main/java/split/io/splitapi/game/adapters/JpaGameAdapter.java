package split.io.splitapi.game.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import split.io.splitapi.game.GamePort;
import split.io.splitapi.game.dao.GameRepository;
import split.io.splitapi.game.models.Game;
import split.io.splitapi.game.models.entities.GameEntity;

@Component
@RequiredArgsConstructor
public class JpaGameAdapter implements GamePort {

    private final GameRepository gameRepository;

    @Override
    public void create(Game game) {
        GameEntity gameEntity = new GameEntity(game.id(), game.playerId());
        gameRepository.save(gameEntity);
    }

    @Override
    public Game fetchGame(String id) {
        GameEntity entity = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + id));
        return new Game(entity.getGameId(), entity.getPlayerId(), entity.getOpponentId());
    }

    @Override
    public void saveOpponentId(String gameId, String opponentId) {
        GameEntity entity = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));
        entity.setOpponentId(opponentId);
        gameRepository.save(entity);
    }
}
