package split.io.splitapi.room.dao;

import split.io.splitapi.room.models.entities.PayerEntity;

public interface PayerRepository {
    void save(PayerEntity payerEntity);
}
