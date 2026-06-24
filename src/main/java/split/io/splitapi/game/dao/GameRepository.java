package split.io.splitapi.game.dao;

import split.io.splitapi.game.models.entities.GameEntity;

import java.util.Optional;

public interface GameRepository {
    void save(GameEntity gameEntity);
    Optional<GameEntity> findById(String id);
}
