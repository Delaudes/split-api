package split.io.splitapi.game.dao;

import org.springframework.data.repository.Repository;
import split.io.splitapi.game.models.entities.GameEntity;

public interface JpaGameRepository extends Repository<GameEntity, String>, GameRepository {
}
