package split.io.splitapi.room.dao;

import split.io.splitapi.room.models.entities.PayerEntity;

import java.util.Optional;

public interface PayerRepository {
    void save(PayerEntity payerEntity);
    Optional<PayerEntity> findById(String id);
    void deleteById(String id);
}
