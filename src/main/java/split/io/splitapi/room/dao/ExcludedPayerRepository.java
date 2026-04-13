package split.io.splitapi.room.dao;

import split.io.splitapi.room.models.entities.ExcludedPayerEntity;

public interface ExcludedPayerRepository {
    void save(ExcludedPayerEntity excludedpayerEntity);
    void delete(ExcludedPayerEntity excludedpayerEntity);
}
