package split.io.splitapi.room.dao;

import org.springframework.data.repository.Repository;
import split.io.splitapi.room.models.entities.PayerEntity;

public interface JpaPayerRepository extends Repository<PayerEntity, String>, PayerRepository {
}
