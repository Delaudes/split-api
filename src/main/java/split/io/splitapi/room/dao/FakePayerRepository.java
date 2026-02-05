package split.io.splitapi.room.dao;

import split.io.splitapi.room.models.entities.PayerEntity;

public class FakePayerRepository implements PayerRepository {

    public PayerEntity savedPayer;

    @Override
    public void save(PayerEntity payerEntity) {
        this.savedPayer = payerEntity;
    }
}
