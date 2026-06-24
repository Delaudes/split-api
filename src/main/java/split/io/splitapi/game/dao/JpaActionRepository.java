package split.io.splitapi.game.dao;

import org.springframework.data.repository.Repository;
import split.io.splitapi.game.models.entities.ActionEntity;

public interface JpaActionRepository extends Repository<ActionEntity, String>, ActionRepository {
}
