package split.io.splitapi.room.dao;

import org.springframework.data.repository.Repository;
import split.io.splitapi.room.models.entities.ExcludedPayerEntity;

public interface JpaExcludedPayerRepository extends Repository<ExcludedPayerEntity, String>, ExcludedPayerRepository {
}
