package split.io.splitapi.room.dao;

import split.io.splitapi.room.models.entities.ExcludedPayerEntity;

public class FakeExcludedPayerRepository implements ExcludedPayerRepository {

    public ExcludedPayerEntity savedExcludedPayer;
    public ExcludedPayerEntity deletedExcludedPayer;

    @Override
    public void save(ExcludedPayerEntity excludedPayerEntity) {
        this.savedExcludedPayer = excludedPayerEntity;
    }

    @Override
    public void delete(ExcludedPayerEntity excludedPayerEntity) {
        this.deletedExcludedPayer = excludedPayerEntity;
    }
}
