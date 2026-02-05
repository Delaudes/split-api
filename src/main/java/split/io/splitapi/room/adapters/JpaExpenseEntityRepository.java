package split.io.splitapi.room.adapters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import split.io.splitapi.room.models.entities.ExpenseEntity;

@Repository
public interface JpaExpenseEntityRepository extends JpaRepository<ExpenseEntity, String> {
}
