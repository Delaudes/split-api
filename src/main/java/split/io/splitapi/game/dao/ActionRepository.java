package split.io.splitapi.game.dao;

import split.io.splitapi.game.models.entities.ActionEntity;

public interface ActionRepository {
    void save(ActionEntity actionEntity);
}
