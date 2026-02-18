package split.io.splitapi.room.dao;

import split.io.splitapi.room.models.entities.PayerEntity;

import java.util.Optional;

public class FakePayerRepository implements PayerRepository {

    public PayerEntity savedPayer;
    public String findByIdParam;
    public PayerEntity payerToReturn;
    public String deleteByIdParam;

    @Override
    public void save(PayerEntity payerEntity) {
        this.savedPayer = payerEntity;
    }

    @Override
    public Optional<PayerEntity> findById(String id) {
        this.findByIdParam = id;
        return Optional.ofNullable(payerToReturn);
    }

    @Override
    public void deleteById(String id) {
        this.deleteByIdParam = id;
    }
}
