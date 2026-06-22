package split.io.splitapi.game.dao;

import split.io.splitapi.game.models.entities.GameEntity;

import java.util.Optional;

public class FakeGameRepository implements GameRepository {

    public GameEntity savedGame;
    public GameEntity gameToReturn;
    public String findByIdParam;

    @Override
    public void save(GameEntity gameEntity) {
        this.savedGame = gameEntity;
    }

    @Override
    public Optional<GameEntity> findById(String id) {
        this.findByIdParam = id;
        return Optional.ofNullable(gameToReturn);
    }
}
