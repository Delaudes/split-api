package split.io.splitapi.game.dao;

import split.io.splitapi.game.models.entities.ActionEntity;

public class FakeActionRepository implements ActionRepository {

    public ActionEntity savedAction;

    @Override
    public void save(ActionEntity actionEntity) {
        this.savedAction = actionEntity;
    }
}
